package com.zhph.base.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by DL on 2017/2/5.
 */
public class RequestUtil {



    /**
     *
     * @Title: getParameter
     * @Description: 将request获取的表单参数放入map
     * @param @param request
     * @param @return
     * @return Map<String,Object>
     * @author DL
     * @date 2017-2-5
     * @throws
     */
    public static Map<String, Object> getParameter(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<String, Object>();
        Iterator<Map.Entry<String, String[]>> iter = request.getParameterMap().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String[]> entry = iter.next();
            String key = entry.getKey();
            String[] val = entry.getValue();
            for(int i = 0;i < val.length;i++){
                if (val[i] != null){
                    val[i] = val[i].trim();
                }
            }
            if (val.length == 1) {
                param.put(key, val[0]);
            } else if (val.length > 1) {
                param.put(key, val);
            } else {
                param.put(key, null);
            }
        }
        return param;
    }
}
