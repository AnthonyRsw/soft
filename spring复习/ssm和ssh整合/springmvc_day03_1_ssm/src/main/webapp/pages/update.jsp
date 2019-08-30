<%--
  Created by IntelliJ IDEA.
  User: sun
  Date: 2018/11/22
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <!-- 引入CSS样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
<form class="form-horizontal" role="form" method="post" action="${pageContext.request.contextPath}/account/update">
    <%--使用隐藏域，保存id的值，更新时作为条件--%>
    <input type="hidden" name="id" value="${account.id}">
    <div class="form-group">
        <label for="username" class="col-sm-2 control-label">账户名</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="username" name="name" value="${account.name}"
                   placeholder="请输入账户名">
        </div>
    </div>
    <div class="form-group">
        <label for="money" class="col-sm-2 control-label">余额</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="money" name="money" value="${account.money}"
                   placeholder="请输入余额">
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">更新</button>
        </div>
    </div></form>
</body>
<!-- 引入JS文件 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

</html>
