package com.zhph.base.common;

/**
 * @Author:DaiLiang
 * @Description:
 * @Create: 2017-01-16 13:26
 */
public class ObjectUtil {
    /**
     * 去掉double 的空值，若为空返回 0,否则返回原值
     *
     * @param obj
     * @return
     */
    public static Double clearDoubleNull(Object obj) {
        return obj == null ? 0 : Double.parseDouble(obj.toString());
    }

    /**
     * 去掉String 的空值，若为空返回"",否则返回原值
     *
     * @param obj
     * @return
     */
    public static String clearStringNull(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * 去掉Long 的空值，若为空返回0，否则返回原值
     *
     * @param obj
     * @return
     */
    public static Long clearLongNull(Object obj) {
        return obj == null ? 0l : Long.parseLong(obj.toString());
    }

    /**
     * 去掉double 的空值，若为空返回 0,否则返回原值
     * @param obj
     * @return
     */
    public static Double clearNull(Double obj) {
        return obj == null ? 0 : obj;
    }
}
