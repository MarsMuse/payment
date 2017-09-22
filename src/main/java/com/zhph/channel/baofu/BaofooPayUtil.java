package com.zhph.channel.baofu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.zhph.api.entity.ConstantPay;
import com.zhph.base.common.ApplicationContextUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
/**
 * 宝付第三方信息帮助类
 * @author likang
 *
 */
@Component
public  class BaofooPayUtil implements Serializable{
	private static final long serialVersionUID = 1L;

////获取当前环境宝付配置,dev为测试环境,pro为线上环境
	@Value("#{commonInfo}")
	private Properties commonInfo;
	//单扣测试环境配置
	@Value("#{baofooKeyInfoPro}")
	private Properties baofooKeyInfoPro;
	//单扣正式环境配置
	@Value("#{baofooKeyInfoDev}")
	private Properties baofooKeyInfoDev;
	//批扣测试环境配置
	@Value("#{baofoobatchKeyInfoPro}")
	private Properties baofoobatchKeyInfoPro;
	//批扣正式环境配置
	@Value("#{baofoobatchKeyInfoDev}")
	private Properties baofoobatchKeyInfoDev;
	/**
	 * 取单扣配置
	 * 优化目标。如果选择了其中一个 可以直接把别一个从内存中移除
	 * @return
	 */
	public Properties getSingleProperties(){
		String path = (String) commonInfo.get("BF.KEY.PATH");
		if(path.trim().equalsIgnoreCase(ConstantPay.COMPANY_PRO)){
			return this.baofooKeyInfoPro;
		}else{
			return this.baofooKeyInfoDev;
		}
	}

	/**
	 * 取批扣配置
	 * @return
	 */
	public Properties getBatchProperties(){
		String path = (String) commonInfo.get("BF.KEY.PATH");
		if(path.trim().equalsIgnoreCase(ConstantPay.COMPANY_PRO)){
			return this.baofoobatchKeyInfoPro;
		}else{
			return this.baofoobatchKeyInfoDev;
		}
	}
	
	/**
	 * 根据账户不同获取请求报文
	 * 
	 * @param mainBody 主体
	 * @param pro
	 * @param payType  扣款还是查询  txn_sub_type_pay 或者  txn_sub_type_query
	 * @return
	 */
	public static Map<String, String> getHeadPostParamByMainBody(String mainBody,Properties pro,String payType) {
		Map<String, String> HeadPostParam = new HashMap<String, String>();
		HeadPostParam.put("version",pro.getProperty("version").toString().trim());
		if (mainBody.equalsIgnoreCase(ConstantPay.ZH_COMPANY)) {
			HeadPostParam.put("member_id",pro.getProperty("member_id").toString().trim());
			HeadPostParam.put("terminal_id",pro.getProperty("terminal_id").toString().trim());
		}
		if (mainBody.equalsIgnoreCase(ConstantPay.HT_COMPANY)) {
			HeadPostParam.put("member_id",pro.getProperty("member_id_ht").toString().trim());
			HeadPostParam.put("terminal_id",pro.getProperty("terminal_id_ht").toString().trim());
		}
		HeadPostParam.put("txn_type",pro.getProperty("txn_type").toString().trim());
		
		HeadPostParam.put("txn_sub_type", pro.get(payType).toString().trim());
		HeadPostParam.put("data_type",pro.getProperty("data_type").toString().trim());

		return HeadPostParam;
	}


	/**
	 * 批扣根据账户不同获取请求报文
	 *
	 * @Title: getHeadPostParamByMainBodyBatch
	 * @Description:
	 * @param mainBody 主体
	 * @param  pro    请求实体
	 * @param payType 请求类型
	 * @return Map<String,String>
	 */
	public static Map<String, String> getHeadPostParamByMainBodyBatch(String mainBody, Properties pro,String payType) {
		Map<String, String> HeadPostParam = new HashMap<>();
		HeadPostParam.put("version", pro.getProperty("version_batch"));
		if (mainBody.equalsIgnoreCase(ConstantPay.ZH_COMPANY)) {
			HeadPostParam.put("terminal_id",  pro.getProperty("terminal_id"));
			HeadPostParam.put("member_id",  pro.getProperty("member_id"));
		}
		if (mainBody.equalsIgnoreCase(ConstantPay.HT_COMPANY)) {
			HeadPostParam.put("terminal_id",  pro.getProperty("terminal_id_ht"));
			HeadPostParam.put("member_id",  pro.getProperty("member_id_ht"));
		}
		HeadPostParam.put("txn_type",  pro.getProperty("txn_type_batch"));
		HeadPostParam.put("txn_sub_type", pro.getProperty(payType));
		HeadPostParam.put("data_type",  pro.getProperty("data_type"));
		return HeadPostParam;
	}

	public Properties getCommonInfo() {
		return commonInfo;
	}

	public void setCommonInfo(Properties commonInfo) {
		this.commonInfo = commonInfo;
	}

	public Properties getBaofooKeyInfoPro() {
		return baofooKeyInfoPro;
	}

	public void setBaofooKeyInfoPro(Properties baofooKeyInfoPro) {
		this.baofooKeyInfoPro = baofooKeyInfoPro;
	}

	public Properties getBaofooKeyInfoDev() {
		return baofooKeyInfoDev;
	}

	public void setBaofooKeyInfoDev(Properties baofooKeyInfoDev) {
		this.baofooKeyInfoDev = baofooKeyInfoDev;
	}
 
	

	 
	
	
}
