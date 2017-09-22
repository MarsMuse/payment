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
            <li class="active">批扣记录信息详情</li>
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
                            <label for="paymentBeginDate">账单日：</label>
                            <div class="input-group date form_date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="paymentBeginDate" data-link-format="yyyy-mm-dd">
	                            <input class="form-control" id="paymentBeginDate" name="paymentBeginDate" size="16" type="text" style="width: 140px;" readonly>
	                            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
	                        </div>
	                        <input type="hidden" id="paymentBeginDate" /><br/>
                        </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="paymentEndDate">至：</label>
                            <div class="input-group date form_date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="paymentEndDate" data-link-format="yyyy-mm-dd">
	                            <input class="form-control" id="paymentEndDate" name="paymentEndDate" size="16" type="text" style="width: 140px;" readonly>
	                            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
	                        </div>
	                        <input type="hidden" id="paymentEndDate" /><br/>
                        </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="chargeBeginTime">扣款时间：</label>
                            <div class="input-group date form_date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="chargeBeginTime" data-link-format="yyyy-mm-dd">
	                            <input class="form-control" id="chargeBeginTime" name="chargeBeginTime" size="16" type="text" style="width: 140px;" readonly>
	                            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
	                        </div>
	                        <input type="hidden" id="chargeBeginTime" /><br/>
                        </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="chargeEndTime">至：</label>
                            <div class="input-group date form_date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="chargeEndTime" data-link-format="yyyy-mm-dd">
	                            <input class="form-control" id="chargeEndTime" name="chargeEndTime" size="16" type="text" style="width: 140px;" readonly>
	                            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
	                        </div>
	                        <input type="hidden" id="chargeEndTime" /><br/>
                        </div>
                    </div>
                </div>
                <div class="row">
                	<div class="col-md-3"> 
                        <div class="form-group">
                            <label for="platformCode">平台名称：</label>
                            <select name="platformCode" id="platformCode" class="js-example-basic-single">
                                <option value="">请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="chargeStatus">扣款状态：</label>
                            <select name="chargeStatus" id="chargeStatus" class="js-example-basic-single">
                                <option value="" >请选择</option>
                                <option value="0">扣款中</option>
                                <option value="1">扣款成功</option>
                                <option value="2">扣款失败</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="updateFlag">是否更新：</label>
                            <select name="updateFlag" id="updateFlag" class="js-example-basic-single">
                                <option value="" >请选择</option>
                                <option value="0">未更新</option>
                                <option value="1">已更新</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="channelNo">渠道：</label>
                            <select name="channelNo" id="channelNo" class="js-example-basic-single">
                                <option value="" >请选择</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="loanNo">合同号：</label>
                            <input type="text" id="loanNo" name="loanNo" class="form-control"  >
                        </div>
                    </div>
                	<div class="col-md-3"> 
                        <div class="form-group">
                            <label for="loanName">客户姓名：</label>
                            <input type="text" id="loanName" name="loanName" class="form-control"  >
                        </div>
                    </div>
                    <div class="col-md-3"> 
                        <div class="form-group">
                            <label for="chargeNo">扣款编号：</label>
                            <input type="text" id="chargeNo" name="chargeNo" class="form-control">
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div id="toolbar1">
        <button type="button" class="btn btn-primary" onclick="queryDataBtn()"><span class="glyphicon glyphicon-search table-span-icon"></span>查询</button>
        <!-- <button type="button" class="btn btn-success" onclick="addDataByButton()"><span  class="glyphicon glyphicon-plus table-span-icon"></span>新增</button> -->
        <button type="button" class="btn btn-info" onclick="exportDataByButton()"><span  class="glyphicon glyphicon-download-alt table-span-icon"></span>导出</button>
        <button type="button" class="btn btn-success" onclick="backButton()"><span  class="glyphicon glyphicon-arrow-left table-span-icon"></span>返回</button>
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
	    function getQueryInfor(gridHandler, page){
	        //表单数据
	        var queryData = $("#queryForm").serializeArray();
	        //批口号
	        var batchNo = '${batchNo}';
	        //根据不同节点查出不同数据
	        queryData.push({
	        	name : 'currentPage', 
	        	value: page.currentPage
	        });
	        queryData.push({
	        	name: 'limit', 
	        	value: page.limit
	        });
	        queryData.push({
	        	name: 'batchNo', 
	        	value: batchNo
	        });
	        $.ajax({
	            url:"${path}/action/batchChargeRecord/batchChargeRecordDetailData.do",
	            method: "post",
	            data: queryData,
	            dataType: "json"
	        }).success(function(data){
	        	if(data.flag=="false"){
	        		backToBatchChargeRecordList();//跳转到列表页面
	        	    alert(data.errorStr);
	        		return;
	        	}else{
	        		gridHandler(data);
	        	}
	        }).error(function(){
	        	console.error("获取批扣记录信息详情数据异常！");
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
	   			field: 'chargeType',
	   			title: '扣款方式',
	   			align: 'center',
	   		}, {
	   			field: 'chargeChannelName',
	   			title: '渠道',
	   			align: 'center',
	   		}, {
	   			field: 'chargeNo',
	   			title: '扣款编号',
	   			align: 'center',
	   		}, {
	   			field: 'branchOrgName',
	   			title: '分公司名称',
	   		}, {
	   			field: 'loanNo',
	   			title: '合同号',
	   			align: 'center',
	   		}, {
	   			field: 'loanName',
	   			title: '客户姓名',
	   			align: 'center',
	   		}, {
	   			field: 'loanIdCard',
	   			title: '客户身份证号',
	   			align: 'center',
	   		}, {
	   			field: 'phoneNumber',
	   			title: '客户手机号',
	   			align: 'center',
	   		}, {
	   			field: 'paymentDate',
	   			title: '账单日',
	   			align: 'center',
	   		}, {
	   			field: 'billTerm',
	   			title: '还款期数',
	   		}, {
	   			field: 'accountNumber',
	   			title: '客户银行卡号',
	   			align: 'center',
	   		}, {
	   			field: 'amount',
	   			title: '扣款金额',
	   		}, {
	   			field: 'operateName',
	   			title: '操作人姓名',
	   			align: 'center',
	   		}, {
	   			field: 'operateTime',
	   			title: '操作时间',
	   			align: 'center',
/* 	   			formatter:function(value, row, index){
	                return globalDateFormat(value, 'yyyy-mm-dd hh:mi:ss');
	            } */
	   		}, {
	   			field: 'chargeTime',
	   			title: '扣款时间',
	   			align: 'center',
	   		}, {
	   			field: 'chargeMessage',
	   			title: '扣款返回信息',
	   		}, {
	   			field: 'chargeStatus',
	   			title: '扣款状态',
	   			align: 'center',
	   		}, {
	   			field: 'pushFlag',
	   			title: '推送标志位',
	   			align: 'center',
	   		}, {
	   			field: 'pushCount',
	   			title: '推送次数',
	   		}, {
	   			field: 'pushTime',
	   			title: '最近推送时间',
	   			align: 'center',
	   		}, {
	   			field: 'mianBody',
	   			title: '主体',
	   			align: 'center',
	   		}, {
	   			field: 'bankKey',
	   			title: '银行卡键值',
	   		}, {
	   			field: 'createTime',
	   			title: '创建时间',
	   			align: 'center',
	   		}, {
	   			field: 'updateTime',
	   			title: '更新时间',
	   			align: 'center',
	   		}, {
	   			field: 'updateFlag',
	   			title: '是否更新',
	   			align: 'center',
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
	    var gridController = tba.grid("dataInforTable", tbControl);
	    //查询这个表格
	    function queryDataBtn(){
	        gridController.query();
	    };
	    //返回列表页面
	    function backToBatchChargeRecordList(){
	        $.ajax({
	            type: "post",
	            url: "${path}/action/batchChargeRecord/batchChargeRecordList.do",
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
	    }
	    //导出报表
		exportDataByButton = function(){
			//表单数据
	        var queryData = $("#queryForm").serializeArray();
	        //批口号
	        var batchNo = '${batchNo}';
	        queryData.push({
	        	name: 'batchNo', 
	        	value: batchNo
	        });
			//将查询条件拼装成字符串
	        var query = "";
	        for(var i = 0 ; i< queryData.length ; i++){
	        	query += queryData[i].name + "=" + queryData[i].value;
	        	if(i != queryData.length-1){
	        		query += "&";
	        	}
			}
	        window.location.href = "${path}/action/batchChargeRecord/exportBatchChargeRecordDetail.do?"+query;
	    };
	    //返回批扣记录信息页面
	    backButton = function(){
	    	$.ajax({
	            type: "post",
	            url: "${path}/action/batchChargeRecord/batchChargeRecordList.do",
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
	});
</script>

