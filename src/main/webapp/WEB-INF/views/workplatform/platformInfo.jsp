<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div>
    <div class="breadcrumbs" id="breadcrumbs">
        <ul class="breadcrumb">
            <li>
                <i class="icon-home home-icon"></i>
                <a href="#">密钥管理</a>
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
                            <label for="platformName">平台名称：</label>
                            <input  type="text" id="platformName"   name="platformName"   class="form-control"  >
                        </div>
                    </div>

                    <div class="col-md-3 ">
                        <div class="form-group">
                            <label for="platformName">平台代码：</label>
                            <input  type="text" id="platformCode"   name="platformCode"   class="form-control"  >
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div id="toolbar1">
        <button type="button" class="btn btn-primary" onclick="queryDataByButton()"><span  class="glyphicon glyphicon-search  table-span-icon"></span>查询</button>
        <!-- <button type="button" class="btn btn-success" onclick="addDataByButton()"><span  class="glyphicon glyphicon-plus  table-span-icon"></span>新增</button>
        <button type="button" class="btn btn-info" onclick="exportDataByButton()"><span  class="glyphicon glyphicon-download-alt  table-span-icon"></span>导出</button> -->
    </div>
    <!-- 表格内容-->
    <table  id="dataInforTable"></table>
</div>
<script  type="text/javascript">
    //渲染select2
    $(".js-example-basic-single").select2({width:'179px',height:'34px'});
    //获取导审核数据
    function  getJudgeInfor(gridHandler, page){
        //表单数据
        var queryData = $("#queryForm").serializeArray();
        //根据不同节点查出不同数据
        queryData.push({name:'currentPage' , value: page.currentPage});
        queryData.push({name:'limit' , value: page.limit});
        //将page替换过去
        /* queryData.push(page); */
        $.ajax({
            url:"${path}/action/workplatform/cipherkeylist.do",
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
        uniqueId : 'id',
        method : 'post',
        toolbar : '#toolbar1',
        //resizable:true, //注：如果要求启用resizable 一定要将paginationVAlign设置为top，否则在部分版本的chrome中出现table水平滚动轴与分页栏部分重合的情况
        columns : [
            {
                title: '序号',//标题  可不加
                width:"60px",
                align:'center',
                formatter: function (value, row, index) {
                    return    tba.currentOffset+index+1;
                }
            },
            {
                field: 'platformCode',
                title: '平台代码'
            },
            {
                field: 'platformName',
                title: '平台名称'
            },
            {
                field: 'certificatePath',
                title: '密钥路径'
            },
            {
                field: 'certificateName',
                title: '密钥名称'
            },
            {
                field: 'id',
                title: '操作',
                width:"120px",
                formatter:function(value , rowEntity , index){
                    return'<button type="button" class="btn btn-success  btn-sm  "  onclick="updateKey(\''+value+'\',\''+rowEntity.certificatePath+'\',\''+rowEntity.certificateName+'\')"   style="margin-right:12px;"><span  class="glyphicon glyphicon-screenshot  table-span-icon"></span>修改密钥</button>' ;
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
    //修改密钥
     function  updateKey(id ,certificatePath ,certificateName){


         layer.open({
            title:'密钥信息修改',
            type: 1,
            closeBtn: 1,
            area:['480px','360px'],
            shadeClose: true,
            move :false,
            resize :false,
            btn: ['确定', '取消'],
            btnAlign: 'r',
            yes: function(index, layero){

                submitKeyInfo(id);
                layer.close(index);
                return false;
            },
            cancel: function(index, layero){
                layer.close(index);
                return false;
            },
             content:'<div  style="padding-top:48px; "> <form class="form-inline"  id="updateKeyForm"><div class="form-group"  style="margin-bottom: 24px;"><label for="certificatePath">密钥路径：</label><input  type="text" id="certificatePath" style="width: 280px;" value="'+certificatePath+'" name="certificatePath"   class="form-control" ></div>'+
                    '<div class="form-group"><label for="certificateName">密钥名称：</label><input  type="text" id="certificateName" style="width: 280px;"  value="'+certificateName+'" name="certificateName"   class="form-control" ></div>'+
         '</form></div>'
        });
    };

     function submitKeyInfo(id){
         //表单数据
         var queryData = $("#updateKeyForm").serializeArray();
         queryData.push({name:'id' , value: id});
         $.ajax({
             url:"${path}/action/workplatform/updatekey.do",
             method:"post",
             data:queryData,
             dataType:"json"
         }).success(function(data){
            if(data == '1'){
                layer.msg('密钥更新成功');
                queryDataByButton();
            }else{
                layer.msg('密钥更新异常');
            }
         }).error(function(){
             console.error("网络异常");
         });
     };

</script>
