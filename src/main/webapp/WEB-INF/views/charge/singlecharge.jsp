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
                <a href="#">单扣扣款记录</a>
            </li>
        </ul>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">查询条件</h3>
        </div>
        <div class="panel-body">
            <form class="form-inline" method="post"  id="queryForm">
                <div class="row">
                    <div class="col-md-3 ">
                        <div class="form-group">
                            <label for="platform_code">平台编码：</label>
                            <input  type="text" id="platform_code"   name="platform_code"   class="form-control"  >
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <label for="charge_status">扣款状态：</label>
                            <select id="charge_status"  name="charge_status" class="js-example-basic-single">
                                <option value="">请选择</option>
                                <option value="0">扣款中</option>
                                <option value="1">扣款成功</option>
                                <option value="2">扣款失败</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3 ">
                        <div class="form-group">
                            <label for="mian_body">主体信息：</label>
                            <select id="mian_body"  name="mian_body" class="js-example-basic-single">
                                <option value="">请选择</option>
                                <option value="ZH">正合</option>
                                <option value="HT">鸿特</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div  class="row">

                    <div class="col-md-3 ">
                        <div class="form-group">
                            <label for="push_flag">推送状态：</label>
                            <select id="push_flag"  name="push_flag" class="js-example-basic-single">
                                <option value="">请选择</option>
                                <option value="0">未推送</option>
                                <option value="1">推送成功</option>
                                <option value="2">推送失败</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3 ">
                        <div class="form-group">
                            <label for="update_flag">是否更新：</label>
                            <select id="update_flag"  name="update_flag" class="js-example-basic-single">
                                <option value="">请选择</option>
                                <option value="0">未更新</option>
                                <option value="1">已更新</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-3 ">
                        <div class="form-group">
                            <label for="send_flag">发送状态：</label>
                            <select id="send_flag"  name="send_flag" class="js-example-basic-single">
                                <option value="">请选择</option>
                                <option value="0">发送中</option>
                                <option value="1">发送成功</option>
                                <option value="2">发送失败</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div  class="row">
                    <div class="col-md-3 ">
                        <div class="form-group">
                            <label for="loan_no">合同号：</label>
                            <input  type="text" id="loan_no"   name="loan_no"   class="form-control"  >
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <label for="single_no">交易流水号：</label>
                            <input  type="text" id="single_no"   name="single_no"   class="form-control"  >
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <label for="charge_channel_code">渠道名称：</label>
                            <input  type="text" id="charge_channel_code"   name="charge_channel_code"   class="form-control"  >
                        </div>
                    </div>
                    <div class="col-md-3 ">
                        <div class="form-group">
                            <label for="operate_name">操作人：</label>
                            <input  type="text" id="operate_name"   name="operate_name"   class="form-control"  >
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3">
                        <div class="form-group">
                            <label for="createTimeStart">创建时间：</label>
                            <div class="input-group date form_date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="paymentBeginDate" data-link-format="yyyy-mm-dd">
                                <input class="form-control" id="createTimeStart" name="createTimeStart" size="16" type="text" style="width: 140px;" readonly>
                             <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                        </div>
                    </div>
                    </div>
                    <div class="col-md-3">
                         <div class="form-group">
                            <label for="createTimeEnd">至：</label>
                            <div class="input-group date form_date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="paymentEndDate" data-link-format="yyyy-mm-dd">
                            <input class="form-control" id="createTimeEnd" name="createTimeEnd" size="16" type="text" style="width: 140px;" readonly>
                            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                         </div>
                    </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <label for="updateTimeStart">更新时间：</label>
                            <div class="input-group date form_date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="chargeBeginTime" data-link-format="yyyy-mm-dd">
                                <input class="form-control" id="updateTimeStart" name="updateTimeStart" size="16" type="text" style="width: 140px;" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <label for="updateTimeEnd">至：</label>
                            <div class="input-group date form_date col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="chargeEndTime" data-link-format="yyyy-mm-dd">
                                <input class="form-control" id="updateTimeEnd" name="updateTimeEnd" size="16" type="text" style="width: 140px;" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div id="toolbar1">
        <button type="button" class="btn btn-primary" onclick="queryDataByButton()"><span  class="glyphicon glyphicon-search  table-span-icon"></span>查询</button>
        <button type="button" class="btn btn-info" onclick="exportDataByButton()"><span  class="glyphicon glyphicon-download-alt  table-span-icon"></span>导出</button>
        <button type="button" class="btn btn-info" onclick="pushMessage()"><span  class="glyphicon glyphicon-send table-span-icon"></span>推送数据</button>
        <button type="button" class="btn btn-info" onclick="pushQuery()"><span  class="glyphicon glyphicon-send table-span-icon"></span>更新扣款中数据</button>
    </div>

    <!-- 表格内容-->
    <table  id="dataInforTable"></table>
</div>

<script  type="text/javascript">
    //渲染select2
    $(".js-example-basic-single").select2({width:'179px',height:'34px'});
//    $(".js-example-basic-multiple").select2({width:'179px',height:'34px'});
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
            url:"${path}/action/charge/singlechargelist.do",
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
                width:"60px",
                formatter: function (value, row, index) {
                    return    tba.currentOffset+index+1;
                }
            },
            {
                field: 'platform_code',
                title: '平台编码'
            },
            {
                field: 'charge_channel_code',
                title: '渠道名称'
            },
            {
                field: 'single_no',
                title: '交易流水号'
            },
            {
                field: 'branch_org_name',
                title: '分公司名称'
            },{
                field: 'loan_no',
                title: '合同号'
            },{
                field: 'loan_name',
                title: '客户姓名'
            },{
                field: 'loan_id_card',
                title: '客户身份证号'
            },
            {
                field: 'phone_number',
                title: '客户手机号'
            },
            {
                field: 'payment_date',
                title: '账单日'
            },
            {
                field: 'bill_term',
                title: '还款期数'
            },
            {
                field: 'account_number',
                title: '客户银行卡号'
            },
            {
                field: 'amount',
                title: '已扣金额'
            },
            {
                field: 'real_amount',
                title: '应扣金额'
            },
            {
                field: 'operate_name',
                title: '操作人'
            },
            {
                field: 'operate_time',
                title: '操作时间'
            },
            {
                field: 'charge_time',
                title: '扣款日期'
            },
            {
                field: 'charge_message',
                title: '扣款返回信息'
            },
            {
                field: 'charge_status',
                title: '扣款状态',
                formatter:function(value , rowEntity , index){
                    if(value == 0 ){
                        return "扣款中";
                    }else if(value == 1){
                        return "扣款成功";
                    }else if(value == 2){
                        return "扣款失败";
                    }else{return value;}
                }
            },
            {
                field: 'push_flag',
                title: '推送状态',
                formatter:function(value , rowEntity , index){
                    if(value == 0 ){
                        return "未推送";
                    }else if(value == 1){
                        return "推送成功";
                    }else if(value == 2){
                        return "推送失败";
                    }else{return value;}
                }
            },
            {
                field: 'push_count',
                title: '推送次数'
            },
            {
                field: 'flag',
                title: '是否有效',
                formatter:function(value , rowEntity , index){
                    if(value == 0 ){
                        return "无效";
                    }else if(value == 1){
                        return "有效";
                    }else{return value;}
                }
            },
            {
                field: 'mian_body',
                title: '主体'
            },
            {
                field: 'bank_key',
                title: '银行名称'
            },
            {
                field: 'update_flag',
                title: '是否更新',
                formatter:function(value , rowEntity , index){
                    if(value == 0 ){
                        return "未更新";
                    }else if(value == 1){
                        return "已更新";
                    }else{return value;}
                }
            },
            {
                field: 'push_time',
                title: '最近推送时间'
            },
            {
                field: 'send_flag',
                title: '发送状态',
                formatter:function(value , rowEntity , index){
                    if(value == 0 ){
                        return "发送中";
                    }else if(value == 1){
                        return "发送成功";
                    }else if(value == 2){
                        return "发送失败";
                    }else{return value;}
                }
            },
            {
                field: 'create_time',
                title: '创建时间'
            },
            {
                field: 'update_time',
                title: '更新时间'
            },{
                field: 'cut_no',
                title: '分单号'
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
        form.action = "${pageContext.request.contextPath}/action/charge/exportData.do";
        form.submit();
    }
    function pushQuery() {
            $.ajax({
                url:"${path}/action/charge/pushQuery.do",
                method:"post",
                data:"",
                dataType:"json"
            }).success(function(data,flag){
                alert(data.message);

            }).error(function(){
                console.error("第三方查询异常")
            });
    }
    function pushMessage(){
        $.ajax({
            url:"${path}/action/charge/pushMessage.do",
            method:"post",
            data:"",
            dataType:"json"
        }).success(function(data,flag){
            alert(data.message);

        }).error(function(){
            console.error("推送异常")
        });
    }


</script>
