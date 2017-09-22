package com.zhph.entity;


public class PaymentResult  {
    private String reqContent; //请求报文
    private String respContent; //响应报文
    private String platformCode;//平台Id
    private SinglePaymentResult singlePaymentResult; //单扣结果
    private BatchPaymentResult batchPaymentResult; //批扣结果
    private BaseResult bfResult;
    private Object resultBySelf; //自定义结果集（用于传值）


    
    
    
    public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getReqContent() {
        return reqContent;
    }

    public void setReqContent(String reqContent) {
        this.reqContent = reqContent;
    }

    public String getRespContent() {
        return respContent;
    }

    public void setRespContent(String respContent) {
        this.respContent = respContent;
    }

    public SinglePaymentResult getSinglePaymentResult() {
        if(singlePaymentResult == null)
            singlePaymentResult = new SinglePaymentResult();
        return singlePaymentResult;
    }

    public void setSinglePaymentResult(SinglePaymentResult singlePaymentResult) {
        this.singlePaymentResult = singlePaymentResult;
    }

    public BatchPaymentResult getBatchPaymentResult() {
        if(batchPaymentResult == null){
            batchPaymentResult = new BatchPaymentResult();
        }
        return batchPaymentResult;
    }

    public void setBatchPaymentResult(BatchPaymentResult batchPaymentResult) {
        this.batchPaymentResult = batchPaymentResult;
    }

    public BaseResult getBfResult() {
        if(bfResult == null){
            bfResult = new BaseResult();
        }
        return bfResult;
    }

    public void setBfResult(BaseResult bfResult) {
        this.bfResult = bfResult;
    }

    public Object getResultBySelf() {
        return resultBySelf;
    }

    public void setResultBySelf(Object resultBySelf) {
        this.resultBySelf = resultBySelf;
    }
}
