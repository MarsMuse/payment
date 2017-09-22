package com.zhph.base.common;

import java.io.File;
import java.math.BigDecimal;

/**
 * @Author:DaiLiang
 * @Description:
 * @Create: 2017-01-16 11:57
 */
public class StringOperator {
    /**

     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**

     * 提供精确的加法运算。
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1,double v2){

        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1,double v2){

        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.subtract(b2).doubleValue();
    }
    
    public static double div(double v1,double v2,int scale){

        if(scale<0){

            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }

        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    //删除目录下文件
    public static void deletePath(String filepath) throws Exception { 
    	File f = new File(filepath);//定义文件路径          
        if(f.exists() && f.isDirectory()){//判断是文件还是目录   
	       //若有则把文件放进数组，并判断是否有下级目录   
	       File delFile[]=f.listFiles();   
	       int i =f.listFiles().length;   
	       for(int j=0;j<i;j++){   
	           if(delFile[j].isDirectory()){   
	        	   deletePath(delFile[j].getAbsolutePath());//递归调用del方法并取得子目录路径   
	            }   
	            delFile[j].delete();//删除文件   
	       }
        }
    }
    //元转分
    public static long getDimeConvertPenny(double dime){
        double d = 0;
        if(dime!=0){
            String dimeStr=String.valueOf(dime);
            String[] dimeAry=dimeStr.split("\\.");
            int len=dimeAry.length;
            if(len==1){
                d=new Double(dimeStr+"00").doubleValue();
            }
            else{
                int dimePenny=new Integer(dimeAry[1]).intValue();
                if(dimePenny==0){
                    d=new Double(dimeAry[0]+"00").doubleValue();
                }
                else{
                    int length=dimeAry[1].length();
                    if(length==1){
                        d=new Double(dimeAry[0]+dimeAry[1]+"0").doubleValue();
                    }
                    else{
                        d=new Double(dimeAry[0]+dimeAry[1]).doubleValue();
                    }
                }
            }
        }
        return (long)d;
    }
}
