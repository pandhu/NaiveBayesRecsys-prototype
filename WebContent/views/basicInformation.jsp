<%@ page import="com.coolonWeb.model.Item" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.coolonWeb.Config" %>
<%@ page import="com.coolonWeb.model.Category" %>
<%@ page import="java.net.URLEncoder" %><%--
  Created by IntelliJ IDEA.
  User: root
  Date: 13/04/16
  Time: 10:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/jsp/header.inc.jsp"></jsp:include>

<h1>Informasi Umum</h1>
<?php $baris = $hasilnya[0]?>
<ol class="breadcrumb">
    <li>Informasi Umum</li>
</ol>
<div class="inner cover">
    <form action="<%=Config.SITE_URL%>/survey/basicInformation" method="POST">
        <div class="form-group">
            <label>Email</label>
            <input type="text" name="email" class="form-control" >
        </div>
        <div class="form-group">
            <label>Usia</label>
            <input type="number" name="age" class="form-control" >
        </div>
        <div class="form-group">
            <label>Gender</label>
            <input type="text" name="gender" class="form-control" >
        </div>
        <div class="form-group">
            <label>Hp</label>
            <input type="text" name="phone" class="form-control" >
        </div>
        <button type="submit" class="btn btn-default">Lanjut</button>
    </form>
</div>
<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>
