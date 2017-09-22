package com.zhph.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhph.base.utils.RequestUtil;
import com.zhph.manager.model.PaymentRecorde;
import com.zhph.manager.service.impl.PaymentRecordServiceImp;
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
 * Created by ZHPH on 2017/8/8.
 */
@Controller
@RequestMapping("/paymentRecord")
public class PaymentRecordController {

    private Logger logger = LoggerFactory.getLogger(PaymentRecordController.class);

    @Resource
    private PaymentRecordServiceImp paymentRecordServiceImp;

    @RequestMapping("/toPaymentRecord")
    public String toPaymentRecord(){
        return "singleInterLog/singleInterLog";
//        return "paymentRecord/paymentRecordList";
    }

    //取所有的
    @RequestMapping("/paymentRecordList")
    @ResponseBody
    public Map<String , Object> paymentRecordList(HttpServletRequest request){
        logger.info("查询单扣扣款详情记录 开始");
        Map<String , Object> parameter = RequestUtil.getParameter(request);
        Map<String , Object> result =  paymentRecordServiceImp.getPaymentRecord(parameter);
        return result;
    }
    @RequestMapping("/exportData")
    public void exportData(HttpServletRequest request, HttpServletResponse response){
        logger.info("导出单扣扣款详情记录 开始");
        Map<String , Object> parameter = new HashMap<>();
        try{
            request.setCharacterEncoding("UTF-8");
            parameter = RequestUtil.getParameter(request);
            String path = "exTemplate/paymentRecord.xls";
            String filePath = request.getRealPath("/").replace("\\", "/") + "fileTemp/";
            String fileName = "单扣扣款详情记录_" + new Date().getTime();
            String extendName = ".xls";
            int startRow = 0;

            List<File> fileList = new ArrayList<File>();

            while (true) {
                parameter.put("currentPage",startRow * 50000);
                parameter.put("limit",(startRow * 50000)+50000);
                //中文乱码修改
                parameter.put("name",new String(String.valueOf(parameter.get("name")).getBytes("ISO-8859-1"),"UTF-8"));

                Map<String , Object> result =  paymentRecordServiceImp.getPaymentRecord(parameter);
                List<PaymentRecorde> repaymenInfotOfExport = (List<PaymentRecorde>)result.get("result");
                FileUtils.Copy(path, filePath + fileName + "_" + (startRow + 1) + extendName);
                File file = new File(filePath + fileName + "_" + (startRow + 1) + extendName);
                ExcelUtil<PaymentRecorde> vExcelUtil = new ExcelUtil<PaymentRecorde>();
                vExcelUtil.writeExcelContent(file, repaymenInfotOfExport);
                fileList.add(file);
                startRow++;
                if (repaymenInfotOfExport.size() < 50000) {
                    break;
                }
            }
            String zipFileName = "单扣扣款详情记录_" + new Date().getTime() + ".zip";
            FileUtils.createFile(filePath, zipFileName);
            File zipFile = new File(filePath + zipFileName);
            ZipUtil.compressToZip(fileList, zipFile);

            DownLoadUtil.downloadFile(response, zipFileName, zipFile);
        }catch (Exception e){  StringBuilder sBuilder = new StringBuilder();
            sBuilder.append("导出单扣扣款详情记录异常，在PaymentRecordController【exportData】中");
            logger.error(sBuilder.toString(), e);
            e.printStackTrace();
        }
    }

}
