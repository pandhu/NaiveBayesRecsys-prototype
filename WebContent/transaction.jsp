<%@ page import="java.util.ArrayList" %>
<%@ page import="com.coolonWeb.model.Transaction" %>
<%@ page import="com.coolonWeb.Config" %><%--
  Created by IntelliJ IDEA.
  User: root
  Date: 19/04/16
  Time: 12:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/jsp/header.inc.jsp"></jsp:include>

<h1>Daftar History Purchase</h1>
<ol class="breadcrumb">
    <li><a href="/<%= Config.SITE_URL%>">Home</a></li>
    <li class="active">History</li>
</ol>
<div class="col-md-6">
    <table class="table table-hover">
        <th>No</th><th>Nama Barang</th>
        <%
            ArrayList<Transaction> transactions = (ArrayList<Transaction>) request.getAttribute("transactions");
            int no = 1;
        %>
        <%for(Transaction transaction : transactions) {%>
        <tr>
            <td><%=no++%></td>
            <td><a href="/item/detail?id=<%= transaction.item %>"><%= transaction.itemName%></a></td>
        </tr>
        <%}%>
    </table>
</div>
<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>

