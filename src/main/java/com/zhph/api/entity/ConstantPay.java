package com.zhph.api.entity;

/**
 * 常量配置
 * 
 * @author likang
 * 
 */
public final class ConstantPay {
	/**
	 * 标识-单扣 
	 */
	public final static  int SINGLESTATE = 1;
	/**
	 * 标识-批扣
	 */
	public final static  int BATCHSTATE = 2;
	/**
	 * 标识-单扣查询
	 */
	public final static  int SINGLESTATE_QUERY = 3;
	/**
	 * 标识-批扣查询
	 */
	public final static  int BATCHSTATE_QUERY = 4;
	/**
	 * 批扣查询请求报文
	 */
	public final static int BATCHSTATE_REQ_QUERY=3;
	/**
	 * 批扣查询接收报文
	 */
	public final static int BATCHSTATE_RESP_QUERY=4;
	
	/**
	 * 请求编号 单扣：发送报文存入数据库状态    
	 * 1:扣款发送报文
	 */
	public final static int REQ_NO = 1;
	/**
	 * 请求编号 单扣：接收报文存入数据库状态    
	 * 2:扣款接收报文
	 */
	public final static int RESP_NO = 2;
	/**
	 * 请求编号 单扣-查询：发送存入数据库状态  
	 * 3:查询发送报文
	 */
	public final static int REQ_SELECT_NO = 3;
	/**
	 * 请求编号 单扣-查询：接收存入数据库状态  
	 * 4:查询接收报文
	 */
	public final static int RESP_SELECT_NO = 4;
	
	
	/**
	 * 宝付编码code
	 */
	public final static String BAOFOO_CODE = "C007";
	/**
	 * 爱农
	 */
	public final static String CHINAG_CODE= "C006";
	/**
	 * 中金
	 */
	public final static String ZJ_CODE= "C001";

	public final static String ZJ_NOT_MESSAGE = "未知错误，详情请咨询中金渠道";

	/**
	 * 标识为：请求报文 
	 */
	public final static int REQUEST = 1;
	/**
	 * 标识为：接收报文 
	 */
	public final static int RESPONSE = 2;
	/**
	 * 正合标识
	 */
	public final static String ZH_COMPANY = "ZH";
	/**
	 * 鸿特标识
	 */
	public final static String HT_COMPANY = "HT";

	/**
	 * 正式环境目录
	 */
	public static final String COMPANY_PRO = "PRO";

	/**
	 * 测试环境目录
	 */
	public static final String COMPANY_DEV = "DEV";
}
