package com.zhph.base.utils;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * Author: Zou Yao
 * Description: (xml与Java对象之间的转换器)
 * Time: 2017/7/25 11:17
 *
**/
public class XmlConvertHandler {

    //日志打印对象
    private static Logger  log  =  LoggerFactory.getLogger(XmlConvertHandler.class);



    /**
     *
     * Author: zou yao
     * Description: {私有构造器}
     * Date: 2017/7/25 11:45
     *
    **/
    private XmlConvertHandler(){
        throw new RuntimeException("不可创建对象");
    }
    /**
     *
     * Author: zou yao
     * Description: {xml字符串转化成为对象}
     * Date: 2017/7/25 11:38
     *
    **/
    public static  <T> List<T>  domConvert(Class<T>  entityClass , String xmlSource , String entityTagName){
        if(xmlSource == null)
        {
            return null;
        }
        //编码字符串
        StringReader  reader = new  StringReader(xmlSource);
        //通过流解析成SAX的inputSource
        InputSource input =  new InputSource(reader);
        return domConvert(entityClass, input, entityTagName);

    }


    /**
     *
     * Author: zou yao
     * Description: {输入源转化成对象}
     * Date: 2017/7/25 11:37
     *
    **/
    private static <T> List<T>  domConvert(Class<T>  entityClass  ,  InputSource  input ,  String entityTagName){
        if(input == null)
        {
            return null;
        }
        log.debug("开始解析Xml,创建类 {} " ,entityClass.getName());
        //文本对象工厂
        DocumentBuilderFactory  factory  =  DocumentBuilderFactory.newInstance();
        //XML文本对象
        Document  document =  null;
        //构建XML文本对象
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document  =  builder.parse(input);
        } catch (ParserConfigurationException e) {
            log.error("构建文本对象出现转换异常");
            e.printStackTrace();
        } catch (SAXException e) {
            log.error("构建文本对象出现SAX异常");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("构建文本对象出现IO异常");
            e.printStackTrace();
        }

        return splitElementToBind(entityClass, document , entityTagName);
    }
    /**
     *
     * Author: zou yao
     * Description: {拆分元素并且绑定}
     * Date: 2017/7/25 11:31
     *
    **/
    private static <T> List<T>  splitElementToBind(Class<T>  entityClass,Document  document , String entityTagName){
        List<T>  result = new ArrayList<>();
        Element  rootElement = document.getDocumentElement();
        //获取到域数组
        Field[] fields = entityClass.getDeclaredFields();
        //获取到需要转换的对象结点集合
        NodeList  entityList= null;
        if(entityTagName == null || "root".equals(entityTagName)){
            T obj = createNewObject(rootElement.getChildNodes(), entityClass, fields);
            result.add(obj);
        }
        else{
            entityList  = rootElement.getElementsByTagName(entityTagName);
        }

        if(entityList != null){
            //循环遍历集合，寻求子节点信息
            for(int i = 0 ; i < entityList.getLength() ; i++){
                //获取到对象节点
                Node node = entityList.item(i);
                //取出子节点集合
                NodeList  nodeList = node.getChildNodes();
                T obj = createNewObject(nodeList, entityClass, fields);
                result.add(obj);
            }
        }
        return result;
    }
    /**
     *
     * Author: zou yao
     * Description: {创建对象,并且将对象与节点进行绑定}
     * Date: 2017/7/25 11:29
     *
    **/
    private static <T>  T   createNewObject(NodeList  nodeList , Class<T>  entityClass ,Field[] fields){
        T  obj = null;

        try {
            //反射创建对象
            obj = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("反射构建对象出现异常，请检测构造器");
            e.printStackTrace();
        }
        //将子节点与对象绑定
        bindNodeListToObject(obj, fields, nodeList);

        return obj;
    }
    /**
     *
     * Author: zou yao
     * Description: {将XML节点与JavaBean进行绑定}
     * Date: 2017/7/25 11:18
     * 
    **/
    private static void  bindNodeListToObject(Object obj, Field[] fields, NodeList  nodeList){

        //判断是否传入空值，规避异常
        if(obj != null &&  nodeList!=null &&  fields!= null){
            //循环遍历子节点获取节点名称与值
            for(int i = 0 ; i < nodeList.getLength() ; i++){

                Node node  = nodeList.item(i);
                String  tagName = node.getNodeName().toLowerCase();
                String value = node.getFirstChild() ==null ? null :node.getFirstChild().getNodeValue();
                //若为空值则无需赋值
                if(value == null){
                    continue;
                }
                //将子节点名称与对象属性名进行循环匹配
                for (Field field : fields) {
                    String fieldName = field.getName().toLowerCase();
                    //如果XML标签能够匹配上对象的属性名则赋值
                    if (tagName.equals(fieldName)) {
                        try {
                            //设置对象的熟悉值
                            ReflectHandler.setValueByFieldName(obj, field.getName(), value);
                        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
                            log.error("反射赋值出现异常，请检测GETTER SETTER");
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }
}
