package com.zhph.base.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.zhph.base.redis.JedisLock;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.zhph.base.common.CommonUtils;
import com.zhph.base.common.StringOperator;
import com.zhph.base.common.StringUtil;
import com.zhph.base.redis.RedisUtil;

/**
 * Excel 操作类
 * 
 * @see  2012-01-11
 */
public class ExeclUtil {
    private static Logger log = Logger.getLogger(ExeclUtil.class);

    /**
     * @author kang_yuan
     * @param strings 原始数组
     * @param i 循环的步进值，拆分的第几次
     * @param column 自定义新数组的长度大小
     * @since 2014-07-24 案例： 导出Excel的时候，数组中的数据大于一个sheet的
     * */
    public String[][] substrStringArray(String[][] strings, int i, int column) {
		String[][] newStrings = null;
		if (strings.length - i * column > column) { // 根据原始数组的大小动态的去创建新的数组
		    newStrings = new String[column][];
		} else{
		    newStrings = new String[strings.length - i * column][];
		}
		for (int j = 0; j < column; j++) {
		    if (i * column + j < strings.length) {
		    	newStrings[j] = strings[i * column + j];
		    } else{
		    	break; // 超出原始数组的边界
		    }
		}
		return newStrings;
    }
    
    /**
     * excel导出 没有压缩版 add by 李东奎2017 04 06
     */
    public void downloadExecl(String[] colTitleAry, short[] colWidthAry, String[][] contentAry, int[][] numColAry, HttpServletResponse response, String fileName, String reportName) throws Exception {
        JedisLock lock = RedisUtil.getMethodLock(false);// 获取JedisLock
        try {
            if (lock.acquire()) {
                String filePath = fileName;
                HSSFWorkbook wb = new HSSFWorkbook();
                if (contentAry.length > 65535) { // 需要导出的数据需要多个sheet存放
                    int sheetCount = contentAry.length / 50000 + 1;// 每个sheet存放50000行数据
                    for (int i = 0; i < sheetCount; i++) {
                        String[][] newContent = substrStringArray(contentAry, i, 50000);
                        HSSFSheet sheet = wb.createSheet(reportName + i);
                        int rowCount = 0;
                        HSSFFont font = wb.createFont();
                        font.setFontName(HSSFFont.FONT_ARIAL);
                        HSSFCellStyle colTitleStyle = createColTitleStyle(wb, font);
                        createColTitleRow(sheet, colTitleStyle, rowCount, colTitleAry, colWidthAry);
                        HSSFCellStyle contentStyle = createDefaultStyle(wb, font);
                        HSSFCellStyle rmbStyle = createRMBStyle(wb, font);
                        writeContent(wb, sheet, contentStyle, rmbStyle, newContent, numColAry, rowCount + 1);
                    }
                } else { // 需要导出的数据一个sheet就可以满足了
                    HSSFSheet sheet = wb.createSheet(reportName);
                    int rowCount = 0;
                    HSSFFont font = wb.createFont();
                    font.setFontName(HSSFFont.FONT_ARIAL);
                    HSSFCellStyle colTitleStyle = createColTitleStyle(wb, font);
                    createColTitleRow(sheet, colTitleStyle, rowCount, colTitleAry, colWidthAry);
                    HSSFCellStyle contentStyle = createDefaultStyle(wb, font);
                    HSSFCellStyle rmbStyle = createRMBStyle(wb, font);
                    writeContent(wb, sheet, contentStyle, rmbStyle, contentAry, numColAry, rowCount + 1);
                }
                if (StringUtil.isNull(fileName)) {
                    fileName = "my_excel";
                }
                //创建临时目录
                CommonUtils.mkDirs(filePath);
                //创建临时文件存放路径
                String newfile = filePath + File.separator + reportName + ".xls";
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(newfile);
                    wb.write(fos);
                    fos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                InputStream is = null;
                OutputStream os = null;
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    response.setContentType("multipart/form-data");
                    response.addHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(reportName, "UTF-8") + ".xls");
                    is = new FileInputStream(newfile);
                    bis = new BufferedInputStream(is);
                    os = response.getOutputStream();
                    bos = new BufferedOutputStream(os);
                    
                    int read = 0;
                    byte[] bytes = new byte[8072];
                    while ((read = bis.read(bytes, 0, bytes.length)) != -1) {
                        bos.write(bytes, 0, read);
                    }
                    bos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    bos.close();
                    os.close();
                    bis.close();
                    is.close();
                    //删除临时目录和临时文件
                    StringOperator.deletePath(filePath);
                    File file = new File(filePath);
                    file.delete();
                }
            }
        } catch (InterruptedException e) {
            StringBuffer sb = new StringBuffer();
            sb.append("方法在[downloadExecl(String[] colTitleAry,short[] colWidthAry, String[][] contentAry, int[][] numColAry,HttpServletResponse response, String fileName, String reportName)]中");
            e.printStackTrace();
            log.error("Redis异常：" + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.release();
        }
    }

    private void writeContent(HSSFWorkbook wb, HSSFSheet sheet, HSSFCellStyle style, HSSFCellStyle rmbsStyle, String[][] contentAry, int[][] numColAry, int rowCount) {
		for (int i = 0; i < contentAry.length; i++) {
		    HSSFRow row = sheet.createRow(i + rowCount);
		    for (int j = 0; j < contentAry[i].length; j++) {
				String obj = contentAry[i][j] == null ? "" : contentAry[i][j];
				Double cellValue = 0.0;
				HSSFCell cell = row.createCell(j);
				cell.setCellValue(obj);
				cell.setCellStyle(style);
				for (int k = 0; numColAry.length != 0 && k < numColAry.length; k++) {
				    if (j == numColAry[k][0]) {
						if (!obj.equals("")) {
						    if (numColAry[k][1] == 2) {
								cellValue = new Double(obj);
								cell.setCellValue(cellValue);
								cell.setCellStyle(rmbsStyle);
						    } else if (numColAry[k][1] == 5) {
								cell.setCellValue(obj.toString());
								cell.setCellStyle(rmbsStyle);
						    } else {
								cellValue = new Double(obj);
								cell.setCellValue(cellValue);
								cell.setCellStyle(style);
						    }
						    break;
						}
				    }
				}
		    }
		}
    }

    @SuppressWarnings("deprecation")
	private static HSSFCellStyle createColTitleStyle(HSSFWorkbook wb, HSSFFont font) {
		HSSFCellStyle titleStyle = wb.createCellStyle();
		titleStyle.setFont(font);
		titleStyle.setBorderLeft((short) 1);
		titleStyle.setBorderRight((short) 1);
		titleStyle.setBorderTop((short) 1);
		titleStyle.setBorderBottom((short) 1);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleStyle.setWrapText(true);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		return titleStyle;
    }

    @SuppressWarnings("deprecation")
	private static HSSFCellStyle createRMBStyle(HSSFWorkbook wk, HSSFFont font) {
		HSSFCellStyle style = wk.createCellStyle();
		HSSFDataFormat format = wk.createDataFormat();
		style.setDataFormat(format.getFormat("#,##0.00"));
		style.setBorderTop((short) 1);
		style.setBorderLeft((short) 1);
		style.setBorderBottom((short) 1);
		style.setBorderRight((short) 1);
		style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		return style;
    }

    @SuppressWarnings("deprecation")
	private static HSSFCellStyle createDefaultStyle(HSSFWorkbook wk, HSSFFont font) {
		HSSFCellStyle style = wk.createCellStyle();
		style.setBorderTop((short) 1);
		style.setBorderLeft((short) 1);
		style.setBorderBottom((short) 1);
		style.setBorderRight((short) 1);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return style;
    }

    private static void createColTitleRow(HSSFSheet sheet, HSSFCellStyle style, int rowNum, String[] colTitleAry, short[] colWidthAry) {
		HSSFRow row = sheet.createRow(rowNum);
		row.setHeight((short) (30 * 16));
		for (int i = 0; i < colTitleAry.length; i++) {
		    HSSFCell cell = row.createCell(i);
		    cell.setCellStyle(style);
		    cell.setCellValue(colTitleAry[i]);
		    sheet.setColumnWidth(i, (36 * colWidthAry[i]));
		}
    }
}
