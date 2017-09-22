<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<style>
    .select2-search--dropdown{display: none;}/** 隐藏下拉框快捷搜索*/
    .select2-selection--single{height: 34px !important;} /**设置显示内容的行高和字体高度**/
    .select2-selection__rendered{height:34px;line-height: 34px !important;}
    .select2-selection__arrow{top:4px !important;}
    /*.select2-container--default{width: 179px !important;}*/
</style>
<div>
    <div class="breadcrumbs" id="breadcrumbs">
        <ul class="breadcrumb">
            <li>
                <i class="icon-home home-icon"></i>
                <a href="#">批扣记录</a>
            </li>
            <li class="active">批扣记录信息列表</li>
        </ul>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
           <h3 class="panel-title">查询条件</h3>
        </div>
        <div class="panel-body">
            <form class="form-inline" id="queryForm" method="post">
            	<div class="row">
                    <div class="col-md-3"> 
                        <div class="form-group">
	                        <label for="createBeginTime" >创建时间：</label>
	                        <div class="input-group date form_date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="createBeginTime" data-link-format="yyyy-mm-dd">
	                            <input class="form-control" id="createBeginTime" name="createBeginTime" size="16" type="text" style="width: 140px;" readonly>
	                            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
	                            <!-- <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span> -->
	                        </div>
	                        <input type="hidden" id="createBeginTime" /><br/>
                        </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="createEndTime">至：</label>
                            <div class="input-group date form_date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="createEndTime" data-link-format="yyyy-mm-dd">
	                            <input class="form-control" id="createEndTime" name="createEndTime" size="16" type="text" style="width: 140px;" readonly>
	                            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
	                            <!-- <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span> -->
	                        </div>
	                        <input type="hidden" id="createEndTime" /><br/>
                        </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
	                        <label for="updateBeginTime" >更新时间：</label>
	                        <div class="input-group date form_date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="updateBeginTime" data-link-format="yyyy-mm-dd">
	                            <input class="form-control" id="updateBeginTime" name="updateBeginTime" size="16" type="text" style="width: 140px;" readonly>
	                            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
	                            <!-- <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span> -->
	                        </div>
	                        <input type="hidden" id="updateBeginTime" /><br/>
	                    </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="updateEndTime">至：</label>
                            <div class="input-group date form_date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="updateEndTime" data-link-format="yyyy-mm-dd">
	                            <input class="form-control" id="updateEndTime" name="updateEndTime" size="16" type="text" style="width: 140px;" readonly>
	                            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
	                            <!-- <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span> -->
	                        </div>
	                        <input type="hidden" id="updateEndTime" /><br/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="platformCode">平台名称：</label>
                            <select name="platformCode" id="platformCode" class="js-example-basic-single">
                                <option value="">请选择</option>
                                <%-- <c:forEach items="${platformList}" var="platform">
                                	<option value="${platform.platformCode}"<c:if test="${platform.platformCode==params.platformCode}">selected="selected"</c:if>>${platform.platformName}</option>
                            	</c:forEach> --%>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="sendFlag">发送状态：</label>
                            <select name="sendFlag" id="sendFlag" class="js-example-basic-single">
                                <option value="" >请选择</option>
                                <option value="0">发送中</option>
                                <option value="1">发送成功</option>
                                <option value="2">发送失败</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="endFlag">标志位状态：</label>
                            <select name="endFlag" id="endFlag" class="js-example-basic-single">
                                <option value="" >请选择</option>
                                <option value="0">未结束</option>
                                <option value="1">已结束</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="channelNo">渠道：</label>
                            <select name="channelNo" id="channelNo" class="js-example-basic-single">
                                <option value="" >请选择</option>
                               <%--  <c:forEach items="${channelList}" var="channel">
                                	<option value="${channel.channelNo}"<c:if test="${channel.channelNo==params.channelNo}">selected="selected"</c:if>>${channel.channelName}</option>
                            	</c:forEach> --%>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row">
                	<div class="col-md-3"> 
                        <div class="form-group">
                            <label for="batchNo">批扣号：</label>
                            <input type="text" id="batchNo" name="batchNo" class="form-control"  >
                        </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="mainBody">主体：</label>
                            <select name="mainBody" id="mainBody" class="js-example-basic-single">
                                <option value="" >请选择</option>
                                <option value="ZH">ZH</option>
                                <option value="HT">HT</option>
                            </select>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div id="toolbar1">
        <button type="button" class="btn btn-primary" onclick="queryDataBtn()"><span class="glyphicon glyphicon-search table-span-icon"></span>查询</button>
        <!-- <button type="button" class="btn btn-success" onclick="addDataByButton()"><span  class="glyphicon glyphicon-plus  table-span-icon"></span>新增</button> -->
        <button type="button" class="btn btn-info" onclick="exportDataByButton()"><span  class="glyphicon glyphicon-download-alt  table-span-icon"></span>导出</button>
    </div>
    <!-- 表格内容-->
    <table id="dataInforTable"></table>
</div>
<script>
	$(function () {
		//向后台请求获取页面下拉框数据
		$.ajax({
            url:"${path}/action/batchChargeRecord/getDropdownList.do",
            method: "post",
            dataType: "json"
        }).success(function(data){
        	if(data.flag=="false"){
        	    alert(data.errorStr);
        		return;
        	}
        	//绑定值到平台下拉框
        	$.each(data.platformList, function(key, val) {
                $("#platformCode").append('<option value="' + val.platformCode + '">' + val.platformName + '</option>');
            });
        	//绑定值到渠道下拉框
        	$.each(data.channelList, function(key, val) {
                $("#channelNo").append('<option value="' + val.channelNo + '">' + val.channelName + '</option>');
            });
        }).error(function(){
        	console.error("获取页面下拉框数据异常！");
        }); 
		//渲染select2
		$(".js-example-basic-single").select2({width:'179px',height:'34px'});
		//渲染datetimepicker
		$('.form_date').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
	        autoclose: 1,
	        todayHighlight: 1,
	        startView: 2,
	        minView: 2,
	        forceParse: 0
	    });
		//获取查询数据
	    getQueryInfor = function(gridHandler, page){
	        //表单数据
	        var queryData = $("#queryForm").serializeArray();
	        //根据不同节点查出不同数据
	        queryData.push({
	        	name : 'currentPage', 
	        	value: page.currentPage
	        });
	        queryData.push({
	        	name: 'limit', 
	        	value: page.limit
	        });
	        $.ajax({
	            url:"${path}/action/batchChargeRecord/batchChargeRecordData.do",
	            method: "post",
	            data: queryData,
	            dataType: "json"
	        }).success(gridHandler).error(function(){
	        	console.error("获取批扣记录信息数据异常！");
	        }); 
	    };
	    //渲染参数
	    var tbOptions = {
	   		uniqueId : 'id',
	        method : 'post',
	        toolbar : '#toolbar1',
	        //resizable:true, //注：如果要求启用resizable 一定要将paginationVAlign设置为top，否则在部分版本的chrome中出现table水平滚动轴与分页栏部分重合的情况
	        columns : [{
	   			title: '序号',
	   			width: "60px",
	   			align: 'center',
	   			formatter: function (value, row, index) {
	                return tba.currentOffset+index+1;
	            }
	   		}, {
	   			field: 'platformName',
	   			title: '平台名称',
	   		}, {
	   			field: 'batchNo',
	   			title: '批扣号',
	   			align: 'center',
	   		}, {
	   			field: 'sendInfoCount',
	   			title: '发送批扣数据量',
	   		}, {
	   			field: 'replyInfoCount',
	   			title: '收到扣款回复条数',
	   		}, {
                field: 'pushCount',
                title: '成功推送条数',
            }, {
	   			field: 'createTime',
	   			title: '创建时间',
	   			align: 'center',
	   		}, {
	   			field: 'updateTime',
	            title: '最近更新时间',
	            align: 'center',
	   		}, {
	   			field: 'endFlag',
	   			title: '标志位状态',
	   			align: 'center',
	   		}, {
	   			field: 'channelName',
	   			title: '渠道',
	   			align: 'center',
	   		}, {
	   			field: 'sendFlag',
	   			title: '发送状态',
	   			align: 'center',
	   		}, {
	   			field: 'mainBody',
	   			title: '主体',
	   			align: 'center',
	   		}, {
	   			field : 'handle',
	   			title : '操作',
	   			align : 'center', 
	   			formatter: function(value, row, index ){  
	                var details = '<button type="button" class="btn btn-success btn-sm" onclick="batchChargeRecordDetail(\''+row.batchNo+'\')" style="margin-right:12px;"><span class="glyphicon glyphicon-log-in table-span-icon"></span>详情</button>';
	                return details;  
	            }
	   		}]
	    };
	    //表格初始化配置
	    var tbControl = {
	   		dataHandler : getQueryInfor,
	   		options     : tbOptions
	    };
	    //表格渲染对象
	    var tba = new TableByAjax();
	    //获取到表格控制器
	    var gridController = tba.grid("dataInforTable" , tbControl);
	    //查询这个表格
	    queryDataBtn = function(){
	        gridController.query();
	    };
	    //跳转到批扣记录信息详情页面
	    batchChargeRecordDetail = function(batchNo) {
	    	$.ajax({
	            type: "post",
	            data: {batchNo : batchNo},
	            url: "${path}/action/batchChargeRecord/batchChargeRecordDetail.do",
	            success: function (data) {
	                $("#content").html(data);
	            },error:function(XMLHttpRequest,textStatus, errorThrown) {
	                if (XMLHttpRequest.status == 500) {
	                } else if(XMLHttpRequest.status == 404) {
	                } else {
	                    window.location.reload();
	                }
	            }
	        });
	    };
	    //导出报表
		exportDataByButton = function(){
			//表单数据
	        var queryData = $("#queryForm").serializeArray();
			//将查询条件拼装成字符串
	        var query = "";
	        for(var i = 0 ; i< queryData.length ; i++){
	        	query += queryData[i].name + "=" + queryData[i].value;
	        	if(i != queryData.length-1){
	        		query += "&";
	        	}
			}
	        window.location.href = "${path}/action/batchChargeRecord/exportBatchChargeRecordList.do?"+query;
	    };
	});
</script>

