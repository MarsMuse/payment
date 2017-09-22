package com.zhph.payment.proxy;

import com.zhph.api.entity.ConstantPay;
import com.zhph.entity.ParamsEntity;
import com.zhph.entity.PaymentResult;
import com.zhph.payment.charge.entity.ChargeDetailInfo;
import com.zhph.payment.proxy.paylogger.PayLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 切面面  第三方支付日志切面
 * 			此类只做业务处理。不做数据库操作
 * @author kang 2017年7月12日上午9:43:18
 */
@Aspect
@Component
public class PayLogAspect {
	private final Logger logger = LoggerFactory.getLogger(PayLogAspect.class);
	//对数据库操作接口
	@Resource
	private PayLogService payLogService;
	/**
	 * 支付点切入类
	 */
	@Pointcut("execution(public * com.zhph.channel.PaymentChannel.*(..))")
	public void controllerAspect() {
	}
	/**
	 * 请求方法前
	 * @author likang
	 * @date 2017-7-25下午2:14:24
	 */
	@Before(value ="controllerAspect()")
	public void before(JoinPoint joinPoint) {
		String targetName = joinPoint.getTarget().getClass().getName();//包名
		String methodName = joinPoint.getSignature().getName();//方法名
		logger.info("=====开始执行（"+targetName+"...."+methodName+"）---before  前置通知=====");
		List<Object> listArguments = getListArguments(joinPoint);
		Object firstObj = listArguments.get(0);//第一个参数
		if(firstObj instanceof ChargeDetailInfo ){
			Object  objEntity =  listArguments.get(0);
			if(objEntity instanceof ParamsEntity){//单扣操作
				ParamsEntity entity = (ParamsEntity)objEntity;
				if(entity.getChargeType() == ConstantPay.SINGLESTATE){
					payLogService.updateReqChargeRecordInfo("0", null, "0", entity.getChargeNo(),null,"0");
				}else{//批扣 / 循环单扣 / 单扣查询 /批扣查询
					//批扣的已在前面记录了。这里不需要记录
					logger.info("未处理此方法");
				}
			}
		}else if(firstObj instanceof List){//批扣list暂时不操作
			logger.info("批扣操作");
		}
		logger.info("=====结束执行（"+targetName+"...."+methodName+"）---before  前置通知=====");
	}
	/**
	 * 配置后置返回通知
	 */
	@AfterReturning(pointcut ="controllerAspect()",returning="obj")
	public void afterReturn(JoinPoint joinPoint,Object obj) {
		String targetName = joinPoint.getTarget().getClass().getName();//包名
		String methodName = joinPoint.getSignature().getName();//方法名
		logger.info("=====开始执行（"+targetName+"...."+methodName+"）---afterReturn  后置返回通知=====");
		List<Object> listArguments = getListArguments(joinPoint);
		Object firstObj = listArguments.get(0);//第一个参数
		if(firstObj instanceof ChargeDetailInfo ){//单扣操作与循环单扣操作
			PaymentResult pr ;
			if(obj instanceof PaymentResult){
				pr = (PaymentResult)obj;//接收参数
				ParamsEntity entity = (ParamsEntity) listArguments.get(0);//请求参数
				payLogService.SingleRequestPayHandllog(entity, pr,targetName);//请求报文
				if(pr.getRespContent() != null)//支付异常的情况下才为会null
					payLogService.SingleResponsePayHandlog(entity, pr,targetName);//结束报文 处理结果状态
			}
		}else if(firstObj instanceof List){//  不操作批扣 循环单扣
			logger.info("{}该渠道在调用日志记录",targetName);
			String batchNo = (String) listArguments.get(1);
			PaymentResult pr ;
			if(obj instanceof PaymentResult) {
				pr = (PaymentResult) obj;
				payLogService.BatchResponsePayHandlog(listArguments.get(0), pr, batchNo, targetName,ConstantPay.REQUEST);//请求报文
				payLogService.BatchResponsePayHandlog(listArguments.get(0), pr, batchNo, targetName,ConstantPay.RESPONSE);//返回报文
			}else{
				logger.info("{}平台未知返回类型",targetName);
			}
		}else if(listArguments.size() ==2){//批扣查询
				String batchNo = (String) listArguments.get(0);
				PaymentResult pr = null;
				if(obj instanceof  PaymentResult){
					pr = (PaymentResult) obj;
					payLogService.BatchResponsePayHandlog(null, pr, batchNo, targetName,ConstantPay.BATCHSTATE_REQ_QUERY);//请求报文
					payLogService.BatchResponsePayHandlog(null, pr, batchNo, targetName,ConstantPay.BATCHSTATE_RESP_QUERY);//返回报文
				}
		}else{//暂时不知道该方法是什么方法
			logger.error("在切面编程中，还没定义该接口方法。与参数类型");
		}
		logger.info("=====结束执行（"+targetName+"...."+methodName+"）---afterReturn  后置返回通知=====");
	}

	/**
	 * 异常通知 用于拦截记录异常日志
	 */
	@AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint,Throwable e) {
		String targetName = joinPoint.getTarget().getClass().getName();//包名
		String methodName = joinPoint.getSignature().getName();//方法名
		logger.info("=====开始执行（"+targetName+"...."+methodName+"）---发生异常信息");
		try {
			List<Object> listArguments = getListArguments(joinPoint);
			Object firstObj = listArguments.get(0);//第一个参数
			if(firstObj instanceof ParamsEntity ){//单扣
				//更新业务库状态 暂时未做
				logger.error("错误日志："+e.getMessage());//只记录。不存库。字段可能存不下。
				//payLogService.updateReqChargeRecordInfo("2", e.getMessage(), "1", entity.getChargeNo(),null,"1");
				payLogService.ExceptionPaylog((ParamsEntity)listArguments.get(0), e,targetName);
			}else if(firstObj instanceof List){//批扣暂时不操作 list 第一个参数是list
				if(listArguments.size() == 2)//批扣查询
					logger.error("在切面编程中，（{}）批扣查询这个方法类中接口参数哪里写错了,第一个参数为：{}",targetName,firstObj.toString());
				else
					logger.error("在切面编程中，（"+targetName+"）批扣这个方法类中接口参数哪里写错了");
			}else{
				logger.error("在切面编程中，（"+targetName+"）这个方法类中接口参数哪里写错了");
			}
		} catch (Exception eq) {
			logger.error("数据库插入错误"+eq.getMessage());
			eq.printStackTrace();
		}
		logger.error("=====结束执行（"+targetName+"...."+methodName+"）---扣款异常信息:",e);
	}
	/**
	 * 获取请求参数 列表
	 * @author likang
	 * @date 2017-7-20上午10:05:45
	 */
	private List<Object> getListArguments(JoinPoint joinPoint) {
		Object[] arguments = joinPoint.getArgs();//参数
		List<Object> list =null ;//取参数
		if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
			list = new ArrayList<>();
			for (int i = 0; i < joinPoint.getArgs().length; i++) {
				list.add(arguments[i]);
			}
		}
		return list;
	}
	

}
