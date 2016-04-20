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
<table class="table table-hover">
    <tr>
        <th>Riwayat Transaksi</th>
    </tr>

    <%
        ArrayList<Item> transactions = ((User)request.getSession().getAttribute("user")).itemTransactions;
        if(transactions != null){
        for(Item item : transactions) {%>
    <tr>
        <td><a href="<%=Config.SITE_URL+"item/detail?id="+item.id%>"><%=item.name%></a></td>
    </tr>
    <% }
    }%>
</table>