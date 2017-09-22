<%--
  Created by IntelliJ IDEA.
  User: zhph
  Date: 2017-02-28
  Time: 14:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div id="content-pane">
     <form  class="form-horizontal">
         <shiro:principal type="java.lang.String"/>
         <shiro:principal property="name"/>

         <shiro:hasPermission name="HCheck">
             <input type="button" value="审核" class="form-control"/>
         </shiro:hasPermission>
         <shiro:hasRole name="SYS_admin">
             用户[<shiro:principal/>]拥有权限SYS_HCheck:HCheck<br/>
         </shiro:hasRole>
     </form>

</div>
