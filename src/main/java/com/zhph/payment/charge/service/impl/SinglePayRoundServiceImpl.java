package com.zhph.payment.charge.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhph.api.entity.ChargeOperationException;
import com.zhph.api.entity.ConstantPay;
import com.zhph.api.entity.PlatformCodeAndData;
import com.zhph.api.entity.ResultInformation;
import com.zhph.base.common.StringOperator;
import com.zhph.base.common.StringUtil;
import com.zhph.base.redis.JedisLock;
import com.zhph.base.redis.RedisUtil;
import com.zhph.entity.ParamsEntity;
import com.zhph.payment.charge.dao.BankNormalLimitMapper;
import com.zhph.payment.charge.dao.PaymentRecordMapper;
import com.zhph.payment.charge.dao.SingleChargeDao;
import com.zhph.payment.charge.entity.*;
import com.zhph.payment.charge.service.PushChargeInfoService;
import com.zhph.payment.charge.service.SinglePayRoundService;
import com.zhph.payment.charge.service.business.PaymentService;
import com.zhph.payment.charge.util.BatchChargePushExecutor;
import com.zhph.payment.charge.util.ChargeDataVerify;
import com.zhph.payment.charge.util.UUIDUtil;
import com.zhph.utils.ArithDoubleUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * 抽象环绕循环扣款  
 *   判断渠道。如果没有指定渠道。根据数据库优先级选择渠道，
 *   	 <p/>1.判断交易金额是否可扣。 如果不可扣。处理异常。
 *       <p/>2.判断交易金额。如果金额过大。拆分金额
 *       <p/>3.根据合同号判断银行限制 
 * @author likang
 */
@Service("singlePayRoundServiceImpl")
public class SinglePayRoundServiceImpl implements SinglePayRoundService{
	
	 //日志打印对象
    private final Logger log = LoggerFactory.getLogger(SinglePayRoundServiceImpl.class);
	@Resource
	private PaymentRecordMapper paymentRecordMapper;
	@Resource
	private BankNormalLimitMapper bankNormalLimitMapper; 
    @Resource
    private ChannelBeanServiceImp channelBeanService;
    @Resource
	private SingleChargeDao singleChargeDao;
	//推送服务
	@Resource
	private PushChargeInfoService pushChargeInfoService;

	/**
	 * @author likang
	 * @date 2017-7-18下午5:40:14
	 */
	public ResultInformation singlePayService(String message) {
		ResultInformation resultInfo = new ResultInformation("1", "请求成功");
		// 获取到平台信息及数据
		PlatformCodeAndData pcd = JSON.parseObject(message,	PlatformCodeAndData.class);
		log.info(pcd.getPlatformCode()+"平台请求成功!参数为："+JSON.toJSONString(pcd));
		ParamsEntity info = JSON.parseObject(pcd.getData(), ParamsEntity.class);
		info.setPlatformCode(pcd.getPlatformCode());
		info.setChargeType(1);

		// 首先插入批次信息到数据库中存储无效数据
		if (!inValidDetail(info)) {// 数据检验不正确
			 info.setFlag("0");
		} else {
			info.setFlag("1");
		}
		JedisLock lock = RedisUtil.getSinglePaymentLock(info.getLoanNo() ,false);
 		try {
			if (lock.acquire()) {
				resultInfo = singleServiceHandler(info,pcd);
			}else{
				resultInfo.setMessage("系统繁忙！请等候处理");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultInfo.setCode("2");
			log.error(e.getMessage());
			resultInfo.setMessage("异常了。。"+e.getMessage());
			if(e instanceof SocketTimeoutException){
				resultInfo.setMessage("请求超时。。。。。Read timed out");
			}
		} finally {
			lock.release();
		}
		return resultInfo;

	}

	/**
	 * 单扣查询服务
	 * @param message 传入消息体
	 * @return 消息体
	 */
	@Override
	public ResultInformation singleQueryService(String message) {
		ResultInformation resultInfo = new ResultInformation("1", "请求成功");
		PlatformCodeAndData pcd = JSON.parseObject(message,	PlatformCodeAndData.class);	// 获取到平台信息及数据
		log.info(pcd.getPlatformCode()+"平台请求成功!参数为："+JSON.toJSONString(pcd));
		ParamsEntity info = JSON.parseObject(pcd.getData(), ParamsEntity.class);
		info.setPlatformCode(pcd.getPlatformCode());
		if(StringUtil.isNotEmpty(info.getChargeNo())) {
			try{
				List<ChargeRecordDetail> listDetail = singleChargeDao.getSingleQueryPayingChargeInfo(info.getChargeNo());//查询未推送的数据
				Integer countSize = singleChargeDao.getSingleQueryPayingSize(info.getChargeNo());//记录了多少条数据
				//业务库有数据 初始化表中有记录
				if (listDetail != null && pcd.getPlatformCode() != null ) {
					if(listDetail.size() != 0 && countSize!= null && countSize>0){//表示当前交易号未推送  回盘业务平台接口。重新发起推送
						BatchPushPlatformInfo platforminfo = singleChargeDao.getNeedPushPlatformInfoByCode(pcd.getPlatformCode());
						if (platforminfo != null) {//平台不为空
							List<SingleChargeInfo> singleList = new ArrayList<>();
							boolean isPush = true;
							for (ChargeRecordDetail detail : listDetail) {
								if(detail.getCharge_status().equals("0")){//扣款中的数据返回扣款中
									resultInfo.setCode("0");
									resultInfo.setMessage("扣款中");
									isPush = false;
									break;
								}
								SingleChargeInfo singleQuery = new SingleChargeInfo();
								singleQuery.setChargeNo(detail.getCharge_no());
								singleQuery.setChargeMessage(detail.getCharge_message());
								singleQuery.setChargeStatus(detail.getCharge_status());
								singleQuery.setChargeTime(detail.getCharge_time());
								singleQuery.setLoanNo(detail.getLoan_no());
								singleQuery.setRealAmount(detail.getAmount());
								singleQuery.setChargeChannelCode(detail.getCharge_channel_code());
								//singleQuery.setGroupChargeNo(info.getChargeNo());
								singleQuery.setGroupChargeNo(detail.getCut_no());
								singleQuery.setOperateName(detail.getOperate_name());
								singleList.add(singleQuery);
							}
							if(isPush){//重新开启推送服务，，同步返回结果
								pushSingleQuery(singleList,platforminfo,resultInfo,listDetail);
							}
						} else {//未找到平台
							resultInfo.setCode("2");
							resultInfo.setMessage("平台检测未到。。。。。。");
						}
					}else if(listDetail.size() == 0 && countSize != null && countSize>0 ){//已推送成功 初始化表中有记录
						List<ChargeRecordDetail> list = singleChargeDao.getPayingChargeInfoById(info.getChargeNo());
						String valmessage = "";
						String code  = "2";
						if(list != null && list.size()>0){//业务库查到此交易号
							MessageTitle amountTitle = new MessageTitle(list).invoke();
							valmessage = amountTitle.getValmessage();
							code = amountTitle.getCode();
						}else{
							valmessage = "未找到交易号！此交易未成交!";
						}
						resultInfo.setCode(code);
						resultInfo.setMessage(valmessage);
					}else{
						resultInfo.setCode("2");
						resultInfo.setMessage("此交易号未找到。请检查是否正确");
					}
				} else {//业务库里没有数据。初始化库也没有数据
					resultInfo.setCode("2");
					resultInfo.setMessage("请求对象为空");
				}
			}catch (Exception ex){
				resultInfo.setCode("2");
				resultInfo.setMessage("请求出现异常");
				ex.printStackTrace();
			}
		}
		resultInfo.setResultNo(info.getChargeNo());
		resultInfo.setChargeChannelCode(info.getPaymentChannel());
		return resultInfo;
	}

	/**
	 * 判断数组是否存在
	 * @param arr
	 * @param targetValue
	 * @return
	 */
	public static boolean useArraysBinarySearch(Integer[] arr, Integer targetValue) {
		if(arr == null || arr.length <= 0 || targetValue == null )
			return  false;
		boolean isShow = false;
		for (int i =0;i<arr.length;i++){
			if(arr[i] == targetValue ){
				isShow = true;
				break;
			}
		}
		return  isShow;
	}
	/**
	 * 单扣查询推送
	 * @param platforminfo  请求参数
	 * @param platforminfo  基础信息
	 * @param resultInfo 	 封装要处理信息
	 */
	private void pushSingleQuery( final List<SingleChargeInfo> SingleChargeInfo,final BatchPushPlatformInfo platforminfo,ResultInformation resultInfo,final List<ChargeRecordDetail> listDetail ){
		String sourceData = JSON.toJSONString(SingleChargeInfo);//明文
		String result = null;//获取到返回结果
		try{
			result = pushChargeInfoService.pushDataEncryptAndDecrypt(sourceData ,platforminfo );
		}catch (Exception e){
			if(e instanceof ChargeOperationException){
                log.info("查询，异常更新数据开始");//记录失败次数
                this.singleChargeDao.updatePushErrorInfo(SingleChargeInfo);
                log.info("查询，更新查询数据结束");
                log.error(platforminfo.getPlatformCode()+"平台，请求失败"+e.getMessage());
                resultInfo.setCode("2");
                resultInfo.setMessage("查询异常{}"+e.getMessage());
			}
			e.printStackTrace();
		}finally {
			MessageTitle amountTitle = new MessageTitle(listDetail).invoke();
			if (result != null) {
				List<ChargePushBackInfo> pushBackInfo = JSON.parseArray(result, ChargePushBackInfo.class);	//转换为集合
				if (pushBackInfo != null && (!pushBackInfo.isEmpty())) {
					this.singleChargeDao.updateSinglePushInfo(pushBackInfo);//批量更新
					log.info("平台{}推送成功！支付平台处理数据完成",platforminfo.getPlatformCode());
					resultInfo.setCode(amountTitle.getCode());
					resultInfo.setMessage(amountTitle.getValmessage());
				}else{
					resultInfo.setCode("2");
					resultInfo.setMessage("推送成功！但平台未接收到推送返回数据!稍后重新查询！交易号提示信息："+amountTitle.getValmessage());
				}
			}else{
				log.error("推送{}返回结果为空！",platforminfo.getPlatformCode());
				resultInfo.setCode("2");
				resultInfo.setMessage("未接收到平台推送返回结果，请销后重新请求！交易提示信息："+amountTitle.getValmessage());
			}
		}
	}
	/**
	 * 单扣逻辑与算法处理 第二种算法 费掉第一种算法
	 * @param values 请求参数
	 * @param pcd    基础信息
	 * @return  请求状态
	 */
	private  ResultInformation singleServiceHandler(ChargeDetailInfo values, PlatformCodeAndData pcd){
		ParamsEntity params = (ParamsEntity) values;
		ResultInformation formation = new ResultInformation("1","请求成功");
		Double originAmount = Double.valueOf(params.getAmount()); //原始要扣的现金
		params.setArginiAmount(originAmount.toString());
		Map<String, List<Double>> chossList = selectQueryChannel(originAmount,params.getBankKey(), params.getLoanNo(),params.getMainBody(),pcd.getChannelNo(),formation);
		if(chossList == null || chossList.size() == 0){
			formation.setCode("2");
			if(StringUtil.isEmpty(formation.getMessage()))
				formation.setMessage("未匹配到渠道！请查明原因");
			return formation;
		}
		log.info("当前合同号拆分金额为：："+chossList.toString());
		String  resultNo = "";//返回给第三方合同号
		String channelNo="";//扣款渠道
		SingleChargeBasicInfo basicInfo = new SingleChargeBasicInfo(pcd.getPlatformCode() ,UUIDUtil.getUUID(), pcd.getChannelNo());
		if(chossList.size() == 1){//单渠道扣款
			for (String inChannel : chossList.keySet()) {
				params.setPaymentChannel(inChannel);
				channelNo += inChannel+",";
				List<Double> amountList = chossList.get(inChannel);
				resultNo = UUIDUtil.getChargeNo(params.getLoanNo());//交易流水号
				if(amountList.size() == 1){//单渠道一次性扣款
					params.setAmount(amountList.get(0).toString());//交易金额
					synSingleRequestPayment(inChannel,params,basicInfo,resultNo);
				}else{//单渠道多次扣款
					loopPaymentForList(inChannel,resultNo,params,basicInfo,amountList);
				}
			}
		}else if(chossList.size() > 1){//多渠道扣款
			 resultNo = UUIDUtil.getChargeNo(params.getLoanNo());//交易流水号
			 //这里的null 不应该在这里传递信息
			 channelNo = RequestSingleToo(params, basicInfo, resultNo, chossList);
		}else{
			formation.setCode("2");
			log.info("没有渠道");
		}
		formation.setResultNo(resultNo);
		formation.setChargeChannelCode(channelNo.substring(0,channelNo.length()-1));
		return  formation;
	}

	/**
	 * 单渠道 多次扣款
	 * @param inChannel 渠道
	 * @param cutNo 分组合同号
	 * @param params 请求参数
	 * @param basicInfo 基本信息
	 * @param amountList 拆分金额
	 */
	private void loopPaymentForList(String inChannel, String cutNo, ParamsEntity params, SingleChargeBasicInfo basicInfo, List<Double> amountList) {

		//添加基础信息开始
		basicInfo.setSingleNo(cutNo);//用于基础数据平台合同号
		basicInfo.setSendInfoCount(amountList.size());
		basicInfo.setChannelNo(inChannel);//添加渠道号
		singleChargeDao.insertSingleBasicInfo(basicInfo);
		params.setCutNo(cutNo);
		//添加基础信息结束
		for(int i=0;i<amountList.size();i++){
			params.setAmount(amountList.get(i).toString());
			//重新生成交易号。每个交易号不一样
			params.setChargeNo(UUIDUtil.getChargeNo(params.getLoanNo()));//区分合同号  用于第三方区分合同号存于日志库，合同库
			insertChargeRecordDetail(params);
			log.info(cutNo+"拆分金额第"+(i+1)+"次操作");
			PaymentService paymentService = channelBeanService.getInstance(inChannel,params.getMainBody());
			if(paymentService == null){
				log.error("{},主体{}未找到此注入bean服务。请查看提供类已帮助",inChannel,params.getMainBody());
				continue;
			}
			singlePaymentView(params, paymentService);
			//同步请求
			//ansySinglePayOperation(paymentService,params);//异步请求
			singleChargeDao.updateSingleBatchReplyCount(cutNo,1);//累加添加已更新的条数
		}
		singleChargeDao.updateSingleEndFlag(cutNo);//基础库标识为已结束-标识
		//singleChargeDao.updateSingleBatchReplyAmountMessgae(changeNo);//更新业务库最终状态
	}


	/**
	 * 单步 请求第三方单扣
	 *  并添加流水号，保存业务库，
	 *  @param paymentChannel 渠道号
	 *  @param  params  请求参数
	 *  @param  basicInfo 基础参数
	 *  @param     changeNo 交易号
	 */
	private void synSingleRequestPayment(String paymentChannel, ChargeDetailInfo params,SingleChargeBasicInfo basicInfo,String changeNo){
		if(paymentChannel == null && params.getMainBody().isEmpty()){
			return ;
		}
		try {
			params.setChargeNo(changeNo);
			PaymentService  paymentService = 	channelBeanService.getInstance(paymentChannel,params.getMainBody());//请求第三方
			if(paymentService == null){
				log.error("{},主体{}未找到此注入bean服务。请查看提供类帮助",paymentChannel,params.getMainBody());
				return  ;
			}
			basicInfo.setChannelNo(paymentChannel);//添加渠道号
			this.saveValidList(params,basicInfo);//插入基础信息
			singlePaymentView((ParamsEntity)params, paymentService);
			singleChargeDao.updateSingleBatchReplyCount(changeNo,1);//添加已更新的数据一条。
			singleChargeDao.updateSingleEndFlag(changeNo);//标识为已结束标识
		} catch (Exception e) {
			log.error("存入基础表数据异常参数：{}",params.toString());//第三方处理异常
			e.printStackTrace();
		}
	}



	/**
	 * 添加业务基础数据
	 * @param  params 原始参数
	 * @param  basicInfo 基础信息
	 **/
	@Transactional(rollbackFor = Exception.class)
	private void saveValidList(ChargeDetailInfo params ,SingleChargeBasicInfo basicInfo) {
		try {
			InsertBasicInfoTable(params, basicInfo);
			//初始化新对象
			ParamsEntity paramsNew = new ParamsEntity();
			BeanUtils.copyProperties(paramsNew,params);
			paramsNew.setAmount("0.0");//表示真实已扣金额。第三方请求成功了。在去更新值
			singleChargeDao.insertChargeRecordInfo(paramsNew);//直接存储信息
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 插入基础信息库
	 * @param params
	 * @param basicInfo
	 */
	private void InsertBasicInfoTable(ChargeDetailInfo params, SingleChargeBasicInfo basicInfo) {
		basicInfo.setSendInfoCount(1);
		basicInfo.setSingleNo(params.getChargeNo());
		singleChargeDao.insertSingleBasicInfo(basicInfo);	//基础信息
	}

	/**
	 * 异步扣款请求第三方
	 * @param paymentService 服务方
	 * @param params  参数请求
	 */
	private synchronized void ansySinglePayOperation(final PaymentService paymentService, final ParamsEntity params) {
		Executor exe = BatchChargePushExecutor.getExecutor();	//获取到异步操作
		exe.execute(new Runnable() {//开启多线程进行推送
			@Override
			public void run() {
				singlePaymentView(params, paymentService);
			}
		});
	}


	/**
	 * 请求第三方。多渠道
	 * @param params
	 * @param basicInfo
	 * @param cutNo
	 * @param channelValue
	 */
	private String RequestSingleToo(ParamsEntity params, SingleChargeBasicInfo basicInfo, String cutNo, Map<String, List<Double>> channelValue) {
		Iterator<Map.Entry<String,List<Double>>> it = channelValue.entrySet().iterator();
		params.setCutNo(cutNo);//用于业务平台 数据分组号
		String clannelNo="";
		int count = 0;
		try {
			while (it.hasNext()) {
				Map.Entry<String,List<Double>> entry = it.next();
				String clannelKey = entry.getKey();
				clannelNo += clannelKey+",";
				List<Double> listAmount = entry.getValue();
				//添加基础信息开始
				if(count == 0){//第一次进来
					basicInfo.setSingleNo(cutNo);//第一次用 添加分组号
					basicInfo.setSendInfoCount(listAmount.size());
					basicInfo.setChannelNo(clannelKey);//添加渠道号
					singleChargeDao.insertSingleBasicInfo(basicInfo);	//第一次用添加基础信息
				}else{
					singleChargeDao.updateSinlgeBatchSendCount(cutNo,listAmount.size());//更新基础表的执行次数与条数。
				}
				params.setPaymentChannel(clannelKey);
				PaymentService paymentService = channelBeanService.getInstance(clannelKey,params.getMainBody());
				if(paymentService == null){
					log.error("{},主体{}未找到此注入bean服务。请查看提供类已帮助",clannelKey,params.getMainBody());
					continue;
				}
				//添加基础信息结束
				if(listAmount.size()>0){//多次请求
					for(Double amoumt : listAmount){
						String changeNo =  UUIDUtil.getChargeNo(params.getLoanNo());//交易流水号
						params.setChargeNo(changeNo);//生新组件Id
						params.setPaymentChannel(clannelKey);
						insertChargeRecordDetail(params);
						params.setAmount(String.valueOf(amoumt));
						singlePaymentView(params, paymentService); //同步请求
						//ansySinglePayOperation(paymentService,params);//异步请求 目前切面不支持多线程
						singleChargeDao.updateSingleBatchReplyCount(cutNo,1);//添加已更新的数据一条。
					}
				}
				count ++;
			}
			singleChargeDao.updateSingleEndFlag(cutNo);//基础库标识为已结束-标识
		} catch (Exception e) {
			log.error("操作数据库异常");
			e.printStackTrace();
		}
		return clannelNo;
	}

	/**
	 * 请求第三方
	 * @author likang
	 * @date 2017-8-29下午4:05:22
	 */
	private void singlePaymentView(final ParamsEntity params, final PaymentService paymentService) {
		if(paymentService == null){
			log.error("未找到注入平台服务bean：");// 
		}
		try {
			paymentService.singlePayment(params);//请求第三方
		} catch (Exception e) {
			log.error("{}渠道，主体{}调用服务器异常：",paymentService.getClass().getName(),params.getMainBody());//第三方处理异常
			e.printStackTrace();
		}
	}
 

	/**
	 * 插入基础数据库表
	 * @param params 封装参数
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void insertChargeRecordDetail(ParamsEntity params) {
        try {
        	if(params.getChargeType() == ConstantPay.SINGLESTATE){
                ParamsEntity paramsNew = new ParamsEntity();
			BeanUtils.copyProperties(paramsNew,params);
			paramsNew.setAmount("0"); //初始化已扣金额为0
	        singleChargeDao.insertChargeRecordInfo(paramsNew);//插入基础数据业务库
        	}
		} catch (IllegalAccessException e) {
			log.error("复制bean异常");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("复制bean异常");
			e.printStackTrace();
		}
	}

	/**
	 *  验证数据
	 * @param source
	 *            源数据
	 **/
	private boolean inValidDetail(ChargeDetailInfo source) {
		if (source != null) {// 做数据有效检验
			return ChargeDataVerify.verifyData(source);
		}
		return false;
	}


	/**
	 * 重新计算平台可扣金额
	 * @param originAmount
	 * @param loanNo
	 * @param mainBody
	 * @param financingChannel
	 * @param formation
	 * @return
	 */
	private Map<String, List<Double>>  selectQueryChannel(Double originAmount,String bankKey, String loanNo,
														   String mainBody, String financingChannel, ResultInformation formation) {
		Map<String,Object> bankNormalLimitsMap = new HashMap<>();
		bankNormalLimitsMap.put("bankKey", bankKey);
		bankNormalLimitsMap.put("mainBody", mainBody);
		bankNormalLimitsMap.put("financingChannel", financingChannel);
		String message = "";//消息体
		//查询可扣渠道   优先级
		List<BankNormalLimit> bankNormalLimits = bankNormalLimitMapper.queryByBankKey(bankNormalLimitsMap);
		//查询该合同号当天的扣款失败信息 平台：paymentNum：次数
		Map<String,Map<String,Object>> paymentMap = paymentRecordMapper.queryPaymentNum(loanNo,bankKey,mainBody);
		if(bankNormalLimits == null || bankNormalLimits.size() == 0 ){
			message = "未匹配到渠道";
			formation.setCode("2");
			formation.setMessage(message);
			return null;
		}
		Map<String,Integer> errCount = new HashMap<>();
		//处理第一个平台。。并判断平台是否可用
		for (int i = 0; i < bankNormalLimits.size(); i++){
			BankNormalLimit bankNormalLimit  =bankNormalLimits.get(i);
			String channelNo = bankNormalLimit.getFinancingChannel();
			if(bankNormalLimit.getSingleAmountLimit().equals(-1.0)){
				bankNormalLimit.setSingleAmountLimit(999999999.99d);
			}
			Double totalAmountToo = paymentRecordMapper.sumTotalPaymentAmount(loanNo,channelNo);//该合同号也扣金额
			if (bankNormalLimit.getDayAmountLimit().equals(-1.0)) {
				bankNormalLimit.setDayAmountLimit(999999999.99d);
			}else{
				if( bankNormalLimit.getDayAmountLimit() - totalAmountToo > 0){//直接把当天可扣总金额改为剩余的可扣金额钱
					bankNormalLimit.setDayAmountLimit(bankNormalLimit.getDayAmountLimit() - totalAmountToo);
				}else{//此渠道的扣款金额也达到上限
					message += channelNo+"此渠道的扣款金额也达到上限";
					log.info(message);
					bankNormalLimits.remove(bankNormalLimit);//移除当前渠道
					i=i-1;//每移除一个元素以后再把i移回来
					continue;//下一个平台
				}
			}
			if(paymentMap.get(channelNo) != null){
				int curRepaymentNumber =  Integer.valueOf(paymentMap.get(channelNo).get("paymentNum").toString());
				if (curRepaymentNumber >= bankNormalLimit.getDayCountLimit()) {//大于每日扣款失败次数
					message += loanNo+"合同号"+bankNormalLimit.getChannelName()+"渠道的扣款失败次数 >= 该渠道每日扣款次数限制,跳出当前循环;";
					log.info(message);
					bankNormalLimits.remove(bankNormalLimit);//移除当前渠道
					i=i-1;
					continue;
				}
				//次数问题！
				if(curRepaymentNumber != 0 && bankNormalLimit.getDayCountLimit() - curRepaymentNumber>0){
					Integer count = (bankNormalLimit.getDayCountLimit().intValue() - curRepaymentNumber);
					errCount.put(bankNormalLimit.getFinancingChannel(),count);
				}
			}
			if (bankNormalLimit.getDayAmountLimit() != -1 && StringOperator.add(originAmount, totalAmountToo) > bankNormalLimit.getDayAmountLimit()) {
				message += loanNo+"合同号"+bankNormalLimit.getChannelName()+"渠道当前扣款金额 + 当天已需要扣款总额 > 该渠道当天扣款限制;";
				Double sumCount = StringOperator.add(originAmount, totalAmountToo);
				log.info(message+"已扣总额:"+sumCount);
			}
		}
		if (bankNormalLimits == null ||bankNormalLimits.size() == 0) {
			formation.setCode("2");
			formation.setMessage("该合同号已在所有渠道对应的银行扣款金额达到最大限制！");
			return null;
		}
		Double tmpOriginAmoumnt = originAmount;
		Map<String, List<Double>> mapAmount = new LinkedHashMap<>(); //当前渠道拆分金额
		for (BankNormalLimit bankNormalLimit : bankNormalLimits){
			List<Double> cutAmountList  = new ArrayList<>();//拆分条数金额
			Double deductableAmount = bankNormalLimit.getDayAmountLimit();//当天可扣金额
			Double singlePayLimit = bankNormalLimit.getSingleAmountLimit();//单次可扣金额
			Integer dayCountlimit = bankNormalLimit.getDayCountLimit().intValue();//次数
			String channelCode = bankNormalLimit.getFinancingChannel();//扣款渠道
			if(tmpOriginAmoumnt <= 0.0){
				break;
			}
			Integer count = errCount.get(channelCode);//还能扣款次数
			if(tmpOriginAmoumnt>deductableAmount){//原始金额大于可扣总额
				if(deductableAmount>=singlePayLimit){//可扣总额大于单次扣款
					Double realAmount = AddCutAmountList(cutAmountList, deductableAmount, singlePayLimit,count);
					tmpOriginAmoumnt =  ArithDoubleUtils.sub(tmpOriginAmoumnt,realAmount);
				}else{
					cutAmountList.add(tmpOriginAmoumnt);
					mapAmount.put(channelCode,cutAmountList);
					break;
				}
			}else{//原始金额小于或等于可扣总额渠道
				if(tmpOriginAmoumnt>singlePayLimit){
					AddCutAmountList(cutAmountList, tmpOriginAmoumnt, singlePayLimit,count);
					mapAmount.put(channelCode,cutAmountList);
					break;
				}else{
					cutAmountList.add(tmpOriginAmoumnt);
					mapAmount.put(channelCode,cutAmountList);
					break;
				}
			}
			mapAmount.put(channelCode,cutAmountList);
		}
		log.info("最新拆分方法**********"+mapAmount.toString());
		return mapAmount;
	}

	/**
	 * 进行金额拆分。以最大限制与单次限制进行拆分
	 * @param cutAmount
	 * @param deductableAmount
	 * @param singlePayLimit
	 * @param errCount
	 * @return
	 */
	private Double AddCutAmountList( List<Double> cutAmount, Double deductableAmount, Double singlePayLimit,Integer errCount) {
		Double realdouble = 0.0;//真实已分批扣款总合
		int countIndex = (int)Math.ceil(deductableAmount / singlePayLimit);
		//取余数
		Double lastAmount = ArithDoubleUtils.remainder(deductableAmount,singlePayLimit);
		for (int i=0;i<countIndex;i++) {
			if(errCount != null && errCount != 0 && i == errCount){//失败次数
				break;
			}
            if(i+1 == countIndex && lastAmount != null && lastAmount != 0.0 && lastAmount > 0) {//最后一次 使用剩余余额
				realdouble = ArithDoubleUtils.add(realdouble,lastAmount);
				cutAmount.add(lastAmount);
			}
            else{
				realdouble = ArithDoubleUtils.add(realdouble,singlePayLimit);
				cutAmount.add(singlePayLimit);
			}
        }
        return realdouble;
	}

	/**
	 * 处理提示信息结果显示
	 */
	private class MessageTitle {
		private List<ChargeRecordDetail> list;
		private String valmessage;//消息体
		private String code;      //消息码

		public MessageTitle(List<ChargeRecordDetail> list) {
			this.list = list;
		}

		public String getValmessage() {
			return valmessage;
		}

		public String getCode() {
			return code;
		}

		public MessageTitle invoke() {
			Double amount = 0.0;
			String errmessage = "";
			int index = 0;
			Integer codeval [] = new Integer[list.size()];
			for(ChargeRecordDetail crd :list){
                index ++;
                if(crd.getCharge_status().equals("0")){
                    codeval[index-1]= 1;
                    errmessage += "["+index+"]"+"扣款中,";
                }else if(crd.getCharge_status().equals("1")){
                    amount = ArithDoubleUtils.add(Double.parseDouble(crd.getAmount()),amount);
                    codeval[index-1]= 2;
                    errmessage += "["+index+"]"+"扣款成功!已扣款金额"+crd.getAmount()+",";
                }else{//crd.getCharge_status().equals("2");
                    codeval[index-1]= 3;
                    if(StringUtil.isNotEmpty(crd.getCharge_message())){
                        errmessage +=  "["+index+"]"+crd.getCharge_message()+",";
                    }else{
                        errmessage +=  "["+index+"]"+"扣款失败,";
                    }
                }
            }
            //如果有扣款中的数据  标识为扣款中
			if(SinglePayRoundServiceImpl.useArraysBinarySearch(codeval,1)){
                code = "0";
                errmessage ="扣款中,";
            }else if(SinglePayRoundServiceImpl.useArraysBinarySearch(codeval,3)){//如果有失败的数据  标识为扣款失败
				code = "2";
            }else{
				code = "1";
				errmessage="扣款成功,";
            }
			valmessage = errmessage.substring(0,errmessage.length()-1);
			return this;
		}
	}
}
