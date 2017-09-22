package com.zhph.manager.controller;

import com.zhph.base.utils.RequestUtil;
import com.zhph.manager.model.PaymentRecorde;
import com.zhph.manager.model.SingleInterLogList;
import com.zhph.manager.service.SingleInterLogService;
import com.zhph.utils.DownLoadUtil;
import com.zhph.utils.ExcelUtil;
import com.zhph.utils.FileUtils;
import com.zhph.utils.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * Created by ZHPH on 2017/9/14.
 */
@Controller
@RequestMapping("/singleInterLog")
public class SingleInterfaceLogController {
    private Logger logger = LoggerFactory.getLogger(SingleInterfaceLogController.class);

    @Resource
    private SingleInterLogService singleInterLogService;

    @RequestMapping("/toSingleInterLog")
    public String toPaymentRecord(){
        return "singleInterLog/singleInterLog";
    }

    //取所有的
    @RequestMapping("/singleInterLogList")
    @ResponseBody
    public Map<String , Object> paymentRecordList(HttpServletRequest request){
        logger.info("查询单扣日志记录表记录 开始");
        Map<String , Object> parameter = RequestUtil.getParameter(request);
        Map<String , Object> result =  singleInterLogService.getSingleInterLog(parameter);
        return result;
    }


    //根据ID获取内容
    @RequestMapping("/queryInterfaceContent")
    @ResponseBody
    public Map<String , Object> queryInterfaceContent(HttpServletRequest request){
        logger.info("查询单扣日志记录表记录 开始");
        Map<String , Object> result = new HashMap<>();
        Map<String , Object> parameter = RequestUtil.getParameter(request);
        String priNumebr = parameter.get("priNumber") == null ? "":parameter.get("priNumber").toString();
        result.put("status","1");
        if("".equals(priNumebr)){
            result.put("message","参数错误");
            return result;
        }
        String content = singleInterLogService.queryInterfaceContent(priNumebr);
        result.put("status","0");
        result.put("result",content);
        return result;
    }


    @RequestMapping("/exportData")
    public void exportData(HttpServletRequest request, HttpServletResponse response){
        logger.info("导出单扣日志记录 开始");
        Map<String , Object> parameter = new HashMap<>();
        try{
            request.setCharacterEncoding("UTF-8");
            parameter = RequestUtil.getParameter(request);
            String path = "exTemplate/singleInterLog.xls";
            String filePath = request.getRealPath("/").replace("\\", "/") + "fileTemp/";
            String fileName = "单扣日志记录_" + new Date().getTime();
            String extendName = ".xls";
            int startRow = 0;

            List<File> fileList = new ArrayList<File>();

            while (true) {
                parameter.put("currentPage",startRow * 50000);
                parameter.put("limit",(startRow * 50000)+50000);
                //中文乱码修改
                parameter.put("name",new String(String.valueOf(parameter.get("name")).getBytes("ISO-8859-1"),"UTF-8"));

                Map<String , Object> result =  singleInterLogService.getSingleInterLog(parameter);
                List<SingleInterLogList> repaymenInfotOfExport = (List<SingleInterLogList>)result.get("result");
                FileUtils.Copy(path, filePath + fileName + "_" + (startRow + 1) + extendName);
                File file = new File(filePath + fileName + "_" + (startRow + 1) + extendName);
                ExcelUtil<SingleInterLogList> vExcelUtil = new ExcelUtil<SingleInterLogList>();
                String filterStr = "priNumber,interfaceContent,isLoop"; //导出过滤掉这些字段
                vExcelUtil.writeExcelContentByFileter(file, repaymenInfotOfExport,filterStr);
                fileList.add(file);
                startRow++;
                if (repaymenInfotOfExport.size() < 50000) {
                    break;
                }
            }
            String zipFileName = "单扣日志记录_" + new Date().getTime() + ".zip";
            FileUtils.createFile(filePath, zipFileName);
            File zipFile = new File(filePath + zipFileName);
            ZipUtil.compressToZip(fileList, zipFile);

            DownLoadUtil.downloadFile(response, zipFileName, zipFile);
        }catch (Exception e){  StringBuilder sBuilder = new StringBuilder();
            sBuilder.append("导出单扣日志记录异常，在SingleInterfaceLogController【exportData】中");
            logger.error(sBuilder.toString(), e);
            e.printStackTrace();
        }
    }
}
