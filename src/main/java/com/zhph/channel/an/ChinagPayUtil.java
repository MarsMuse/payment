package com.zhph.channel.an;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.jnewsdk.util.SignUtil;
import com.zhph.api.entity.ConstantPay;
import com.zhph.base.common.ApplicationContextUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChinagPayUtil implements Serializable {
	private static final long serialVersionUID = 1L;

	////获取当前环境宝付配置,dev为测试环境,pro为线上环境
	@Value("#{commonInfo}")
	private Properties commonInfo;
	@Value("#{chinagKeyInfoPro}")
	private  Properties propertiesPro;
	@Value("#{chinagKeyInfoDev}")
	private  Properties propertiesDev;
	private static final String URL_PARAM_CONNECT_FLAG = "&";

	/**
	 * 取单扣配置
	 * @return
	 */
	public Properties getSingleProperties(){
		String path = (String) commonInfo.get("AN.KEY.PATH");
		if(path.trim().equalsIgnoreCase(ConstantPay.COMPANY_PRO)){
			return this.propertiesPro;
		}else{
			return this.propertiesDev;
		}
	}



	/**
	 * 设置签名
	 *
	 * @param paramMap
	 *            参数
	 * @param key
	 *            秘钥
	 */
	public  void setSignature(Map<String, String> paramMap, String key) {
		String signMethod = paramMap.get("signMethod");
		Set<String> removeKey = new HashSet<String>();
		removeKey.add("signMethod");
		removeKey.add("signature");
		String signMsg = SignUtil.getSignMsg(paramMap, removeKey);
		String signature = SignUtil.sign(signMethod, signMsg, key, "UTF-8");
		paramMap.put("signature", signature);
	}


	/**
	 * 将map转化为形如key1=value1&key2=value2...
	 *
	 * @param map
	 *            请求参数
	 * @return 拼接后的请求参数
	 */
	public  String getWebForm(Map<String, String> map) {
		if (null == map || map.keySet().size() == 0) {
			return "";
		}
		StringBuilder url = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String value = entry.getValue();
			String str = (value != null ? value : "");
			try {
				str = URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			url.append(entry.getKey()).append("=").append(str).append(URL_PARAM_CONNECT_FLAG);
		}
		// 最后一个键值对后面的“&”需要去掉。
		String strURL = url.toString();
		if (URL_PARAM_CONNECT_FLAG.equals(""+ strURL.charAt(strURL.length() - 1))) {
			strURL = strURL.substring(0, strURL.length() - 1);
		}
		return (strURL);
	}

	public Properties getPropertiesPro() {
		return propertiesPro;
	}

	public void setPropertiesPro(Properties propertiesPro) {
		this.propertiesPro = propertiesPro;
	}

	public Properties getPropertiesDev() {
		return propertiesDev;
	}

	public void setPropertiesDev(Properties propertiesDev) {
		this.propertiesDev = propertiesDev;
	}
}
