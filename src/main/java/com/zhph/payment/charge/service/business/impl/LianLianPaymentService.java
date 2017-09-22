package com.zhph.payment.charge.service.business.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.zhph.payment.charge.entity.ChargeDetailInfo;
import com.zhph.payment.charge.service.business.PaymentService;

/**
 * 连连扣款逻辑层
 * 
 * @author zhph
 */
@Service
public class LianLianPaymentService implements PaymentService {

	@Override
	public void singlePayment(ChargeDetailInfo chargeDetailInfo)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void singlePaymentQuery(ChargeDetailInfo chargeDetailInfo)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batchPayment(List<ChargeDetailInfo> chargeDetailInfoList,
			String batchNo,String platformCode ,String mainBody) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batchPaymentQuery(String batchNo,String mainBody) throws Exception {
		// TODO Auto-generated method stub
		
	}

	 
 
}
