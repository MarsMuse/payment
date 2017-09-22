
package com.zhph.channel.baofu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;


/**
 * 
 * MAP对象转XML
 * 识别数据类型，String、Map、ArrayList、其他数据类型执异常;
 * @author 宝付
 *
 */

public class MapToXml{	
	public static String Coverter(Map<Object,Object> Param,String Rootstr) throws Exception{
		StringBuilder XMLStr = new StringBuilder();
		XMLStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		XMLStr.append("<"+Rootstr+">");
		for (Object key : Param.keySet()){	
			XMLStr.append("<").append(key.toString()).append(">");
			XMLStr.append(covertStr((Object) Param.get(key)));			
			XMLStr.append("</"+key.toString()+">");
		}
		XMLStr.append("</"+Rootstr+">");
		return XMLStr.toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static String covertStr(Object Param) throws Exception{
		StringBuilder XMLStr = new StringBuilder();
		if(Param == null){
			return "";
		}
		else if(Param.getClass() ==  String.class){			
			return Param.toString();
		}else if(Param.getClass() == ArrayList.class){
			List<Object> MapList= (List<Object>) Param;	
			Iterator ItList = MapList.iterator();
			while(ItList.hasNext()){
				XMLStr.append(covertStr((Object) ItList.next()));
			}
		}else if(Param.getClass() == HashMap.class){
			Map MapTemp = (HashMap) Param;			 
			 for (Object key : MapTemp.keySet()){
				 XMLStr.append("<").append(key.toString()).append(">");
				 XMLStr.append(covertStr(MapTemp.get(key)));
				 XMLStr.append("</").append(key.toString()).append(">");
			 }			 
		}else{			
			throw new Exception("不能识别的数据类型：" +( Param.getClass()==String[].class));
		}		
		return XMLStr.toString();
	}
	
	 public static String jsontoXml(String json){  
	        try {  
	            XMLSerializer serializer = new XMLSerializer();  
	            JSON jsonObject = JSONSerializer.toJSON(json);  
	            return serializer.write(jsonObject);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return null;  
	    }  
	
	
	
	
}