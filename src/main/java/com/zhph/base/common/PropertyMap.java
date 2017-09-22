package com.zhph.base.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 *
 * @Author: Zou Yao
 * @Description: (配置获取到)
 * @Time: 2017/7/17 17:18
 *
**/
public class PropertyMap extends PropertyPlaceholderConfigurer {
    //配置文件中的数据
    private Map<String , String> dataMap;
    /**
     *
     * @Author: zou yao
     * @Description: {加载配置文件时执行方法}
     * @Date: 2017/7/17 17:38
     * @Param: No such property: code for class: Script1
     *
    **/
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
    	super.processProperties(beanFactory , props);
    	//实例化数据Map
        dataMap  =  new HashMap<String , String>();
        for(Object key : props.keySet()){
            String keyName = (String) key;
            if(dataMap.containsKey(keyName)){
                throw new RuntimeException("配置文件中存在相同的KEY");
            }
            String value = props.getProperty(keyName);
            dataMap.put(keyName , value);
        }
    }



    /**
     *
     * @Author: zou yao
     * @Description: {获取到属性值}
     * @Date: 2017/7/17 17:45
     * @Param: No such property: code for class: Script1
     *
    **/
    public  String getValue(String key){
        String  result = null;
        if(dataMap != null && (!dataMap.isEmpty())){
            result = dataMap.get(key);
        }

        return  result;
    }
}
