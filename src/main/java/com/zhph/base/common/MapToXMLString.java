package com.zhph.base.common;

import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Set;

import static com.baofoo.util.MapToXMLString.coverter;

/**
 * Created by zhph on 2017/2/6.
 */
public class MapToXMLString {
    public static String converter(Map<Object, Object> dataMap, String Rootstr)
    {
        synchronized (MapToXMLString.class)
        {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            strBuilder.append("<"+Rootstr+">");
            Set<Object> objSet = dataMap.keySet();
            for (Object key : objSet)
            {
                if (key == null)
                {
                    continue;
                }
                strBuilder.append("<").append(key.toString()).append(">");
                Object value = dataMap.get(key);
                strBuilder.append(coverter(value));
                strBuilder.append("</"+key.toString()+">");
            }
            strBuilder.append("</"+Rootstr+">");
            return strBuilder.toString();
        }
    }

    public static String converterStr(Map<String, String> dataMap,String Rootstr)
    {
        synchronized (MapToXMLString.class)
        {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            strBuilder.append("<"+Rootstr+">");
            Set<String> objSet = dataMap.keySet();
            for (String key : objSet)
            {
                if (StringUtils.isEmpty(key))
                {
                    continue;
                }
                strBuilder.append("<").append(key).append(">");
                Object value = dataMap.get(key);
                strBuilder.append(coverter(value));
                strBuilder.append("</"+key+">");
            }
            strBuilder.append("</"+Rootstr+">");
            return strBuilder.toString();
        }
    }
}
