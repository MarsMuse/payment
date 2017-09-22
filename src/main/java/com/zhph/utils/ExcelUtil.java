package com.zhph.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ExcelUtil<T> {

    /**
     *
     * @Title: writeExcelContent
     * @param：@param soucreFileString
     * @param：@param file
     * @param：@param dataSet
     * @param：@throws IOException
     * @param：@throws NoSuchMethodException
     * @param：@throws SecurityException
     * @param：@throws InvocationTargetException
     * @return：void
     * @Description：TODO(将list写入到Excel 文件中)
     * @author liuping
     * @date 2016-9-7 下午2:18:59
     * @throws
     */
    @SuppressWarnings("unused")
    public void writeExcelContent(File file, Collection<T> dataSet)
            throws IOException, NoSuchMethodException, SecurityException,
            InvocationTargetException {
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = null;

        // 写入内容
        Iterator<T> iterator = dataSet.iterator();
        int index = 1;
        int rowid = 0;
        int count =0;
        while (iterator.hasNext()) {
            count ++;
            if(count % 65000 ==0){//判断 行数大于最大行后就创建 一个新的sheet页签
                rowid ++;
                workbook.createSheet(file.getName().split("_")[0]+rowid);
                index =0;
            }
            sheet = workbook.getSheetAt(rowid);
            HSSFRow row = sheet.createRow(index);
            T t = (T) iterator.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (short i = 0; i < fields.length; i++) {
                if (i == 0) {
                    @SuppressWarnings("deprecation")
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(count);
                    cell = row.createCell(i + 1);
                    fields[i].setAccessible(true);
                    try {
                        String fieldName = fields[i].getName();
                        String getMethodName = "get"
                                + fieldName.substring(0, 1).toUpperCase()
                                + fieldName.substring(1);
                        Object valueObject = fields[i].get(t);

                        Class<? extends Object> tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName,
                                new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        if (valueObject instanceof String) {
                            cell.setCellValue(valueObject.toString());
                        } else {
                            cell.setCellValue(valueObject + "");
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    HSSFCell cell = row.createCell(i + 1);
                    fields[i].setAccessible(true);
                    try {
                        String fieldName = fields[i].getName();
                        String getMethodName = "get"
                                + fieldName.substring(0, 1).toUpperCase()
                                + fieldName.substring(1);
                        Object valueObject = fields[i].get(t);

                        Class<? extends Object> tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName,
                                new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        if (valueObject instanceof String) {
                            if (valueObject == null) {
                                cell.setCellValue("");
                            } else {
                                cell.setCellValue(valueObject.toString());
                            }

                        } else if (valueObject instanceof BigDecimal) {
                            BigDecimal vDecimal = (BigDecimal) value;
                            cell.setCellValue(vDecimal.doubleValue());
                        } else if (valueObject instanceof Integer) {
                            cell.setCellValue((Integer) valueObject);
                        } else if (valueObject instanceof Double) {
                            cell.setCellValue((Double) valueObject);
                        } else {
                            if (valueObject == null) {
                                cell.setCellValue("");
                            } else {
                                cell.setCellValue(valueObject.toString());
                            }

                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            index++;
        }
        OutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);

        outputStream.close();
    }
    @SuppressWarnings("unused")
    public void writeExcelContentByFileter(File file, Collection<T> dataSet,String filterStr)
            throws IOException, NoSuchMethodException, SecurityException,
            InvocationTargetException {
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = null;

        // 写入内容
        Iterator<T> iterator = dataSet.iterator();
        int index = 1;
        int rowid = 0;
        int count =0;
        while (iterator.hasNext()) {
            count ++;
            if(count % 65000 ==0){//判断 行数大于最大行后就创建 一个新的sheet页签
                rowid ++;
                workbook.createSheet(file.getName().split("_")[0]+rowid);
                index =0;
            }
            sheet = workbook.getSheetAt(rowid);
            HSSFRow row = sheet.createRow(index);
            T t = (T) iterator.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            int j = 0;
            for (short i = 0; i < fields.length; i++) {
                if (i == 0) {

                    @SuppressWarnings("deprecation")
                    HSSFCell cell = row.createCell(j);
                    cell.setCellValue(count);
                    if(null != filterStr && filterStr !="" && filterStr.indexOf(fields[i].getName()) > 0){continue;}
                    cell = row.createCell(j + 1);
                    fields[i].setAccessible(true);
                    try {
                        String fieldName = fields[i].getName();
                        String getMethodName = "get"
                                + fieldName.substring(0, 1).toUpperCase()
                                + fieldName.substring(1);
                        Object valueObject = fields[i].get(t);

                        Class<? extends Object> tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName,
                                new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        if (valueObject instanceof String) {
                            cell.setCellValue(valueObject.toString());
                        } else {
                            cell.setCellValue(valueObject + "");
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    HSSFCell cell = row.createCell(j + 1);
                    fields[i].setAccessible(true);
                    try {
                        String fieldName = fields[i].getName();
                        if(null != filterStr && filterStr !="" && filterStr.indexOf(fieldName) >= 0){continue;}
                        String getMethodName = "get"
                                + fieldName.substring(0, 1).toUpperCase()
                                + fieldName.substring(1);
                        Object valueObject = fields[i].get(t);

                        Class<? extends Object> tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName,
                                new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        if (valueObject instanceof String) {
                            if (valueObject == null) {
                                cell.setCellValue("");
                            } else {
                                cell.setCellValue(valueObject.toString());
                            }

                        } else if (valueObject instanceof BigDecimal) {
                            BigDecimal vDecimal = (BigDecimal) value;
                            cell.setCellValue(vDecimal.doubleValue());
                        } else if (valueObject instanceof Integer) {
                            cell.setCellValue((Integer) valueObject);
                        } else if (valueObject instanceof Double) {
                            cell.setCellValue((Double) valueObject);
                        } else {
                            if (valueObject == null) {
                                cell.setCellValue("");
                            } else {
                                cell.setCellValue(valueObject.toString());
                            }

                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                j++;
            }
            index++;
        }
        OutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);

        outputStream.close();
    }

    @SuppressWarnings({"unused", "deprecation"})
    public void writeExcelContentNotContainOrderNumber(String soucreFileString,
                                                       File file, Collection<T> dataSet) throws IOException,
            NoSuchMethodException, SecurityException, InvocationTargetException {
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = workbook.getSheetAt(0);

        // 写入内容
        Iterator<T> iterator = dataSet.iterator();
        int index = 1;
        while (iterator.hasNext()) {
            HSSFRow row = sheet.createRow(index);
            T t = (T) iterator.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();

            for (short i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                fields[i].setAccessible(true);
                try {
                    String fieldName = fields[i].getName();
                    String getMethodName = "get"
                            + fieldName.substring(0, 1).toUpperCase()
                            + fieldName.substring(1);
                    Object valueObject = fields[i].get(t);

                    Class<? extends Object> tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName,
                            new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});
                    if (valueObject instanceof String) {
                        if (valueObject == null) {
                            cell.setCellValue("");
                        } else {
                            cell.setCellValue(valueObject.toString());
                        }

                    } else if (valueObject instanceof BigDecimal) {
                        BigDecimal vDecimal = (BigDecimal) value;
                        cell.setCellValue(vDecimal.doubleValue());
                    } else if (valueObject instanceof Integer) {
                        cell.setCellValue((Integer) valueObject);
                    } else if (valueObject instanceof Double) {
                        cell.setCellValue((Double) valueObject);
                    } else {
                        if (valueObject == null) {
                            cell.setCellValue("");
                        } else {
                            cell.setCellValue(valueObject.toString());
                        }

                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            index++;
        }
        OutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);

        outputStream.close();
    }

    /*
     * 分公司逾期情况导出对writeExcelContent方法进行了部分修改，否则会将导出cxcel文件的文件头 覆盖(即标题等)
     */
    @SuppressWarnings({"deprecation", "unused"})
    public void writeExcelContent2(String soucreFileString, File file,
                                   Collection<T> dataSet) throws IOException, NoSuchMethodException,
            SecurityException, InvocationTargetException {
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = workbook.getSheetAt(0);

        // 写入内容
        Iterator<T> iterator = dataSet.iterator();
        int index = 2;
        while (iterator.hasNext()) {
            HSSFRow row = sheet.createRow(index);
            T t = (T) iterator.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();

            for (short i = 0; i < fields.length; i++) {
                if (i == 0) {
                    HSSFCell cell = row.createCell(i);
                    // cell.setCellValue(index);
                    cell = row.createCell(i);
                    fields[i].setAccessible(true);
                    try {
                        String fieldName = fields[i].getName();
                        String getMethodName = "get"
                                + fieldName.substring(0, 1).toUpperCase()
                                + fieldName.substring(1);
                        Object valueObject = fields[i].get(t);

                        Class<? extends Object> tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName,
                                new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        if (valueObject instanceof String) {
                            cell.setCellValue(valueObject.toString());
                        } else {
                            cell.setCellValue(valueObject + "");
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    HSSFCell cell = row.createCell(i);
                    fields[i].setAccessible(true);
                    try {
                        String fieldName = fields[i].getName();
                        String getMethodName = "get"
                                + fieldName.substring(0, 1).toUpperCase()
                                + fieldName.substring(1);
                        Object valueObject = fields[i].get(t);

                        Class<? extends Object> tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName,
                                new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        if (valueObject instanceof String) {
                            if (valueObject == null) {
                                cell.setCellValue("");
                            } else {
                                cell.setCellValue(valueObject.toString());
                            }

                        } else if (valueObject instanceof BigDecimal) {
                            BigDecimal vDecimal = (BigDecimal) value;
                            cell.setCellValue(vDecimal.doubleValue());
                        } else if (valueObject instanceof Integer) {
                            cell.setCellValue((Integer) valueObject);
                        } else if (valueObject instanceof Double) {
                            cell.setCellValue((Double) valueObject);
                        } else {
                            if (valueObject == null) {
                                cell.setCellValue("");
                            } else {
                                cell.setCellValue(valueObject.toString());
                            }

                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            index++;
        }
        OutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);

        outputStream.close();
    }
    /**
     * o7excel写入内容
     *
     * @Title: writeExcelContentByXSSF
     * @param：@param soucreFileString
     * @param：@param file
     * @param：@param dataSet
     * @param：@throws IOException
     * @param：@throws NoSuchMethodException
     * @param：@throws SecurityException
     * @param：@throws InvocationTargetException
     * @return：void
     * @Description：TODO(这里用一句话描述这个方法的作用)
     * @author xlin
     * @date 2016-8-29 下午3:13:12
     * @throws
     */
    @SuppressWarnings("unused")
    public void writeExcelContentByXSSF(String soucreFileString, File file,
                                        Collection<T> dataSet) throws IOException, NoSuchMethodException,
            SecurityException, InvocationTargetException {
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));

        SXSSFWorkbook xWorkbook = new SXSSFWorkbook(workbook, 10000);
        //	XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
        Sheet sheet = xWorkbook.getSheetAt(0);

        // 写入内容
        Iterator<T> iterator = dataSet.iterator();
        int index = 1;
        while (iterator.hasNext()) {
            Row row =  sheet.createRow(index);
            T t = (T) iterator.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();

            for (short i = 0; i < fields.length; i++) {
                if (i == 0) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(index);
                    cell = row.createCell(i + 1);
                    fields[i].setAccessible(true);
                    try {
                        String fieldName = fields[i].getName();
                        String getMethodName = "get"
                                + fieldName.substring(0, 1).toUpperCase()
                                + fieldName.substring(1);
                        Object valueObject = fields[i].get(t);

                        Class<? extends Object> tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName,
                                new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        if (valueObject instanceof String) {
                            cell.setCellValue(valueObject.toString());
                        } else {
                            cell.setCellValue(valueObject + "");
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    Cell cell = row.createCell(i + 1);
                    fields[i].setAccessible(true);
                    try {
                        String fieldName = fields[i].getName();
                        String getMethodName = "get"
                                + fieldName.substring(0, 1).toUpperCase()
                                + fieldName.substring(1);
                        Object valueObject = fields[i].get(t);

                        Class<? extends Object> tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName,
                                new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        if (valueObject instanceof String) {
                            if (valueObject == null) {
                                cell.setCellValue("");
                            } else {
                                cell.setCellValue(valueObject.toString());
                            }

                        } else if (valueObject instanceof BigDecimal) {
                            BigDecimal vDecimal = (BigDecimal) value;
                            cell.setCellValue(vDecimal.doubleValue());
                        } else if (valueObject instanceof Integer) {
                            cell.setCellValue((Integer) valueObject);
                        } else if (valueObject instanceof Double) {
                            cell.setCellValue((Double) valueObject);
                        } else {
                            if (valueObject == null) {
                                cell.setCellValue("");
                            } else {
                                cell.setCellValue(valueObject.toString());
                            }

                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            index++;
        }
        OutputStream outputStream = new FileOutputStream(file);
        xWorkbook.write(outputStream);
        outputStream.close();
        xWorkbook.dispose();
    }

    /**
     * 导出excel,根据数组元素排列顺序导出
     *
     * @Title: writeExcelContent
     * @param：@param file
     * @param：@param list
     * @param：@param startRow 从第几行开始写入数据（行号，非行的索引）
     * @param：@throws FileNotFoundException
     * @param：@throws IOException
     * @return：void
     * @Description：TODO(这里用一句话描述这个方法的作用)
     * @author liuping
     * @date 2016-9-8 下午4:34:40
     * @throws
     */
    public void writeExcelContent(File file, List<Object[]> list, int startRow)
            throws FileNotFoundException, IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));

        SXSSFWorkbook xWorkbook = new SXSSFWorkbook(workbook, 10000);

        Sheet sheet = xWorkbook.getSheetAt(0);
        // 序号
        int index = 1;
        Row row = null;
        Object object = null;
        Cell cell = null;
        for (Object[] array : list) {
            row = sheet.createRow(startRow - 1);
            System.out.println(index);
            for (int i = 0; i < array.length; i++) {
                object = array[i];
                // if (i == 0) {
                // cell = row.createCell(i);
                // cell.setCellValue(index);// 设置序号
                // cell = row.createCell(i + 1);
                // if (object instanceof String) {
                // cell.setCellValue(object.toString());
                // } else {
                // cell.setCellValue(object + "");
                // }
                //
                // } else {\
                cell = row.createCell(i);
                if (object instanceof String) {
                    cell.setCellValue(object.toString());
                } else if (object instanceof BigDecimal) {
                    BigDecimal vDecimal = (BigDecimal) object;
                    cell.setCellValue(vDecimal.doubleValue());
                } else if (object instanceof Integer) {
                    cell.setCellValue((Integer) object);
                } else if (object instanceof Double) {
                    cell.setCellValue((Double) object);
                } else {
                    if (object == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(object.toString());
                    }
                }
                // }
            }
            startRow++;
            index++;
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        xWorkbook.write(outputStream);
        outputStream.close();
        xWorkbook.dispose();
    }
}
