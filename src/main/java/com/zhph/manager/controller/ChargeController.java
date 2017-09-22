package com.zhph.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhph.base.utils.RequestUtil;
import com.zhph.manager.service.ChargeService;
import com.zhph.payment.charge.entity.ChargeRecordDetail;
import com.zhph.payment.charge.service.PushChargeInfoService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 单扣批扣管理
 * Created by zhph on 2017/8/7.
 */
@Controller
@RequestMapping("/charge")
public class ChargeController {
    private final static Logger logger = LoggerFactory.getLogger(ChargeController.class);

    @Resource
    private ChargeService chargeService;

    @Resource
    private PushChargeInfoService pushChargeInfoService;

    //页面跳转
    @RequestMapping("/singlecharge")
    public String singlecharge(){
        return "charge/singlecharge";
    }
    //取所有的
    @RequestMapping("/singlechargelist")
    @ResponseBody
    public Map<String , Object> singlechargelist(HttpServletRequest request){
        Map<String , Object> parameter = RequestUtil.getParameter(request);
        Map<String , Object> result =  chargeService.getChargeRecored(parameter);
        return result;
    }

    //更新所有的
    @RequestMapping("/updateSinglecharge")
    public void updateSinglecharge(){

    }

    @RequestMapping("/exportData")
    public void exportData(HttpServletRequest request, HttpServletResponse response){
        logger.info("导出单扣扣款记录 开始");
        Map<String , Object> parameter = RequestUtil.getParameter(request);
        try{
            String path = "exTemplate/singleCharge.xls";
            String filePath = request.getRealPath("/").replace("\\", "/") + "fileTemp/";
            String fileName = "单扣扣款记录_" + new Date().getTime();
            String extendName = ".xls";
            int startRow = 0;

            List<File> fileList = new ArrayList<File>();

            while (true) {
                parameter.put("currentPage",startRow * 50000);
                parameter.put("limit",(startRow * 50000)+50000);

                Map<String , Object> result =  chargeService.getChargeRecored(parameter);
                List<ChargeRecordDetail> repaymenInfotOfExport = (List<ChargeRecordDetail>)result.get("result");
                FileUtils.Copy(path, filePath + fileName + "_" + (startRow + 1) + extendName);
                File file = new File(filePath + fileName + "_" + (startRow + 1) + extendName);
                ExcelUtil<ChargeRecordDetail> vExcelUtil = new ExcelUtil<ChargeRecordDetail>();
                String filterStr = "id,serialVersionUID,charge_type,charge_no"; //导出过滤掉这些字段
                vExcelUtil.writeExcelContentByFileter(file, repaymenInfotOfExport,filterStr);
                fileList.add(file);
                startRow++;
                if (repaymenInfotOfExport.size() < 50000) {
                    break;
                }
            }
            String zipFileName = "单扣扣款记录_" + new Date().getTime() + ".zip";
            FileUtils.createFile(filePath, zipFileName);
            File zipFile = new File(filePath + zipFileName);
            ZipUtil.compressToZip(fileList, zipFile);

            DownLoadUtil.downloadFile(response, zipFileName, zipFile);
        }catch (Exception e){  StringBuilder sBuilder = new StringBuilder();
            sBuilder.append("导出单扣扣款记录异常，ChargeController【exportData】中");
            logger.error(sBuilder.toString(), e);
            e.printStackTrace();
        }
    }


    @RequestMapping("/pushMessage")
    @ResponseBody
    public String pushMessage(HttpServletRequest request){
        JSONObject json = new JSONObject();

        pushChargeInfoService.synPushSingleChargeInfo();
        json.put("statu",1);
        json.put("message","操作成功");
        return json.toJSONString();
    }
    @RequestMapping("/pushQuery")
    @ResponseBody
    public String pushQuery(HttpServletRequest request){
        JSONObject json = new JSONObject();
        pushChargeInfoService.synQueryPayingData();
        json.put("statu",1);
        json.put("message","操作成功");
        return json.toJSONString();
    }


}
