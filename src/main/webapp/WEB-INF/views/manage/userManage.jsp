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
                            <label for="loginCode">登录名：</label>
                            <input  type="text" id="loginCode"   name="loginCode"  maxlength="32"  class="form-control"  >
                        </div>
                    </div>

                    <div class="col-md-3 ">
                        <div class="form-group">
                            <label for="userName">用户姓名：</label>
                            <input  type="text" id="userName"  name="userName" maxlength="64"  class="form-control"  >
                        </div>
                    </div>

                    <div class="col-md-3 ">
                        <div class="form-group">
                            <label for="userCode">员工号：</label>
                            <input  type="text" id="userCode"   name="userCode" maxlength="32"  class="form-control"  >
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div id="toolbar1">
        <button type="button" class="btn btn-primary" onclick="queryDataByButton()"><span  class="glyphicon glyphicon-search  table-span-icon"></span>查询</button>
        <button type="button" class="btn btn-success" onclick="addDataByButton()"><span  class="glyphicon glyphicon-plus  table-span-icon"></span>新增</button>
        <!-- <button type="button" class="btn btn-info" onclick="exportDataByButton()"><span  class="glyphicon glyphicon-download-alt  table-span-icon"></span>导出</button> -->
    </div>
    <!-- 表格内容-->
    <table  id="dataInforTable"></table>
</div>
<script  type="text/javascript">
    //新增或修改标志位，在本页面为全局变量 0 未新增 1为修改
    var addOrUpdateFlag = 0;


    //获取数据列表
    function  getListInfo(gridHandler, page){
        //表单数据
        var queryData = $("#queryForm").serializeArray();
        //根据不同节点查出不同数据
        queryData.push({name:'currentPage' , value: page.currentPage});
        queryData.push({name:'limit' , value: page.limit});
        $.ajax({
            url:"${path}/action/manage/userlist",
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
        uniqueId : 'userId',
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
                field: 'userName',
                title: '用户姓名'
            },
            {
                field: 'loginCode',
                title: '登录名'
            },
            {
                field: 'userCode',
                title: '员工号'
            },
            {
                field: 'mobile',
                title: '手机号'
            },
            {
                field: 'eMail',
                title: '邮箱'
            },
            {
                field: 'createTime',
                title: '注册时间',
                formatter:function(value){
                    return globalDateFormat(value , 'yyyy-mm-dd hh:mi:ss');
                }
            },
            {
                field: 'lastTime',
                title: '最近登录时间',
                formatter:function(value){
                    return globalDateFormat(value , 'yyyy-mm-dd hh:mi:ss');
                }
            },
            {
                field: 'errorTimes',
                title: '错误次数'
            },
            {
                field: 'isLock',
                title: '是否锁定',
                align:'center',
                formatter: function (value) {
                    return    value == 'N'?'否':'是';
                }
            },
            /*{
                field: 'status',
                title: '激活状态',
                align:'center',
                formatter: function (value) {
                    return    value == '0'?'否':'是';
                }
            },*/
            {
                field: 'userId',
                title: '操作',
                width:'260px',
                formatter:function(value , rowEntity , index){
                    var lock = rowEntity.isLock== 'N' ?'Y':'N';
                    var lockButton = rowEntity.isLock == 'N'?'锁定':'解锁';
                    /*var entityInfo = JSON.stringify(rowEntity);//'{"1":"2"}';
                    while(entityInfo.indexOf('"') !== -1){
                        entityInfo = entityInfo.replace('"','\'');
                    }*/
                    return   '<button type="button" class="btn btn-warning  btn-sm  "  onclick="lockUser(\''+value+'\',\''+lock+'\')"   style="margin-right:12px;"><span  class="glyphicon glyphicon-tint  table-span-icon"></span>'+lockButton+'</button>' +
                            '<button type="button" class="btn btn-danger   btn-sm  "  onclick="deleteUser(\''+value+'\')"   style="margin-right:12px;"><span  class="glyphicon glyphicon-remove  table-span-icon"></span>删除</button>';
                }
            }
        ]
    };

    //表格初始化配置
    var tbControl = {
        dataHandler :getListInfo,
        options:tbOptions
    };
    //获取到表格控制器
    var gridController = tba.grid("dataInforTable" , tbControl);

    //查询这个表格
    function queryDataByButton(){
        gridController.query();
    };
    //新增人员
    function  addDataByButton(){
        //修改为新增标志
        addOrUpdateFlag = 0;
        $('#myModal').modal({
            keyboard: false
        });
    };
    //修改用户信息
    function modifyUser(entityInfo){
        //标志位修改为1表示此次为修改
        addOrUpdateFlag = 1;

        $("#loginCodeInfo").val(entityInfo.loginCode);
        $('#myModal').modal({
            keyboard: false
        });

    };
    //激活用户
    function lockUser(id, lock){
        var lockMessage =  lock== 'Y'?'你确定 <b>锁定</b> 该用户？':'你确定 <b>解锁</b> 该用户？';
        layer.msg(lockMessage, {
            time: 0 //不自动关闭
            ,shade:0.4
            ,shadeClose:true
            ,btn: ['确定', '取消']
            ,yes: function(index){
                layer.close(index);
                operateLock(id, lock);
            }
            ,no:function () {
                layer.close(index);
                return ;
            }
        });
    };
    //操作锁定/解锁
    function  operateLock(id, lock){
        $.ajax({
            url:"${path}/action/manage/updatelock",
            method:"post",
            data:{id:id ,lock:lock },
            dataType:"json"
        }).success(function(data){
            if(data == '1'){
                layer.msg('用户锁定状态修改成功');
                //刷新列表
                gridController.refresh();
            }else{
                layer.msg('用户锁定状态修改失败，请刷新重试');
            }
        }).error(function(){
            console.error("网络异常，请刷新浏览器");
        });
    };


    //删除用户
    function deleteUser(id){
        layer.msg('你确定 <b>删除</b> 该用户？', {
            time: 0
            ,shade:0.4
            ,shadeClose:true
            ,btn: ['确定', '取消']
            ,yes: function(index){
                layer.close(index);
                operateDelete(id);
            }
            ,no:function () {
                layer.close(index);
                return ;
            }
        });
    };
    //操作删除用户
    function operateDelete(id){
        $.ajax({
            url:"${path}/action/manage/phydelete",
            method:"post",
            data:{id:id },
            dataType:"json"
        }).success(function(data){
            if(data == '1'){
                layer.msg('用户删除成功');
                //刷新列表
                gridController.refresh();
            }else{
                layer.msg('用户删除失败，请刷新重试');
            }
        }).error(function(){
            console.error("网络异常，请刷新浏览器");
        });
    };
    //验证用户是否唯一
    function verifyAccount(){

        var accountInfo = $.trim($("#loginCodeInfo").val());
        var loginCodePattern = new RegExp("^\\w+$");
        if(!loginCodePattern.exec(accountInfo)){
            layer.msg('登录名由不超过32位的数字、英文字母或者下划线组成，请重新输入');
            $("#loginCodeInfo").val('');
            return;
        }
        //提交到后台去验证
        if(accountInfo != null && accountInfo != ''){
            $.ajax({
                url:"${path}/action/manage/verifyaccount",
                method:"post",
                data:{loginCode:accountInfo},
                dataType:"json"
            }).success(function(data){
                if(data != '0'){
                    layer.msg('用户名已存在，请重新输入');
                    $("#editUserInfoSubmit").attr("disabled" ,true);
                }else{
                    $("#editUserInfoSubmit").attr("disabled" ,false);
                }
            }).error(function(){
                console.error("获取数据异常")
            });
        }
    };

    function verifyUnPassword(){
        var password = $.trim($("#password").val());
        if(password != null && password != ''){
            var unPassword = $.trim($("#unPassword").val());
            if(password != unPassword){
                layer.msg('两次密码输入不一致，请重新输入');
            }
        }
    };

    function verifyPassword(){
        var password = $.trim($("#unPassword").val());
        if(password != null && password != ''){
            var unPassword = $.trim($("#password").val());
            if(password != unPassword){
                layer.msg('两次密码输入不一致，请重新输入');
            }
        }
    };

    function submitUserInfo(){
        //获取到信息
        var userInfo= {
            loginCode:$.trim($("#loginCodeInfo").val()),
            userPass:$.trim($("#password").val()),
            userName:$.trim($("#userNameInfo").val()),
            userCode:$.trim($("#userCodeInfo").val()),
            mobile:$.trim($("#mobileInfo").val()),
            eMail:$.trim($("#eMailInfo").val()),
        };
        if(userInfo.loginCode == null || userInfo.loginCode == '' ){
            layer.msg('登录名不可为空，请重新输入');
            return;
        }
        //登录名合法性检测
        var loginCodePattern = new RegExp("^\\w+$");
        if(!loginCodePattern.exec(userInfo.loginCode)){
            layer.msg('登录名由数字、英文字母或者下划线组成，请重新输入');
            return;
        }

        if(userInfo.userPass == null || userInfo.userPass == ''
            || $("#unPassword").val() == null || $.trim($("#unPassword").val()) == ''){
            layer.msg('密码不可为空，请重新输入');
            return;
        }
        //验证密码是否一致
        var unPassword = $.trim($("#unPassword").val());
        if(userInfo.userPass == null || userInfo.userPass == '' || userInfo.userPass != unPassword){
            layer.msg('两次密码输入不一致，请重新输入');
            return;
        }


        //密码合法性检测
        var passwordPattern = new RegExp("^[a-zA-Z\\d_]{8,}$");
        if(!passwordPattern.exec(userInfo.userPass)){
            layer.msg('密码由8-32位的数字、英文字母和特殊字符组成，请重新输入');
            return;
        }

        if(userInfo.userName == null || userInfo.userName == '' ){
            layer.msg('用户姓名不可为空，请重新输入');
            return;
        }

        if(userInfo.userCode == null || userInfo.userCode == '' ){
            layer.msg('员工号不可为空，请重新输入');
            return;
        }

        if(userInfo.mobile == null || userInfo.mobile == '' ){
            layer.msg('电话号码不可为空，请重新输入');
            return;
        }
        //手机号码合法性检测
        var mobilePattern = new RegExp("^1(3|4|5|7|8)\\d{9}$");
        if(!mobilePattern.exec(userInfo.mobile)){
            layer.msg('手机号码不合法，请重新输入');
            return;
        }
        //邮箱合法性检测
        var emailPattern = new RegExp("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$");
        if(userInfo.eMail != null && userInfo.eMail != '' ){
            if(!emailPattern.exec(userInfo.eMail)){

                layer.msg('邮箱不合法，请重新输入');
                return;
            }

        }

        $.ajax({
            url:"${path}/action/manage/adduser",
            method:"post",
            data:{data:JSON.stringify(userInfo)},
            dataType:"json"
        }).success(function(data){
            if(data == '1'){
                layer.msg('新增用户成功');
                $('#myModal').modal('hide');
                queryDataByButton();
            }else{
                layer.msg('用户已存在，请修改后重新提交');
            }
        }).error(function(){
            console.error("网络异常")
        });
    };

    $("#myModal").on('hide.bs.modal', function (e) {
        $(':input','#addUserInfo')
            .not(':button, :submit, :reset, :hidden')
            .val('')
            .removeAttr('checked')
            .removeAttr('selected');
        $("#editUserInfoSubmit").attr("disabled" ,false);
    });
</script>
<%--模态窗口1--%>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">新增人员信息</h4>
            </div>
            <div class="modal-body">
                <form class="form-inline"  id="addUserInfo">
                    <div class="form-group add-modal-form-fm">
                        <label for="loginCodeInfo">登录名：</label>
                        <input  type="text" id="loginCodeInfo"   name="loginCodeInfo"  onblur="verifyAccount()"  class="form-control add-modal-input-fm" autocomplete="off" maxlength="32" placeholder="请输入登录名" required >
                        <span class="glyphicon glyphicon-asterisk text-danger"></span>
                    </div>

                    <div class="form-group add-modal-form-fm">
                        <label for="password">密码：</label>
                        <input  type="password" id="password"   name="password"   class="form-control add-modal-input-fm"  autocomplete="off" maxlength="32" placeholder="请输入密码" required>
                        <span class="glyphicon glyphicon-asterisk text-danger"></span>
                    </div>
                    <div class="form-group add-modal-form-fm">
                        <label for="unPassword">确认密码：</label>
                        <input  type="password" id="unPassword"   name="unPassword"  class="form-control add-modal-input-fm"  autocomplete="off" maxlength="32" placeholder="请确认密码" required>
                        <span class="glyphicon glyphicon-asterisk text-danger"></span>
                    </div>

                    <div class="form-group add-modal-form-fm">
                        <label for="userNameInfo">用户姓名：</label>
                        <input  type="text" id="userNameInfo"   name="userNameInfo"   class="form-control add-modal-input-fm" placeholder="请输入姓名" maxlength="24" autocomplete="off"  required>
                        <span class="glyphicon glyphicon-asterisk text-danger"></span>
                    </div>

                    <div class="form-group add-modal-form-fm">
                        <label for="userCodeInfo">员工号：</label>
                        <input  type="text" id="userCodeInfo"   name="userCodeInfo"   class="form-control add-modal-input-fm" placeholder="请输入员工号" maxlength="32" autocomplete="off"  required>
                        <span class="glyphicon glyphicon-asterisk text-danger"></span>
                    </div>


                    <div class="form-group add-modal-form-fm">
                        <label for="mobileInfo">电话号码：</label>
                        <input  type="text" id="mobileInfo"   name="mobileInfo"   class="form-control add-modal-input-fm" placeholder="请输入电话号码" maxlength="11" autocomplete="off"  required>
                        <span class="glyphicon glyphicon-asterisk text-danger"></span>
                    </div>


                    <div class="form-group add-modal-form-fm">
                        <label for="eMailInfo">邮箱：</label>
                        <input  type="text" id="eMailInfo"   name="eMailInfo"   class="form-control add-modal-input-fm" placeholder="请输入邮箱" maxlength="32" autocomplete="off"  >
                    </div>


                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="editUserInfoSubmit"  onclick="submitUserInfo()">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
