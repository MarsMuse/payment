<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@include file="../../../common/commonCSS.jsp" %>
<%@include file="../../../common/commonJS.jsp" %>
<html>
<head>
    <title>支付平台</title>
    <style>
        *{
            margin: 0 auto;
            font-family: "Microsoft YaHei";
            font-size:14px;
        }
        body{
            overflow: hidden;
            margin: 0px;
            padding: 0px;
            position: fixed;
            z-index: 1;
            width: 100%;
            height: 100%;
            background-image: url("${path}/assets/images/background.png");
            background-size: cover;
        }
        .login-window{
            width: 362px;
            height: 482px;
            border: 1px solid #d5dce5;
            border-radius: 5px;
            margin-top: 10%;
            margin-left: 68%;
            background-color: white;
            opacity: 0.9;
        }
        .login-window h3{
            font-size: 1.5em;
            color: #4d5e73;
            padding: 25px 36px 15px 36px;
            margin-bottom: 32px;
            margin-top: 0px;
        }
        .input-login-fm{
            margin-left: 36px;
            margin-bottom: 36px;
            margin-right: 36px;
            width: 288px;
            height: 42px;
            line-height: 42px;
        }
        .input-validate-fm{
            margin-left: 36px;
            margin-bottom: 36px;
            width: 140px;
            height: 42px;
            line-height: 42px;
            float: left;
        }
        .validate-image{
            margin-left: 12px;
            width: 120px;
            height: 42px;
            line-height: 42px;
            cursor: pointer;
        }
        .login-button-fm{
            background: #3790CC;
            display: block;
            height: 48px;
            line-height: 48px;
            color: #fff;
            text-align: center;
            margin-top: 20px;
            font-size: 1.5em;
            text-decoration: none;
            clear: both;
            width: 288px;
            border: solid 1px #3790CC;
            border-radius: 3px;
            box-shadow: 0 0 3px #fff inset;
            margin-right: 36px;
            margin-left: 36px;
        }
    </style>
</head>

<body>
    <div  class="login-window">
        <h3>账号登录</h3>
        <input type="text"  name = "loginCode"  id="loginCode"  class="form-control  input-login-fm"  placeholder="输入用户名" autocomplete="off">
        <input type="password"  name="userPass"  id="userPass"  class="form-control input-login-fm"  placeholder="输入密码" autocomplete="off">
        <div>
            <input type="text"  name="validateCode" id="validateCode"  class="form-control input-validate-fm  "  placeholder="输入验证码" autocomplete="off">
            <img id="validateCodeNumber" src="${path}/action/usermanager/validate"  class="validate-image" title="点击更换图片"  />
        </div>
        <button  class="login-button-fm"  id="loginButton"  onclick="login()">登录</button>
    </div>
</div>
</body>

<script>
    $(function(){
        //验证码
        $("#validateCodeNumber").click(function() {
            $("#validateCodeNumber").attr("src", "validate.do?rnd=" + Math.random());
        });

        getValidateCode = function(){
            $("#validateCodeNumber").attr("src", "validate.do?rnd=" + Math.random());
        }
        $("body").keydown(function() {
            if (event.keyCode == 13) {
                login();
            }
        });

        login = function(){
            //数据验证
            if(verifyData()){
                getValidateCode();
                return;
            }
            //获取到数据
            var data = {
                loginCode : $.trim($("#loginCode").val()),
                userPass : $.trim($("#userPass").val()),
                validateCode : $.trim($("#validateCode").val())
            }
            $.ajax({
                type:"POST",
                url:"${path}/action/usermanager/login",
                dataType:"json",
                data:data,
                success:function(data){
                    dealResult(data);
                },
                error:function(){
                    layer.msg('网络异常，请刷新浏览器重试',{
                        time :1500
                    });
                }
            });

        };
        //数据验证函数
        verifyData = function(){

            var loginCodeValue =$("#loginCode").val();
            if(loginCodeValue == null || $.trim(loginCodeValue) == ''){
                layer.msg('请输入用户名',{
                    time :1500
                });
                return true;
            }


            var userPassValue =$("#userPass").val();
            if(userPassValue == null || $.trim(userPassValue) == ''){
                layer.msg('请输入密码',{
                    time :1500
                });
                return true;
            }

            var validateCodeValue =$("#validateCode").val();
            if(validateCodeValue == null || $.trim(validateCodeValue) == ''){
                layer.msg('请输入验证码',{
                    time :1500
                });
                return true;
            }

            return false;
        };
        //数据处理
        dealResult = function(data){
            if(data.success){
                location.href="${path}/action/usermanager/main";
            }else if(data.result == 1){
                getValidateCode();
                layer.msg('验证码输入错误',{
                    time :1500
                });
            }else if(data.result == 3){
                layer.msg('账户不存在或密码错误',{
                    time :1500
                });
            }else{
                layer.msg('账户被锁定或不存在，请联系管理员',{
                    time :1500
                });
            }
        };
    });

</script>
</html>