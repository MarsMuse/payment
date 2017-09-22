<%--
  Created by IntelliJ IDEA.
  User: zhph
  Date: 2017-04-19
  Time: 15:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Title</title>

    <style type="text/css">
        #timeout_div {
            margin: auto auto;
            width: 50%;
        }
    </style>
</head>
<body>
    <div id="timeout_div">
        <h1  class="text-center text-danger">登陆超时或账号被他人登陆请重新登录</h1>
        <button type="button" class="btn btn-warning btn-lg btn-block" onclick="window.location.href='${path}/logout.do'" >重新登录</button>
    </div>

</body>
</html>
