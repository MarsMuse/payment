<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<style>
    .select2-search--dropdown{display: none;}/** 隐藏下拉框快捷搜索*/
    /*.select2-selection--single{height: 34px !important;} !**设置显示内容的行高和字体高度**!*/
    /*.select2-selection__rendered{height:34px;line-height: 34px !important;}*/
    /*.select2-selection__arrow{top:4px !important;}*/
    /*.select2-container--default{width: 179px !important;}*/
</style>
<div>
    <div class="breadcrumbs" id="breadcrumbs">
        <ul class="breadcrumb">
            <li>
                <i class="icon-home home-icon"></i>
                <a href="#">单扣扣款详情记录</a>
            </li>
        </ul>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">查询条件</h3>
        </div>
        <div class="panel-body">
            <form class="form-inline"  id="queryForm">
                <div class="row">
                    <div class="col-md-3 ">
                        <div class="form-group">
                            <label for="terminalId">平台Id：</label>
                            <input  type="text" id="terminalId"   name="terminalId"   class="form-control"  >
                        </div>
                    </div>

                    <div class="col-md-3">
                        <div class="form-group">
                            <label for="name">姓名：</label>
                            <input  type="text" id="name"   name="name"   class="form-control"  >
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <label for="bankCard">银行卡号：</label>
                            <input  type="text" id="bankCard"   name="bankCard"   class="form-control"  >
                        </div>
                    </div>
                </div>
                <div  class="row">
                    <div class="col-md-3">
                        <div class="form-group">
                            <label for="transNo">交易号：</label>
                            <input  type="text" id="transNo"   name="transNo"   class="form-control"  >
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <label for="loanContractNo">合同号：</label>
                            <input  type="text" id="loanContractNo"   name="loanContractNo"   class="form-control"  >
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <label for="paymentChannel">扣款渠道：</label>
                            <input  type="text" id="paymentChannel"   name="paymentChannel"   class="form-control"  >
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div id="toolbar1">
        <button type="button" class="btn btn-primary" onclick="queryDataByButton()"><span  class="glyphicon glyphicon-search  table-span-icon"></span>查询</button>
        <button type="button" class="btn btn-info" onclick="exportDataByButton()"><span  class="glyphicon glyphicon-download-alt  table-span-icon"></span>导出</button>
    </div>

    <!-- 表格内容-->
    <table  id="dataInforTable"></table>
</div>

<script  type="text/javascript">
    //渲染select2
    $(".js-example-basic-single").select2({width:'179px',height:'34px'});
    $(".js-example-basic-multiple").select2({width:'179px',height:'34px'});

    var rowEntityView ;
    //获取导审核数据
    function  getJudgeInfor(gridHandler, page){
        //表单数据
        var queryData = $("#queryForm").serializeArray();
        //根据不同节点查出不同数据
        queryData.push({name:'currentPage' , value: page.currentPage});
        queryData.push({name:'limit' , value: page.limit});
        //将page替换过去
        /* queryData.push(page); */
        //将page替换过去
        /* queryData.push(page); */
        $.ajax({
            url:"${path}/action/paymentRecord/paymentRecordList.do",
            method:"post",
            data:queryData,
            dataType:"json"
        }).success(gridHandler).error(function(){
            console.error("获取数据异常")
        });
    };
    //表格渲染对象
    var tba = new TableByAjax();
    //渲染参数
    var tbOptions = {
        uniqueId : 'pri_number',
        method : 'post',
        toolbar : '#toolbar1',
        //resizable:true, //注：如果要求启用resizable 一定要将paginationVAlign设置为top，否则在部分版本的chrome中出现table水平滚动轴与分页栏部分重合的情况
        columns : [
            {
                title: '序号',//标题  可不加
                formatter: function (value, row, index) {
                    return    tba.currentOffset+index+1;
                }
            },
            {
                field: 'terminalId',
                title: '扣款平台id'
            },
            {
                field: 'name',
                title: '姓名'
            },
            {
                field: 'bankCard',
                title: '银行卡号'
            },
            {
                field: 'transNo',
                title: '交易号'
            },
            {
                field: 'transType',
                title: '扣款类型',
                formatter:function(value , rowEntity , index){
                    if(value == 1){
                        return "单扣";
                    }else if(value == 2){
                        return "批扣";
                    }else{return value;}
                }
            },
            {
                field: 'loanContractNo',
                title: '合同号'
            },
            {
                field: 'paymentChannel',
                title: '扣款渠道'
            },
            {
                field: 'bankCode',
                title: '扣款银行'
            },
            {
                field: 'transAmount',
                title: '交易金额'
            },
            {
                field: 'paymentCode',
                title: '扣款结果',
                formatter:function(value , rowEntity , index){
                    if(value == 1){
                        return "成功";
                    }else if(value == 2){
                        return "扣款中";
                    }else if(value == 3){
                        return "扣款失败";
                    }else{return value;}
                }
            },
            {
                field: 'paymentDesc',
                title: '扣款描述',
                visible:false
            },
            {
                field: 'paymentTime',
                title: '扣款时间'
            },
            {
                field: 'createTime',
                title: '创建时间'
            },
            {
                field: 'updateTime',
                title: '修改时间'
            }
        ]
    };

    //表格初始化配置
    var tbControl = {
        dataHandler :getJudgeInfor,
        options:tbOptions
    };
    //获取到表格控制器
    var gridController = tba.grid("dataInforTable" , tbControl);

    //查询这个表格
    function queryDataByButton(){
        gridController.query();
    };

    function exportDataByButton(){
        //表单数据
        var form = document.forms[0];
        form.action = "${pageContext.request.contextPath}/action/paymentRecord/exportData.do";
        form.submit();
    }

</script>
