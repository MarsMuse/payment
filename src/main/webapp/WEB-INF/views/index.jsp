<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>支付平台</title>
    <%@include file="../../common/commonCSS.jsp" %>
    <%@include file="../../common/commonJS.jsp" %>
    <style>
        *{
            margin: 0 auto;
            font-family: "Microsoft YaHei";
            font-size:14px;
        }
        .table_head{
            background-color: #307ecc;color:white;font-size: 15px;padding: 10px;height: 40px
        }
        #main-body{
            overflow: hidden;
            margin: 0px;
            padding: 0px;
            position: fixed;
            z-index: 1;
            width: 100%;
            height: 100%;
        }
        .sys-nav-bar{
            width: 100%;
            height: 48px;
            min-height:48px;
            background-color: #337ab7;
            clear: both;
        }
        .sys-nav-bar-name{
            height: 48px;
            line-height: 48px;
            font-size: 20px;
            float: left;
            margin-left: 24px;
            color: white;
            cursor: pointer;
        }
        .sys-nav-bar-toolbar{
            height: 48px;
            min-width: 48px;
            float: right;
            margin-right: 24px;
        }
        .sys-log-out-bar{
            line-height: 48px;
            cursor:pointer;
            color: white;
            padding: 0px 12px;
        }
        .sys-log-out-bar:HOVER{
            background-color: #2c5976;
        }
        .sys-self-content{
            width: 100%;
            height:calc(100% - 48px);
        }
        .sys-self-content-left{
            width: 200px;
            height: 100%;
            float: left;
        }
        .sys-self-content-right{
            width: -webkit-calc(100% - 200px);
            width: calc(100% - 200px);
            padding-left:8px;
            padding-right:1px;
            height: 100%;
            overflow-x:hidden;
            overflow-y:auto;
        }
    </style>

    <script type="text/javascript">
        //定义一个全局变量的基本URL(存在污染的可能性)
        var  PROJECT_BAISC_URL = "${path}";
    </script>
</head>
<body  id="main-body">
<!-- 上方导航栏 -->
<div  class="sys-nav-bar">
    <div  class="sys-nav-bar-name">
        <span  class="glyphicon glyphicon-fire"></span>
        <span>正合支付平台</span>
    </div>
    <div  class="sys-nav-bar-toolbar">
        <div  class="sys-log-out-bar"  onclick="logout()"> 退出登录</div>
    </div>
</div>
<!-- 下方内容盒子 -->
<div  class="sys-self-content"   >
    <!-- 左侧菜单盒子 -->
    <div  class="sys-self-content-left">
        <div  id="fm-list-menu"></div>
        &nbsp;
    </div>
    <!-- 右侧内容盒子 -->
    <div  class="sys-self-content-right  scroll-webkit"  id="appWorkSpace">
        <div id="content">
            <img src="${path}/assets/images/index_welcome.png">
        </div>
    </div>
</div>
</body>
<script  type="text/javascript">
    //注销登录
    function logout() {
        location.href = "${path}/action/usermanager/loginOut";
    };
    //获取到菜单数据
    function getMenuData(menuList){
        $.ajax({
            url:"${path}/action/manage/menulist",
            method:"post",
            dataType:"json"
        }).success(menuList).error(function(){
            console.error("获取数据异常")
        });
    };
    //新建一个菜单对象
    var menu = new fmListMenu();
    //菜单初始化函数
    menu.init({dataHandler : getMenuData});

    function  menuClick(url  ,  id){
        //改变指示器
        menu.changeIndicator(id);
        //URL根路径
        var urlRoot = "${path}";
        url = urlRoot+url;
        $.ajax({
            type: "post",
            url: url,
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

    //添加全局ajax请求预处理对象（加上Loading层）
    var  gal  =  new globalAjaxLoading();
</script>
</html>

