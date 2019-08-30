<%--
  Created by IntelliJ IDEA.
  User: sun
  Date: 2018/11/22
  Time: 16:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%--引入c 标签--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <!-- 引入CSS样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
<table class="table table-bordered">
    <caption>
        <a href="${pageContext.request.contextPath}/pages/add.jsp" class="btn btn-success">添加</a>
    </caption>
    <thead>
    <tr>
        <th>编号</th>
        <th>账户名</th>
        <th>余额</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%--foreach循环
        items: 要循环的集合对象
        var：循环中的每一个对象
    --%>

    <c:forEach items="${accountList}" var="account" varStatus="i">
    <tr>
        <td>${i.count}</td>
        <td>${account.name}</td>
        <td>${account.money}</td>
        <td>
            <a href="${pageContext.request.contextPath}/account/del?id=${account.id}" class="btn btn-success">删除</a>
            <a href="${pageContext.request.contextPath}/account/updateUI?id=${account.id}" class="btn btn-success">修改</a>
        </td>
    </tr>
    </c:forEach>
    </tbody></table>
</body>
<!-- 引入JS文件 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>


</html>
