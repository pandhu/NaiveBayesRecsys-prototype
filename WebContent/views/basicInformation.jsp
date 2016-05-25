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
<ol class="breadcrumb">
    <li>Informasi Umum</li>
</ol>
<div class="inner cover">
    <form action="<%=Config.SITE_URL%>/survey/basicInformation" method="POST">
        <%
            String error = (String) request.getAttribute("error");
            if(error != null){
        %>
        <div class="alert alert-danger" role="alert">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            <span class="sr-only">Error:</span>
            <%= error%>
        </div>
        <%
            }
        %>
        <div class="form-group">
            <label>Email</label>
            <input type="text" name="email" class="form-control" required>
        </div>
        <div class="form-group">
            <label>Usia</label>
            <input type="number" name="age" class="form-control" required>
        </div>
        <div class="form-group">
            <label>Gender</label>
            <div class="radio">
                <label><input type="radio" value="M" name="gender" required>Laki-laki</label>
            </div>
            <div class="radio">
                <label><input type="radio" value="F" name="gender">Perempuan</label>
            </div>
        </div>
        <div class="form-group">
            <label>Hp</label>
            <input type="text" name="phone" class="form-control" required>
        </div>
        <button type="submit" class="btn btn-default">Lanjut</button>
    </form>
</div>
<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>
