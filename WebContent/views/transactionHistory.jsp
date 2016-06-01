<%@ page import="com.coolonWeb.model.Transaction" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.coolonWeb.Config" %>
<%@ page import="com.coolonWeb.model.User" %>
<%@ page import="com.coolonWeb.model.Item" %><%--
  Created by IntelliJ IDEA.
  User: root
  Date: 20/04/16
  Time: 10:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/jsp/header.inc.jsp"></jsp:include>
<h1>Riwayat Transaksi</h1>
<div class="col-md-6">
<table class="table table-hover">
    <%
        ArrayList<Item> items = ((ArrayList<Item>) request.getAttribute("items"));
        if(items != null){
        for(Item item : items ) {%>
    <tr>
        <td><a href="<%=Config.SITE_URL+"item/detail?id="+item.id%>"><%=item.name%></a></td>
    </tr>
    <% }
    }%>
</table>
    <div class="alert alert-warning" role="alert">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        Anda merupakan seorang yang telah membeli barang di atas. Jika anda tidak mengenali daftar barang di atas, Anda dapat mengganti riwayat transaksi.
    </div>
    <a class="btn btn-default" href="<%=Config.SITE_URL%>/survey/testTime/part1">Lanjut</a>
    <a class="btn btn-default" href="<%=Config.SITE_URL%>/survey/transactionHistory">Ganti Riwayat Transaksi</a>

</div>
<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>
