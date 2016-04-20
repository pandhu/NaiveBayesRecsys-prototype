<%@ page import="com.coolonWeb.model.Category" %>
<%@ page import="com.coolonWeb.Config" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.coolonWeb.model.Item" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.coolonWeb.model.Transaction" %><%--
  Created by IntelliJ IDEA.
  User: root
  Date: 13/04/16
  Time: 10:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/jsp/header.inc.jsp"></jsp:include>

<h1>Daftar Barang</h1>
<?php $baris = $hasilnya[0]?>
<ol class="breadcrumb">
    <li>Dashboard</li>
</ol>
<div class="col-md-6">
    <table class="table table-hover">
        <tr>
            <th>No</th>
            <th>Kategori</th>
        </tr>
        <%
            int count = 1;
            for(Category category : (ArrayList<Category>)request.getAttribute("categories")) {%>
        <tr>
            <td><%=count++%></td><td><a href="<%=Config.SITE_URL+"/category/lvl1?cat1="+URLEncoder.encode(category.name, "UTF-8")%>"><%=category.name%></a></td>
        </tr>
        <%}%>
    </table>
</div>
<div class="col-md-6">

    <table class="table table-hover">
        <tr>
            <th>Riwayat Transaksi</th>
        </tr>

        <%if(request.getAttribute("historyTransaction") != null){
            for(Transaction transaction : (ArrayList<Transaction>)request.getAttribute("historyTransaction")) {%>
        <tr>
            <td><a href="<%=Config.SITE_URL+"item/detail?id="+transaction.item%>"><%=transaction.itemName%></a></td>
        </tr>
        <% }
        }%>
    </table>

</div>
<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>
