package com.zhph.channel.baofu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JXMConvertUtilBatch {
	

	public static String XmlConvertJson(String XMLString) {	
        JSONObject xmlJSONObj = XML.toJSONObject(XMLString);
		String jsonPrettyPrintString = xmlJSONObj.toString(4);
		return jsonPrettyPrintString;
	}

	
	public static Map<String, Object> JsonConvertHashMap(Object object)  
	   {  
	       Map<String, Object> RMap = new HashMap<String, Object>();  
	       net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(object);   
	       RMap=IteratorHash(jsonObject);
	       return RMap;
	   } 


	public static Map<String,Object> IteratorHash(net.sf.json.JSONObject JsonToMap){
		Iterator<?> it = JsonToMap.keys();		
		HashMap<String, Object> RMap = new HashMap<String, Object>(); 
		
		while(it.hasNext()){
			String key = String.valueOf(it.next());
            if (JsonToMap.get(key).getClass() == net.sf.json.JSONArray.class) {
                if (JsonToMap.getJSONArray(key).isEmpty()) {
                    RMap.put(key, null);
				}else{
					
					List<Map<String,Object>> MapListObj=new ArrayList<Map<String,Object>>();
					for(Object JsonArray : JsonToMap.getJSONArray(key)){
						HashMap<String, Object> TempMap = new HashMap<String, Object>();
						if(JsonArray.getClass() == String.class){
							TempMap.put(key, JsonArray);
						}else{
							TempMap.putAll(IteratorHash(net.sf.json.JSONObject.fromObject(JsonArray)));
						}					
						MapListObj.add(TempMap);
					}					
					RMap.put(key, (Object) MapListObj);
				}
			}else if(JsonToMap.get(key).getClass() == net.sf.json.JSONObject.class){
				
				RMap.put(key,IteratorHash(JsonToMap.getJSONObject(key)));
				
			}else if(JsonToMap.get(key).getClass() == String.class){
				
				RMap.put(key, JsonToMap.get(key));
				
			}
		}
		
		return RMap;
	}

}