package com.zhph.payment.charge.factory;

import com.zhph.base.common.ApplicationContextUtils;
import com.zhph.payment.charge.service.business.PaymentService;

public class ChannelFactoryUtils {
//	@Autowired
//	private PaymentRecordMapper paymentRecordMapper;
//	@Autowired
//	private BankNormalLimitMapper bankNormalLimitMapper; 
	
	public static PaymentService createOperatePayment(String channelId) {
		PaymentService payment = null;
		switch (channelId) {
		case "C007":
			payment =  (PaymentService) ApplicationContextUtils.getBean("baofooPaymentService");
			break;
		case "C001":
			payment = (PaymentService) ApplicationContextUtils.getBean("zjPaymentService");
			break;
		case "C006":
			payment = (PaymentService) ApplicationContextUtils.getBean("chinagPayPaymentService");
			break;
		default:
			payment = (PaymentService) ApplicationContextUtils.getBean("baofooPaymentService");
			break;
		}
		return payment;
	}
}
