package com.zhph.payment.proxy.paylogger.impl;

import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.StringUtils;
import com.zhph.api.entity.ConstantPay;
import com.zhph.base.common.DateUtil;
import com.zhph.base.common.StringUtil;
import com.zhph.entity.BatchPaymentResult;
import com.zhph.entity.ParamsEntity;
import com.zhph.entity.PaymentResult;
import com.zhph.entity.SinglePaymentResult;
import com.zhph.payment.charge.dao.BatchInterfaceLogMapper;
import com.zhph.payment.charge.dao.PaymentRecordMapper;
import com.zhph.payment.charge.dao.SingleChargeDao;
import com.zhph.payment.charge.dao.SingleInterfaceLogMapper;
import com.zhph.payment.charge.entity.BatchInterfaceLog;
import com.zhph.payment.charge.entity.ChargeDetailInfo;
import com.zhph.payment.charge.entity.PaymentRecord;
import com.zhph.payment.charge.entity.SingleInterfaceLog;
import com.zhph.payment.proxy.PayLogAspect;
import com.zhph.payment.proxy.paylogger.PayLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * 日志处理  主要功能 ：1.请求报文插入数据 
 * 			 <p/> 2.请求返回报文插入数据库
 *  		 <p/>  切面方法 请求方法后调用的接口
 * @author likang
 * 
 */
@Service
public class PayLogServiceImpl implements PayLogService {

	private final Logger logger = LoggerFactory.getLogger(PayLogAspect.class);
	@Resource
	private SingleInterfaceLogMapper singleInterfaceLogMapper;
	@Resource
	private PaymentRecordMapper paymentRecordMapper;
	@Resource
	private SingleChargeDao singleChargeDao;
	@Resource
	private BatchInterfaceLogMapper batchInterfaceLogMapper;


	@Override
	public void SingleRequestPayHandllog(ChargeDetailInfo values, PaymentResult result, String className)  {
		logger.info("start::" + className + "扣款发送报文记录日志开始");
		ParamsEntity params = (ParamsEntity) values;
		try {
			if (params.getChargeType() == ConstantPay.SINGLESTATE) {// 单扣
				insertLogMessage(params, result, ConstantPay.REQ_NO);
			} else if (params.getChargeType() == ConstantPay.SINGLESTATE_QUERY|| params.getChargeType() == ConstantPay.BATCHSTATE_QUERY) {// 单扣查询
				insertLogMessage(params, result, ConstantPay.REQ_SELECT_NO);
			}else if(params.getChargeType() == ConstantPay.BATCHSTATE && StringUtil.isNotEmpty(params.getCutNo()) ){//添加批扣循环单扣
				insertLogMessage(params, result, ConstantPay.REQ_NO);
			}
		} catch (Exception e) {
			logger.error("操作数据库错误："+e.getMessage());
			e.printStackTrace();
		}
		logger.info("end:: " + className + "扣款发送报文记录日志结束");
	}
	@Override
	public void SingleResponsePayHandlog(ChargeDetailInfo values, PaymentResult prResult, String className)  {
		ParamsEntity params = (ParamsEntity) values;
		logger.info("start::" + className + "扣款接收报文记录日志开始");
		try {
			UpdateBusinessServiceState(prResult.getSinglePaymentResult(),params);
			if (params.getChargeType() == ConstantPay.SINGLESTATE	|| params.getChargeType() == ConstantPay.BATCHSTATE) {
				insertLogMessage(params, prResult, ConstantPay.RESP_NO);
				insertRecord(params, prResult);// 扣款状态
			} else if (params.getChargeType() == ConstantPay.SINGLESTATE_QUERY) {// 单扣查询
				insertLogMessage(params, prResult,ConstantPay.RESP_SELECT_NO);
				// doto  更新业务库  如果查询操作返回数据 是成功则更新数据 库，如果失败不做任何操作
				params.setCutNo(null);
				updateChargeRecordDetial(params, prResult);//更新业务库表
				updateRecord(params, prResult);//更新扣款记录表
			}
		} catch (Exception e) {
			logger.error("操作数据库错误："+e.getMessage());
			e.printStackTrace();
		}
		logger.info("end:: " + className + "扣款接收报文记录日志结束");
	}

	/**
	 * 更新业务库
	 *  如果查询页面后，返回成功结果。得更新业务库状态。把业务库状态 ‘改为成功1’ 消息改为成功
	 *  只有成功状态才处理结果
	 * @param params
	 * @param prResult
	 */
	private void updateChargeRecordDetial(ParamsEntity params, PaymentResult prResult) throws Exception {
		if(prResult.getSinglePaymentResult().getPaymentCode().equals("1")){
			String chagerNo = "";
			if(StringUtil.isNotEmpty(params.getCutNo()))
				chagerNo = params.getCutNo();
			else
				chagerNo = params.getChargeNo();
			logger.info("更新业务库状态:交易号为："+chagerNo);
			singleChargeDao.updateLastState(chagerNo);
		}
	}


	@Override
	public void updateReqChargeRecordInfo(String charge_status, String charge_messgae, String update_flag, String single_no, String amount, String send_flag) {
		try {
			int count = singleChargeDao.updateReqChargeRecordInfo(charge_status, charge_messgae,update_flag, single_no,amount,send_flag);
			logger.info("更新数据条数："+count);
		} catch (Exception e){
			logger.error("错误状态"+e.getMessage());
		}
	}

	/**
	 * 更新业务库状态
	 * @author likang
	 * @date 2017-8-1下午3:33:00
	 */
	private void UpdateBusinessServiceState(SinglePaymentResult singleState,ParamsEntity reqDataParams) throws Exception {
		logger.info("更新业务表数据状态开始-交易号为：" + reqDataParams.getChargeNo() + "after::::扣款结果为："	+ singleState.getPaymentCode());
			if (singleState.getPaymentCode().equals("1")) {// 扣款成功
				String  paymentAmount = "";
				//如果返回结果里面有金额直接存放第三方返回结果金额//单扣查询
				if( null != singleState  && reqDataParams.getChargeType() ==  ConstantPay.SINGLESTATE_QUERY ){
						paymentAmount = singleState.getPaymentAmount()+"";//第三方返回结果金额
				}else{
						paymentAmount = reqDataParams.getAmount();
				}
				singleChargeDao.updateReqChargeRecordInfo("1",singleState.getPaymentDesc(), "1", reqDataParams.getChargeNo(),paymentAmount, "1");
			} else if (singleState.getPaymentCode().equals("2")) {// 扣款中
				logger.error("暂时不操作扣款中....的数据,交易号{}",reqDataParams.getChargeNo());
				singleChargeDao.updateReqChargeRecordInfo("0",	singleState.getPaymentDesc(), "0", reqDataParams.getChargeNo(), "0","1");
			} else if (singleState.getPaymentCode().equals("3")) {// 失败
				singleChargeDao.updateReqChargeRecordInfo("2",	singleState.getPaymentDesc(), "1", reqDataParams.getChargeNo(), "0","1");
			} else {
				singleChargeDao.updateReqChargeRecordInfo("2",singleState.getPaymentDesc(), "1", reqDataParams.getChargeNo(), "0","1");
			}
		logger.info("更新业务表数据状态结束-交易号为：" + reqDataParams.getChargeNo() + "after::::扣款结果为："+ singleState.getPaymentCode());
	}

	@Override
	public void BatchResponsePayHandlog(Object params, PaymentResult prResult, String batchNo, String className, int req_state) {
		String  reqContent = prResult.getReqContent();
		String  respContent = prResult.getRespContent();
		BatchPaymentResult batchContent = prResult.getBatchPaymentResult();
		BatchInterfaceLog batchlog   = new BatchInterfaceLog();
		batchlog.setBatchNo(batchNo);
		batchlog.setCreateTime(DateUtil.getNowDate());
		try {
			if(!StringUtils.isNullOrEmpty(batchContent.getPaymentChannel())){
                batchlog.setPaymentChannel(batchContent.getPaymentChannel());
            }
			if(!StringUtils.isNullOrEmpty(prResult.getPlatformCode()))
                batchlog.setTerminalId(prResult.getPlatformCode());
			else
				batchlog.setTerminalId("0");
			if(ConstantPay.REQUEST == req_state || ConstantPay.BATCHSTATE_REQ_QUERY == req_state ){
                batchlog.setRequestNo(req_state+"");
                batchlog.setInterfaceContent(reqContent);
            }else if(ConstantPay.RESPONSE == req_state || ConstantPay.BATCHSTATE_RESP_QUERY == req_state ){
                batchlog.setRequestNo(req_state+"");
                batchlog.setInterfaceContent(respContent);
            }
			batchInterfaceLogMapper.insert(batchlog);
		} catch (Exception e) {
			logger.error("{}批扣号操作数据库错误：{}",batchNo,e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	public void updatePaylog(ChargeDetailInfo params, PaymentResult result,	String className) {
		try {
			SingleInterfaceLog singlelog = new SingleInterfaceLog();
			singlelog.setTransNo(params.getChargeNo());
			singlelog.setInterfaceContent(result.getReqContent());
			singleInterfaceLogMapper.updateByPrimaryKey(singlelog);
		} catch (Exception e) {
			logger.error("操作数据库错误："+e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	public void ExceptionPaylog(ChargeDetailInfo params, Throwable e,	String className)  {
		if(!StringUtils.isNullOrEmpty(params.getChargeNo()))
		logger.error("联系程序员！请求类："+className+"请求合同号："+params.getChargeNo()+"第三方异常",e);
		else
		logger.error("联系程序员！请求类："+className+"第三方异常",e);
	}

	/**
	 * 更新 zh_payment_record)记录表
	 * @param params
	 * @param result
	 * @throws ParseException
	 */
	private void updateRecord(ParamsEntity params, PaymentResult result)		throws ParseException{
		SinglePaymentResult singleState =  result.getSinglePaymentResult();
		if (singleState.getPaymentCode().equals("1")) {// 扣款成功
			PaymentRecord record = new PaymentRecord();
			record.setTransNo(params.getChargeNo());
			if(StringUtil.isEmpty(params.getAmount())){//查询返回结果可能为空
				record.setTransAmount(singleState.getPaymentAmount());
				record.setPaymentDesc(singleState.getPaymentDesc());
			}else{
				record.setTransAmount(Double.valueOf(params.getAmount()));
			}
			record.setPaymentCode(1+"");
			record.setPaymentChannel(params.getPaymentChannel());
			record.setLoanContractNo(params.getLoanNo());
			record.setBankCode(params.getBankCode());
			if(StringUtil.isEmpty(params.getPlatformCode())){
				record.setTerminalId("0");//未知存放平台  此字段多余
			}else{
				record.setTerminalId(params.getPlatformCode());
			}
			paymentRecordMapper.insertByQueryState(record);
		}else if(singleState.getPaymentCode().equals("3")){
			logger.info("暂时不插入数据。查询成功。但是返回结果是失败:"+singleState.getPaymentDesc());
		}
	}
	/**
	 * 插入(扣款：zh_payment_record)记录表
	 * @author likang
	 * @date 2017-7-20下午2:03:28
	 */
	private void insertRecord(ParamsEntity params, PaymentResult result)		throws ParseException {
		PaymentRecord singlePaymentRecord = new PaymentRecord();
		SinglePaymentResult singlePaymentResult = result.getSinglePaymentResult();
		singlePaymentRecord.setTransNo(params.getChargeNo());
		if (result.getBfResult() != null) {//如果没有
			singlePaymentRecord.setThirdTransNo(result.getBfResult().getOrderSeq());
		}
		if(params.getCutNo() != null)
			singlePaymentRecord.setThirdBatchNo(params.getCutNo());
		if(StringUtil.isNotEmpty(params.getPlatformCode())){
			singlePaymentRecord.setTerminalId(params.getPlatformCode());
		}else{
			singlePaymentRecord.setTerminalId("0");
		}
		singlePaymentRecord.setPaymentChannel(params.getPaymentChannel());
		singlePaymentRecord.setBankCard(params.getAccountNumber());
		singlePaymentRecord.setBankCode(params.getBankKey());
		singlePaymentRecord.setCreateTime(DateUtil.getNowDate());
		singlePaymentRecord.setLoanContractNo(params.getLoanNo());
		singlePaymentRecord.setName(params.getLoanName());
		singlePaymentRecord.setPaymentCode(singlePaymentResult.getPaymentCode());
		singlePaymentRecord.setTransAmount(Double.valueOf(params.getAmount()));
		singlePaymentRecord.setPaymentDesc(singlePaymentResult.getPaymentDesc());
		singlePaymentRecord.setPaymentTime(DateUtil.stringToDate(singlePaymentResult.getPaymentTime(), "yyyy-MM-dd HH:mm:ss"));
		singlePaymentRecord.setTransType(params.getChargeType() + "");
		paymentRecordMapper.insert(singlePaymentRecord);// 插入扣款记录
		logger.info("单扣（扣款记录）表" + JSON.toJSON(singlePaymentRecord));
	}
	/**
	 * 插入单扣表
	 * 
	 * @author likang
	 * @date 2017-7-20下午1:41:48
	 * @param params
	 *            请求参数
	 * @param result
	 *            返回结果
	 * @param request_no
	 *            请求编号(1:扣款发送报文;2:扣款接收报文;3:查询发送报文;4:查询接收报文)
	 */
	private void  insertLogMessage(ParamsEntity params, PaymentResult result,int request_no) throws Exception {
		// 保存单扣返回报文
		SingleInterfaceLog singlelog = new SingleInterfaceLog();
		singlelog.setCreateTime(DateUtil.getNowDate());
		singlelog.setRequestNo(String.valueOf(request_no));
		singlelog.setLoanContractNo(params.getLoanNo());
		singlelog.setTransNo(params.getChargeNo());
		singlelog.setCharge_type(params.getChargeType() + "");
		singlelog.setPaymentChannel(params.getPaymentChannel());
		if(params.getChargeType() == ConstantPay.BATCHSTATE){//批扣 循环单扣操作
			singlelog.setIs_loop(ConstantPay.BATCHSTATE+"");
		}
		if(StringUtil.isEmpty(params.getPlatformCode())){
			singlelog.setTerminalId("0");//此字段无用。。 此标识为未知
		}else{
			singlelog.setTerminalId(params.getPlatformCode());
		}
		if (result != null) {// 如果不为空
			if (request_no == ConstantPay.REQ_NO	|| request_no == ConstantPay.REQ_SELECT_NO)// 发送状态
				singlelog.setInterfaceContent(result.getReqContent());
			else if (request_no == ConstantPay.RESP_NO|| request_no == ConstantPay.RESP_SELECT_NO)// 接收状态
				singlelog.setInterfaceContent(result.getRespContent());
		} else {//特别返回值处理
			singlelog.setInterfaceContent(params.getOtherObj() != null ? params	.getOtherObj().toString() : "");
		}
		singleInterfaceLogMapper.insert(singlelog);
		logger.info("单扣(日志记录)表、'成功'日志记录：" + JSON.toJSON(singlelog));
	}

}
