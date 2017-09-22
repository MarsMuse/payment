package com.zhph.base.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * String 工具类
 * 
 * @author Even
 * @date 2016-06-23
 */
public class StringUtil extends StringUtils {
    
    public StringUtil() {
        
    }
    
    /**
     * <p>
     * 进行toString操作，如果传入的是null，返回指定的默认值
     * <p>
     * 
     * @param obj 源
     * @param nullStr 若obj为null时返回这个指定值
     * @return String
     */
    public static String toString(Object obj, String nullStr) {
        return obj == null ? nullStr : obj.toString();
    }
    
	/**
	 * <p>
     * 判断字符串是否为空:null 或者 "" 返回true
     * <p>
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		if (null == str || str.equals("")) {
			return true;
		}
		return false;
	}
    
    /**
     * <p>
     * 把String以一个char为分割点，拆分成String数组
     * <p>
     * 
     * @param str 源
     * @param chr 分割点字符
     * @return 拆分后的String数组
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String[] split(String str, char separatorChar) {
        if (str == null) {
            return null;
        }
        
        int length = str.length();
        
        if (length == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        
        List list = new ArrayList();
        int i = 0;
        int start = 0;
        boolean match = false;
        
        while (i < length) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                
                start = ++i;
                continue;
            }
            
            match = true;
            i++;
        }
        
        if (match) {
            list.add(str.substring(start, i));
        }
        
        return (String[])list.toArray(new String[list.size()]);
    }
    
    /**
     * <p>
     * 将字符串按空白字符分割
     * </p>
     * 
     * @param str 要分割的字符串
     * @return 分割后的字符串数组，如果原字符串为null，则返回null
     */
    public static String[] split(String str) {
        return split(str, null, -1);
    }
    
    /**
     * <p>
     * 字符串替换
     * <p>
     * 
     * @param from 字符串中要被替换的子串
     * @param to 替换子串的字符串
     * @param source 被操作的字符串
     * @return
     */
    public static String replaceAll(String source, String from, String to) {
        if (source == null || from == null || to == null) {
            return null;
        }
        StringBuffer bf = new StringBuffer();
        int index = -1;
        while ((index = source.indexOf(from)) != -1) {
            bf.append(source.substring(0, index) + to);
            source = source.substring(index + from.length());
            index = -1;
        }
        bf.append(source);
        return bf.toString();
    }
    
    /**
     * <p>
     * 除去字符串头尾部的指定字符，如果字符串是null，依然返回null
     * </p>
     * 
     * @param str 要处理的字符串
     * @param stripChars 要除去的字符，如果为null,表示除去空白字符
     * @param mode -1 表示trim头部，0表示trim前后，1 表示trim尾部
     *
     * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
     */
    public static String trim(String str, String stripChars, int mode) {
        if (str == null) {
            return null;
        }
        
        int length = str.length();
        int start = 0;
        int end = length;
        
        // 扫描字符串头部
        if (mode <= 0) {
            if (stripChars == null) {
                while ((start < end) && (Character.isWhitespace(str.charAt(start)))) {
                    start++;
                }
            } else if (stripChars.length() == 0) {
                return str;
            } else {
                while ((start < end) && (stripChars.indexOf(str.charAt(start)) != -1)) {
                    start++;
                }
            }
        }
        
        // 扫描字符串尾部
        if (mode >= 0) {
            if (stripChars == null) {
                while ((start < end) && (Character.isWhitespace(str.charAt(end - 1)))) {
                    end--;
                }
            } else if (stripChars.length() == 0) {
                return str;
            } else {
                while ((start < end) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
                    end--;
                }
            }
        }
        
        if ((start > 0) || (end < length)) {
            return str.substring(start, end);
        }
        
        return str;
    }
    
    /**
     * 取得两个分隔符之间的子串。
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>null</code>。 如果分隔子串为<code>null</code> ，则返回<code>null</code>。
     * </p>
     *
     * @param str 字符串
     * @param open 要搜索的分隔子串1
     * @param close 要搜索的分隔子串2
     * @return 子串，如果原始串为<code>null</code>或未找到分隔子串，则返回<code>null</code>
     */
    public static String substringBetween(String str, String open, String close) {
        return substringBetween(str, open, close, 0);
    }
    
    /**
     * 取得两个分隔符之间的子串。
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>null</code>。 如果分隔子串为<code>null</code> ，则返回<code>null</code>。
     * </p>
     *
     * @param str 字符串
     * @param open 要搜索的分隔子串1
     * @param close 要搜索的分隔子串2
     * @param fromIndex 从指定index处搜索
     * @return 子串，如果原始串为<code>null</code>或未找到分隔子串，则返回<code>null</code>
     */
    public static String substringBetween(String str, String open, String close, int fromIndex) {
        if ((str == null) || (open == null) || (close == null)) {
            return null;
        }
        
        int start = str.indexOf(open, fromIndex);
        
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        
        return null;
    }
    
    /**
     * <p>
     * 如果字符串是null或空字符串("")，则返回指定默认字符串，否则返回字符串本身
     * </p>
     *
     * @param str 要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String defaultIfBlank(String str, String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }
    
    /**
     * <p>
     * 将数组中的元素连接成一个字符串
     * </p>
     *
     * @param array 要连接的数组
     * @return 连接后的字符串，如果原数组为null，则返回null
     */
    public static String join(Object[] array) {
        return join(array, null);
    }
    
    /**
     * <p>
     * 将数组中的元素连接成一个字符串
     * </p>
     *
     * @param array 要连接的数组
     * @param separator 分隔符
     * @return 连接后的字符串，如果原数组为null，则返回null
     */
    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }
        
        int arraySize = array.length;
        int bufSize =
            (arraySize == 0) ? 0 : ((((array[0] == null) ? 16 : array[0].toString().length()) + 1) * arraySize);
        StringBuffer buf = new StringBuffer(bufSize);
        
        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        
        return buf.toString();
    }
    
    /**
     * <p>
     * 将指定字符串重复n遍
     * </p>
     * 
     * @param str 要重复的字符串
     * @param repeat 重复次数，如果小于0，则看作0
     * @return 重复n次的字符串，如果原始字符串为null，则返回null
     */
    public static String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        }
        
        if (repeat <= 0) {
            return "";
        }
        
        int inputLength = str.length();
        
        if ((repeat == 1) || (inputLength == 0)) {
            return str;
        }
        
        int outputLength = inputLength * repeat;
        
        switch (inputLength) {
            case 1:
                
                char ch = str.charAt(0);
                char[] output1 = new char[outputLength];
                
                for (int i = repeat - 1; i >= 0; i--) {
                    output1[i] = ch;
                }
                
                return new String(output1);
                
            case 2:
                
                char ch0 = str.charAt(0);
                char ch1 = str.charAt(1);
                char[] output2 = new char[outputLength];
                
                for (int i = (repeat * 2) - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }
                
                return new String(output2);
                
            default:
                
                StringBuffer buf = new StringBuffer(outputLength);
                
                for (int i = 0; i < repeat; i++) {
                    buf.append(str);
                }
                
                return buf.toString();
        }
    }
    
    /**
     * <p>
     * 比较两个字符串，取得第二个字符串中，和第一个字符串不同的部分
     * </p>
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 第二个字符串中，和第一个字符串不同的部分。如果两个字符串相同，则返回空字符串<code>""</code>
     */
    public static String difference(String str1, String str2) {
        if (str1 == null) {
            return str2;
        }
        
        if (str2 == null) {
            return str1;
        }
        
        int index = indexOfDifference(str1, str2);
        
        if (index == -1) {
            return "";
        }
        
        return str2.substring(index);
    }
    
    /**
     * <p>
     * 扩展并左对齐字符串，用指定字符串填充右边
     * </p>
     *
     * @param str 要对齐的字符串
     * @param size 扩展字符串到指定宽度
     * @param padStr 填充字符串
     *
     * @return 扩展后的字符串，如果字符串为null</code>，则返回null
     */
    public static String alignLeft(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        
        if ((padStr == null) || (padStr.length() == 0)) {
            padStr = " ";
        }
        
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        
        if (pads <= 0) {
            return str;
        }
        
        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            
            return str.concat(new String(padding));
        }
    }
    
    /**
     * <p>
     * 扩展并右对齐字符串，用指定字符串填充左边
     * </p>
     *
     * @param str 要对齐的字符串
     * @param size 扩展字符串到指定宽度
     * @param padStr 填充字符串
     *
     * @return 扩展后的字符串，如果字符串为null，则返回null
     */
    public static String alignRight(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        
        if ((padStr == null) || (padStr.length() == 0)) {
            padStr = " ";
        }
        
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        
        if (pads <= 0) {
            return str;
        }
        
        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            
            return new String(padding).concat(str);
        }
    }
    
    /**
     * <p>
     * 字符串编码转换的实现方法
     * </p>
     * 
     * @param str 待转换编码的字符串
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String changeCharset(String str, String newCharset)
        throws UnsupportedEncodingException {
        if (str != null) {
            // 用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }
    
    /**
     * <p>
     * 字符串编码转换的实现方法
     * </p>
     * 
     * @param str 待转换编码的字符串
     * @param oldCharset 原编码
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String changeCharset(String str, String oldCharset, String newCharset)
        throws UnsupportedEncodingException {
        if (str != null) {
            // 用旧的字符编码解码字符串。解码可能会出现异常。
            byte[] bs = str.getBytes(oldCharset);
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }
    
    /**
     * <p>
     * 根据Unicode编码完美的判断中文汉字和符号
     * </p>
     * 
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
    
    /**
     * <p>
     * 判断是否包含中文汉字
     * </p>
     * 
     * @param str
     * @return
     */
    public static boolean isChineseHave(String str) {
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * <p>
     * 判断是否只有中文汉字
     * </p>
     * 
     * @param str
     * @return
     */
    public static boolean isChineseAll(String str) {
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!isChinese(c)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * <p>
     * 验证是不是Int
     * </p>
     * 
     * @param str
     * @return
     */
    public static boolean validateInt(String str) {
        if (str == null || str.trim().equals(""))
            return false;
        
        char c;
        for (int i = 0, l = str.length(); i < l; i++) {
            c = str.charAt(i);
            if (!((c >= '0') && (c <= '9')))
                return false;
        }
        
        return true;
    }
}
