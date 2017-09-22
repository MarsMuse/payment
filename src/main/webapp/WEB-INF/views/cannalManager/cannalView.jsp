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
                <a href="#">渠道管理</a>
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
                            <label for="financingChannel">渠道Id：</label>
                            <input  type="text" id="financingChannel"   name="financingChannel"   class="form-control"  >
                        </div>
                    </div>

                    <div class="col-md-3">
                        <div class="form-group">
                            <label for="channelName">渠道名称：</label>
                            <input  type="text" id="channelName"   name="channelName"   class="form-control"  >
                        </div>
                    </div>
                    <div class="col-md-3 ">
                        <div class="form-group">
                            <label for="bankCode">银行编码：</label>
                            <input  type="text" id="bankCode"   name="bankCode"   class="form-control"  >
                        </div>
                    </div>
                </div>
                <div  class="row">
                    <div class="col-md-3 ">
                        <div class="form-group">
                            <label for="bankName">银行名称：</label>
                            <input  type="text" id="bankName"   name="bankName"   class="form-control"  >
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="form-group">
                            <label for="mainBody">主体：</label>
                            <select id="mainBody"  name="mainBody" class="js-example-basic-single">
                                <option value="">请选择</option>
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
        <button type="button" class="btn btn-primary" onclick="queryDataByButton()"><span  class="glyphicon glyphicon-search  table-span-icon"></span>查询</button>
        <button type="button" class="btn btn-primary"  data-toggle="modal" data-target="#myModal"><span  class="glyphicon glyphicon-plus  table-span-icon"></span>新增</button>
        <!-- <button type="button" class="btn btn-info" onclick="exportDataByButton()"><span  class="glyphicon glyphicon-download-alt  table-span-icon"></span>导出</button> -->
    </div>

    <!-- 模态框（Modal） -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">添加渠道</h4>
                </div>
                <div class="modal-body">
                    <form class="form-inline"  id="addBankKeyInfo">
                        <div class="form-group add-modal-form-fm">
                            <label for="financingChannel" style="width: 150px">渠道Id：</label>
                            <input  type="text" id="financingChannel"   name="financingChannel" style="" class="form-control add-modal-input-fm" autocomplete="off" required >
                            <span class="glyphicon glyphicon-asterisk text-danger"></span>
                        </div>

                        <div class="form-group add-modal-form-fm">
                            <label for="channelName" style="width: 150px">渠道名称：</label>
                            <input  type="text" id="channelName"   name="channelName"     class="form-control add-modal-input-fm"  autocomplete="off" required>
                            <span class="glyphicon glyphicon-asterisk text-danger"></span>
                        </div>
                        <div class="form-group add-modal-form-fm">
                            <label for="bankKey"  style="width: 150px">银行key值：</label>
                            <input  type="text" id="bankKey"   name="bankKey"   class="form-control add-modal-input-fm"  autocomplete="off" required>
                            <span class="glyphicon glyphicon-asterisk text-danger"></span>
                        </div>
                        <div class="form-group add-modal-form-fm">
                            <label for="bankCode"  style="width: 150px">扣款请求时银行编码：</label>
                            <input  type="text" id="bankCode"   name="bankCode"   class="form-control add-modal-input-fm"  autocomplete="off" required>
                            <span class="glyphicon glyphicon-asterisk text-danger"></span>
                        </div>

                        <div class="form-group add-modal-form-fm">
                            <label for="bankName"  style="width: 150px">银行名称：</label>
                            <input  type="text" id="bankName"   name="bankName"   class="form-control add-modal-input-fm" autocomplete="off"  >
                            <span class="glyphicon glyphicon-asterisk text-danger"></span>
                        </div>

                        <div class="form-group add-modal-form-fm">
                            <label for="singleAmountLimit"  style="width: 150px">单次扣款限额：</label>
                            <input  type="text" id="singleAmountLimit"   name="singleAmountLimit"   class="form-control add-modal-input-fm" autocomplete="off"  >
                            <span class="glyphicon glyphicon-asterisk text-danger"></span>
                        </div>


                        <div class="form-group add-modal-form-fm">
                            <label for="dayAmountLimit"  style="width: 150px">每日扣款金额限制：</label>
                            <input  type="text" id="dayAmountLimit"   name="dayAmountLimit"   class="form-control add-modal-input-fm" autocomplete="off"  >
                            <span class="glyphicon glyphicon-asterisk text-danger"></span>
                        </div>


                        <div class="form-group add-modal-form-fm">
                            <label for="dayCountLimit"  style="width: 150px">每日扣款次数限制：</label>
                            <input  type="text" id="dayCountLimit"   name="dayCountLimit"   class="form-control add-modal-input-fm" autocomplete="off"  >
                            <span class="glyphicon glyphicon-asterisk text-danger"></span>
                        </div>

                        <div class="form-group add-modal-form-fm">
                            <label for="isEnable"  style="width: 150px">是否启用：</label>
                            <select class="selectpicker add-modal-input-fm" id="isEnable"  name="isEnable">
                                <option value="0">否</option>
                                <option value="1">是</option>
                            </select>

                        </div>

                        <div class="form-group add-modal-form-fm">
                            <label for="deductLevel"  style="width: 150px">扣款优先级：</label>
                            <input  type="text" id="deductLevel"   name="deductLevel"   class="form-control add-modal-input-fm" autocomplete="off"  >
                            <span class="glyphicon glyphicon-asterisk text-danger"></span>
                        </div>

                        <div class="form-group add-modal-form-fm">
                            <label for="singleAmountLimitBatch"  style="width: 150px">批扣单次扣款金额限制：</label>
                            <input  type="text" id="singleAmountLimitBatch"   name="singleAmountLimitBatch"   class="form-control add-modal-input-fm" autocomplete="off"  >
                            <span class="glyphicon glyphicon-asterisk text-danger"></span>
                        </div>
                        <div class="form-group add-modal-form-fm">
                            <label for="dayAmountLimitBatch"  style="width: 150px">批扣每日扣款金额限制：</label>
                            <input  type="text" id="dayAmountLimitBatch"   name="dayAmountLimitBatch"   class="form-control add-modal-input-fm" autocomplete="off"  >
                            <span class="glyphicon glyphicon-asterisk text-danger"></span>
                        </div>
                        <div class="form-group add-modal-form-fm">
                            <label for="mainBody"  style="width: 150px">主体：</label>
                            <select class="selectpicker add-modal-input-fm" id="mainBody"  name="mainBody">
                                <option value="ZH">ZH</option>
                                <option value="HT">HT</option>
                            </select>

                        </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="submitBankKeyInfo()">确定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

    <!-- 表格内容-->
    <table  id="dataInforTable"></table>
</div>
<script  type="text/javascript">
    //渲染select2
    $(".js-example-basic-single").select2({width:'179px',height:'34px'});
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
            url:"${path}/action/cannalManager/getCannallist.do",
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
                field: 'financingChannel',
                title: '扣款渠道ID'
            },
            {
                field: 'channelName',
                title: '渠道名称'
            },
            {
                field: 'bankCode',
                title: '扣款请求时银行编码'
            },
            {
                field: 'bankName',
                title: '银行名称'
            },{
                field: 'singleAmountLimit',
                title: '单次扣款限额'
            },{
                field: 'dayAmountLimit',
                title: '每日扣款金额限制'
            },{
                field: 'dayCountLimit',
                title: '每日扣款次数限制'
            },
            {
                field: 'deductLevel',
                title: '扣款优先级'
            },{
                field: 'createUserId',
                title: '用户名'
            },
            {
                field: 'mainBody',
                title: '主体'
            },
            {
                field: 'singleAmountLimitBatch',
                title: '批扣单次扣款金额限制'
            },
            {
                field: 'dayAmountLimitBatch',
                title: '批扣每日扣款金额限制'
            },
            {
                field: 'createTime',
                title: '创建时间'
            },
            {
                field: 'id',
                title: '操作',
                width:"248px",
                formatter:function(value , rowEntity , index){
                    var reusltVal ='<button type="button" class="btn btn-success  btn-sm  "  ' +
                                'onclick="updateKey(\''+rowEntity.priNumber+'\',\''+rowEntity.financingChannel+'\',\''+rowEntity.channelName+'\',\''+rowEntity.bankName+'\',\''+rowEntity.singleAmountLimit+'\',\''+rowEntity.dayAmountLimit+'\'' +
                                ',\''+rowEntity.dayCountLimit+'\',\''+rowEntity.deductLevel+'\',\''+rowEntity.singleAmountLimitBatch+'\',\''+rowEntity.dayAmountLimitBatch+'\',\''+rowEntity.mainBody+'\')"   ' +
                                            'style="margin-right:12px;"><span  class="glyphicon glyphicon-screenshot  table-span-icon"></span>修改</button>' ;
                    if(rowEntity.isEnable == 0){
                        reusltVal += '<button type="button" class="btn btn-warning  btn-sm  "  onclick="updateState(\''+rowEntity.priNumber+'\',\'1\')"   style="margin-right:12px;"><span  class="glyphicon glyphicon-tint  table-span-icon"></span>启用</button>' ;
                    }else{
                        reusltVal += '<button type="button" class="btn btn-danger  btn-sm   "  onclick="updateState(\''+rowEntity.priNumber+'\',\'0\')"   style="margin-right:12px;"><span  class="glyphicon glyphicon-tint  table-span-icon"></span>禁用</button>' ;
                    }
                    reusltVal += '<button type="button" class="btn btn-danger  btn-sm   "  onclick="deleteCannal(\''+rowEntity.priNumber+'\',\'0\')"   style="margin-right:12px;"><span  class="glyphicon glyphicon-tint  table-span-icon"></span>删除</button>' ;
                    return reusltVal;
                }
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
    //修改                     console.log(rowEntity.)
    function  updateKey(pri_number,financingChannel ,channelName,bankName,singleAmountLimit,dayAmountLimit,dayCountLimit,deductLevel
    ,singleAmountLimitBatch,dayAmountLimitBatch,mainBody){
        console.log(pri_number);
         layer.open({
            title:'渠道信息修改',
            type: 1,
            closeBtn: 1,
            area:['480px','460px'],
            shadeClose: true,
            move :false,
            resize :false,
            btn: ['修改', '取消'],
            btnAlign: 'r',
            yes: function(index, layero){
                var isok = submitKeyInfo(pri_number);
                if (isok == null)
                    layer.close(index);
                return false;
            },
            cancel: function(index, layero){
                layer.close(index);
                return false;
            },
             content: '<div  style="padding-top:20px; ">                                                                                                                             '+
             '	<form class="form-inline"  id="updateKeyForm">                                                                                                             '+
             '		<div class="form-group" >                                                                                                                              '+
             '			<label for="financingChannel">渠道ID：</label>                                                                                                     '+
             '			<input  type="text" id="financingChannel" style="width: 280px;"  readonly  value="'+financingChannel+'" name="financingChannel"   class="form-control" >      '+
             '		</div>                                                                                                                                                 '+
             '		<div class="form-group">                                                                                                                               '+
             '			<label for="channelName">渠道名称：</label>                                                                                                       '+
             '			<input  type="text" id="channelName" style="width: 280px;"    readonly  value="'+channelName+'" name="channelName"   class="form-control" >                    '+
             '		</div>                                                                                                                                                 '+
             '		<div class="form-group">                                                                                                                               '+
             '			<label for="bankName">银行名称:</label>                                                                                                            '+
             '			<input  type="text" id="bankName" style="width: 280px;"   readonly  value="'+bankName+'" name="bankName"   class="form-control" >                             '+
             '		</div>                                                                                                                                                 '+
             '			<div class="form-group">                                                                                                                           '+
             '			<label for="singleAmountLimit">单次扣款限额:</label>                                                                                              '+
             '			<input  type="text" id="singleAmountLimit" style="width: 280px;"  value="'+singleAmountLimit+'" name="singleAmountLimit"   class="form-control" >  '+
             '		</div>                                                                                                                                                 '+
             '			<div class="form-group">                                                                                                                           '+
             '			<label for="dayAmountLimit">每日扣款金额限制:</label>                                                                                             '+
             '			<input  type="text" id="dayAmountLimit" style="width: 280px;"  value="'+dayAmountLimit+'" name="dayAmountLimit"   class="form-control" >           '+
             '		</div>                                                                                                                                                 '+
             '		<div class="form-group">                                                                                                                               '+
             '			<label for="dayCountLimit">每日扣款次数限制:</label>                                                                                              '+
             '			<input  type="text" id="dayCountLimit" style="width: 280px;"  value="'+dayCountLimit+'" name="dayCountLimit"   class="form-control" >              '+
             '		</div>	                                                                                                                                               '+
             '		<div class="form-group">                                                                                                                               '+
             '			<label for="deductLevel">扣款优先级:</label>                                                                                                      '+
             '			<input  type="text" id="deductLevel" style="width: 280px;"  value="'+deductLevel+'" name="deductLevel"   class="form-control" >                    '+
             '		</div>	                                                                                                                                               '+
             '		<div class="form-group">                                                                                                                           '+
             '			<label for="singleAmountLimitBatch">批扣单次扣款</br>金额限制:</label>                                                                                             '+
             '			<input  type="text" id="singleAmountLimitBatch" style="width: 280px;"  value="'+singleAmountLimitBatch+'" name="singleAmountLimitBatch"   class="form-control" >'+
             '		</div>                                                                                                                                                 '+
             '		<div class="form-group">                                                                                                                               '+
             '			<label for="dayAmountLimitBatch">批扣每日扣款</br>金额限制:</label>                                                                                              '+
             '			<input  type="text" id="dayAmountLimitBatch" style="width: 280px;"  value="'+dayAmountLimitBatch+'" name="dayAmountLimitBatch"   class="form-control" >              '+
             '		</div>	                                                                                                                                               '+
             '		<div class="form-group">                                                                                                                               '+
             '			<label for="mainBody">主体:</label>                                                                                                      '+
             '          <select class="selectpicker add-modal-input-fm" id="mainBody"  name="mainBody" style="width:280px !important;"> '+
             '              <option value="ZH" '+(mainBody == "ZH" ? 'selected':'')+'>ZH</option>'+
             '              <option value="HT" '+(mainBody == "HT" ? 'selected':'') +' >HT</option>'+
             '          </select>'+
             '		</div>			                                                                                                                                       '+
             '	</form>                                                                                                                                                    '+
             '</div>                                                                                                                                                        '
        });
    };
    //更新状态
    function updateState(id,isEnable){
       if(id != '')
        $.ajax({
            url:"${path}/action/cannalManager/updateCannalState.do",
            method:"post",
            data:{id:id,isEnable:isEnable},
            dataType:"json"
        }).success(function(data){
            if(data == '1'){
                //layer.msg('渠道更新成功');
                queryDataByButton();
            }else{
                layer.msg('渠道更新异常');
            }
        }).error(function(){
            console.error("网络异常");
        });
    }
    //删除当前行
    function deleteCannal(id){
        layer.msg('你确定 <b>删除</b> 该条记录？', {
            time: 0
            ,shade:0.4
            ,shadeClose:true
            ,btn: ['确定', '取消']
            ,yes: function(index){
                layer.close(index);
                if(id != '')
                    $.ajax({
                        url:"${path}/action/cannalManager/deleteCannal.do",
                        method:"post",
                        data:{id:id},
                        dataType:"json"
                    }).success(function(data){
                        if(data == '1'){
                            layer.msg('渠道删除成功');
                            queryDataByButton();
                        }else{
                            layer.msg('渠道删除异常');
                        }
                    }).error(function(){
                        console.error("网络异常");
                    });
            }
            ,no:function () {
                layer.close(index);
                return ;
            }
        });

    }
    //添加提交定单
    function submitBankKeyInfo() {
        var queryData = $("#addBankKeyInfo").serializeArray();
        var channlInfo= {};
        var isBooleanCheck = true;//判断是否可以执行
        $.each(queryData, function(i, field){
            console.log(i);
            var tempName = field.name;
            var tempValue = $.trim(field.value);
            if(tempValue == ''){
                isBooleanCheck = false;
//                alert($($('label[for="'+tempName+'"]')[0]).text()+"数据不能为空");
                return false;
            }
            channlInfo[tempName] = field.value;
        });
        isBooleanCheck = newSubmitValidate(channlInfo);
        if(isBooleanCheck){
            //获取到信息
            $.ajax({
                url:"${path}/action/cannalManager/addCannal.do",
                method:"post",
                data:{data:JSON.stringify(channlInfo)},
                dataType:"json"
            }).success(function(data){
                if(data == '1'){
                    layer.msg('渠道添加成功');
                    queryDataByButton();
                    $('#myModal').modal('hide');
                }else{
                    layer.msg('渠道添加异常');
                }
            }).error(function(){
                console.error("网络异常");
            });
        }

    }
    //修改提交表单
     function submitKeyInfo(id){
         //表单数据
         var queryData = $("#updateKeyForm").serializeArray();
         queryData.push({name:'priNumber' , value: id});
         console.log(queryData);
         if(compare(queryData)){
             var obj = {};
             $.each(queryData,function(i,v){obj[v.name] = v.value;})
             if(!formValidate(obj)){return false;}
             $.ajax({
                 url:"${path}/action/cannalManager/updateCannal.do",
                 method:"post",
                 data:{data:JSON.stringify(obj)},
                 dataType:"json"
             }).success(function(data){
                 if(data == '1'){
                     layer.msg('渠道更新成功');
                     queryDataByButton();
                     $('#myModal').modal('hide');
                 }else{
                     layer.msg('渠道更新异常');
                 }
             }).error(function(){
                 console.error("网络异常");
             });
         }else{
         }
     }
     //比较两个对象  暂时未做  有待优化
    function compare(queryDatarowEntityView){
         var isBoolean = true;
        queryDatarowEntityView.forEach(function(item){
//            if($.trim(rowEntityView[item.name]) != $.trim(item.value)){
//                console.log([item.name);
//                console.log([item.value);
//                isBoolean = false;
//                return false;
//            }
        });
        return isBoolean;
//        return queryData.forEach(function(item){ console.log(rowEntityView[item.name]);console.log(item.value);})
//        queryData.forEach(function(item){
//            if(rowEntityView[item.name] != item.value)return false;return true;});
//        queryData.forEach(function(item){ if(rowEntityView[item.name] != item.value){ console.log(item.name+""+item.value+":"+rowEntityView[item.name]);return false;}return true;})
    }

    //修改和添加渠道字段长度验证
    function formValidate(obj){
        var title = "";
        var messate = "";
        var maxNum = 9999999999999.99;  //number(15.2)
        if(isNull(obj)){
            console.log("验证的参数不能为null,参数值："+obj);
            layer.msg("参数验证失败");
            return false;
        }else{
            if(!isNull(obj.singleAmountLimit)) {
                var formatReg = /^((-1)|([0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
                if (!formatReg.test(obj.singleAmountLimit)) {
                    layer.msg('单次扣款限额格式错误,只能写入-1或者正数,两位小数');
                    return false;
                }
                var val = parseFloat(obj.singleAmountLimit);
                if(val <= 0 && val != -1){
                    layer.msg('单次扣款限额必须大于0');
                    return false;
                }
                if(val > maxNum){
                    layer.msg('单次扣款限额超出最大范围,范围值：1~'+maxNum);
                    return false;
                }
            }else{
                layer.msg('单次扣款限额不能为空');
                return false;
            }
            if(!isNull(obj.dayAmountLimit)) {
                var formatReg = /^((-1)|([0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
                if (!formatReg.test(obj.dayAmountLimit)) {
                    layer.msg('每日扣款金额格式错误,只能写入-1或者正数,两位小数');
                    return false;
                }
                var val = parseFloat(obj.dayAmountLimit);
                if(val <= 0 && val != -1){
                    layer.msg('每日扣款金额必须大于0');
                    return false;
                }
                if(val > maxNum){
                    layer.msg('每日扣款金额超出最大范围,范围值：1~'+maxNum);
                    return false;
                }
            }else{
                layer.msg('每日扣款金额不能为空');
                return false;
            }
            if(!isNull(obj.dayCountLimit)){
                var formatReg = /^((-1)|([0-9]*))$/;
                if (!formatReg.test(obj.dayCountLimit)) {
                    layer.msg('每日扣款次数限制格式错误,只能写入-1或者正整数');
                    return false;
                }
                var val = parseInt(obj.dayCountLimit);
                if(val <= 0){
                    layer.msg('每日扣款次数限制必须大于0');
                    return false;
                }
            }else{
                layer.msg('每日扣款次数限制不能为空');
                return false;
            }
//            var val1 = parseInt(obj.dayCountLimit);//次数
//            var val2 = parseFloat(obj.singleAmountLimit);//每次限额
//            var val3 = parseFloat(obj.dayAmountLimit); //单日限额
//            if(val3 < val2){
//                layer.msg('每日扣款金额限制不能小于单次扣款限额');
//                return false;
//            }
//            if((val3 / val2) > val1){
//                layer.msg('每日扣款次数限制应为大于'+parseInt(val3 / val2));
//                return false;
//            }
            if(!isNull(obj.deductLevel)){
                var formatReg = /^(([0-9]*))$/;
                if (!formatReg.test(obj.deductLevel)) {
                    layer.msg('扣款优先级格式错误,只能正整数');
                    return false;
                }
            }else{
                layer.msg('扣款优先级不能为空');
                return false;
            }

            if(!isNull(obj.singleAmountLimitBatch)) {
                var formatReg = /^((-1)|([0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
                if (!formatReg.test(obj.singleAmountLimitBatch)) {
                    layer.msg('批扣单次扣款限额格式错误,只能写入-1或者正数,两位小数');
                    return false;
                }
                var val = parseFloat(obj.singleAmountLimitBatch);
                if(val <= 0 && val != -1){
                    layer.msg('批扣单次扣款限额必须大于0');
                    return false;
                }
                if(val > maxNum){
                    layer.msg('批扣单次扣款限额超出最大范围,范围值：1~'+maxNum);
                    return false;
                }
            }else{
                layer.msg('批扣单次扣款限额不能为空');
                return false;
            }
            if(!isNull(obj.dayAmountLimitBatch)) {
                var formatReg = /^((-1)|([0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
                if (!formatReg.test(obj.dayAmountLimitBatch)) {
                    layer.msg('批扣每日扣款金额格式错误,只能写入-1或者正数,两位小数');
                    return false;
                }
                var val = parseFloat(obj.dayAmountLimitBatch);
                if(val <= 0 && val != -1){
                    layer.msg('批扣每日扣款金额必须大于0');
                    return false;
                }
                if(val > maxNum){
                    layer.msg('批扣每日扣款金额超出最大范围,范围值：1~'+maxNum);
                    return false;
                }
                if(parseFloat(obj.singleAmountLimitBatch) > val){
                    layer.msg('批扣每日扣款金额不能小于批扣单次扣款限额');
                    return false;
                }
            }else{
                layer.msg('批扣每日扣款金额不能为空');
                return false;
            }
        }
        return true;
    }

    //null判断
    function isNull(obj){
        if(obj == null){
            return true;
        }else{
            return false;
        }
    }

    //新增提交表单字段验证
    function newSubmitValidate(obj){
        var maxNum = 9999999999999.99;  //number(15.2)
        if(isNull(obj)){
            layer.msg("参数验证失败");
            console.log("验证的参数不能为null,参数值："+obj);
            return false;
        }else {
            if (!isNull(obj.financingChannel)) {
                if(!fieldEmptyVal("渠道Id",obj.financingChannel,30)){return false;}
                var fmtReg = /^(([0-9A-Za-z]*))$/;
                if(!fmtReg.test(obj.financingChannel)){
                    layer.msg('渠道Id只能为字母和数字或者字母数字组合');
                    return false;
                }
            } else {
                layer.msg('渠道Id不能为空');
                return false;
            }
            if (!isNull(obj.channelName)) {
                if(!fieldEmptyVal("渠道名称",obj.channelName,30)){return false;}
            } else {
                layer.msg('渠道名称不能为空');
                return false;
            }
            if (!isNull(obj.bankKey)) {
                if(!fieldEmptyVal("银行key值",obj.bankKey,2)){return false;}
                var fmtReg = /^(([0-9]*))$/;
                if(!fmtReg.test(obj.bankKey)){
                    layer.msg('银行key值只能为正整数');
                    return false;
                }
            } else {
                layer.msg('银行key值不能为空');
                return false;
            }
            if (!isNull(obj.bankCode)) {
                if(!fieldEmptyVal("银行编码",obj.bankCode,30)){return false;}
                var fmtReg = /^(([0-9A-Za-z]*))$/;
                if(!fmtReg.test(obj.bankCode)){
                    layer.msg('银行编码只能为字母和数字或者字母数字组合');
                    return false;
                }
            } else {
                layer.msg('银行编码不能为空');
                return false;
            }
            if (!isNull(obj.bankName)) {
                if(!fieldEmptyVal("银行名称",obj.bankName,30)){return false;}
            } else {
                layer.msg('银行名称不能为空');
                return false;
            }
            if (!isNull(obj.singleAmountLimit)) {
                var formatReg = /^((-1)|([0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
                if (!formatReg.test(obj.singleAmountLimit)) {
                    layer.msg('单次扣款限额格式错误,只能写入-1或者正数,两位小数');
                    return false;
                }
                var val = parseFloat(obj.singleAmountLimit);
                if(val <= 0 && val != -1){
                    layer.msg('单次扣款限额必须大于0');
                    return false;
                }
                if(val > maxNum){
                    layer.msg('单次扣款限额超出最大范围,范围值：1~'+maxNum);
                    return false;
                }
            } else {
                layer.msg('单次扣款限额不能为空');
                return false;
            }
            if (!isNull(obj.dayAmountLimit)) {
                var formatReg = /^((-1)|([0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
                if (!formatReg.test(obj.dayAmountLimit)) {
                    layer.msg('每日扣款金额限制格式错误,只能写入-1或者正数,两位小数');
                    return false;
                }
                var val = parseFloat(obj.dayAmountLimit);
                if(val <= 0 && val != -1){
                    layer.msg('每日扣款金额限制必须大于0');
                    return false;
                }
                if(val > maxNum){
                    layer.msg('每日扣款金额限制超出最大范围,范围值：1~'+maxNum);
                    return false;
                }
            } else {
                layer.msg('每日扣款金额限制不能为空');
                return false;
            }
            if(!isNull(obj.dayCountLimit)){
                var formatReg = /^((-1)|([0-9]*))$/;
                if (!formatReg.test(obj.dayCountLimit)) {
                    layer.msg('每日扣款次数限制格式错误,只能写入-1或者正整数');
                    return false;
                }
                var val = parseInt(obj.dayCountLimit);
                if(val <= 0){
                    layer.msg('每日扣款次数限制必须大于0');
                    return false;
                }
            }else{
                layer.msg('每日扣款次数限制不能为空');
                return false;
            }
//            var val1 = parseInt(obj.dayCountLimit);//次数
//            var val2 = parseFloat(obj.singleAmountLimit);//每次限额
//            var val3 = parseFloat(obj.dayAmountLimit); //单日限额
//            if(val3 < val2){
//                layer.msg('每日扣款金额限制不能小于单次扣款限额');
//                return false;
//            }
//            if((val3 / val2) > val1){
//                layer.msg('每日扣款次数限制应为大于'+parseInt(val3 / val2));
//                return false;
//            }
            if(!isNull(obj.deductLevel)){
                var formatReg = /^(([0-9]*))$/;
                if (!formatReg.test(obj.deductLevel)) {
                    layer.msg('扣款优先级格式错误,只能正整数');
                    return false;
                }
            }else{
                layer.msg('扣款优先级不能为空');
                return false;
            }
            if(!isNull(obj.singleAmountLimitBatch)) {
                var formatReg = /^((-1)|([0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
                if (!formatReg.test(obj.singleAmountLimitBatch)) {
                    layer.msg('批扣单次扣款限额格式错误,只能写入-1或者正数,两位小数');
                    return false;
                }
                var val = parseFloat(obj.singleAmountLimitBatch);
                if(val <= 0 && val != -1){
                    layer.msg('批扣单次扣款限额必须大于0');
                    return false;
                }
                if(val > maxNum){
                    layer.msg('批扣单次扣款限额超出最大范围,范围值：1~'+maxNum);
                    return false;
                }
            }else{
                layer.msg('批扣单次扣款限额不能为空');
                return false;
            }
            if(!isNull(obj.dayAmountLimitBatch)) {
                var formatReg = /^((-1)|([0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
                if (!formatReg.test(obj.dayAmountLimitBatch)) {
                    layer.msg('批扣每日扣款金额格式错误,只能写入-1或者正数,两位小数');
                    return false;
                }
                var val = parseFloat(obj.dayAmountLimitBatch);
                if(val <= 0 && val != -1){
                    layer.msg('批扣每日扣款金额必须大于0');
                    return false;
                }
                if(val > maxNum){
                    layer.msg('批扣每日扣款金额超出最大范围,范围值：1~'+maxNum);
                    return false;
                }
                if(parseFloat(obj.singleAmountLimitBatch) > val){
                    layer.msg('批扣每日扣款金额不能小于批扣单次扣款限额');
                    return false;
                }
            }else{
                layer.msg('批扣每日扣款金额不能为空');
                return false;
            }
        }

        return true;
    }

    function fieldEmptyVal(title,str,maxLen){
        maxLen = maxLen == null ? 0 : maxLen;
        str = str == null ? "" : str;
        if(str == "" || str.length == 0){
            layer.msg("请输入"+title);
            return false;
        }
        if( !(str.length > 0 && str.length <= maxLen) ){
            layer.msg(title+"长度超过限制，限制长度为：0~"+maxLen);
            return false;
        }
        return true;
    }
    $('#myModal').on('hide.bs.modal', function () {
        var queryData = $("#addBankKeyInfo").children();
        $.each(queryData, function(i, field){
            var el = field.querySelector("input");
            if(el != null)
                el.value = "";
        });
    })

</script>
