package com.zhph.utils;


import java.math.BigDecimal;

/**
 *
 * Author: Zou Yao
 * Description: (浮点数精确运算工具类)
 * Time: 2017/7/24 13:27
 *
**/
public class AccurateCalculation {



    /**
     *
     * Author: zou yao
     * Description: {两个浮点数字符串相乘}
     * Date: 2017/7/24 13:34
     *
    **/
    public static Double  mul(String v1 , String v2) throws Exception{

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);

        return b1.multiply(b2).doubleValue();
    }



    /**
     *
     * Author: zou yao
     * Description: {两个浮点数相乘}
     * Date: 2017/7/24 13:36
     *
    **/
    public static Double mul(double v1 , double v2) throws Exception{
        return  mul(Double.toString(v1) , Double.toString(v2));
    }



    /**
     *
     * Author: zou yao
     * Description: {乘法}
     * Date: 2017/7/24 13:48
     *
    **/
    public static Double mul(String v1 , double v2) throws Exception{
        return  mul(v1 , Double.toString(v2));
    }


    /**
     *
     * Author: zou yao
     * Description: {浮点数相加}
     * Date: 2017/7/24 13:39
     *
    **/
    public static Double add(String v1,String v2){

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);

        return b1.add(b2).doubleValue();
    }


    /**
     *
     * Author: zou yao
     * Description: {描述}
     * Date: 2017/7/24 13:40
     *
    **/
    public static Double add(double v1,double v2){
        return add(Double.toString(v1) , Double.toString(v2));
    }


    /**
     *
     * Author: zou yao
     * Description: {浮点数相减}
     * Date: 2017/7/24 13:39
     *
    **/
    public static Double sub(String v1,String v2){

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);

        return b1.subtract(b2).doubleValue();
    }


    /**
     *
     * Author: zou yao
     * Description: {减法}
     * Date: 2017/7/24 13:42
     *
    **/
    public static Double sub(double v1,double v2){

        return sub(Double.toString(v1) , Double.toString(v2));
    }

    /**
     *
     * Author: zou yao
     * Description: {浮点数相除}
     * Date: 2017/7/24 13:39
     *
    **/
    public static Double div(String v1,String v2,int scale){

        if(scale<0){

            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);

        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     *
     * Author: zou yao
     * Description: {除法}
     * Date: 2017/7/24 13:42
     *
    **/
    public static Double div(double v1,double v2,int scale){
        return div(Double.toString(v1) , Double.toString(v2) ,scale);
    }

}
