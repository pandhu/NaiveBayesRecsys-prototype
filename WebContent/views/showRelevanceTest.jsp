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

<h1>Relevansi Rekomendasi</h1>
<row>
<div class="col-md-4">
    <h3>Riwayat Transaksi</h3>
    <table class="table table-hover">
        <%
            ArrayList<Item> historyItems = ((ArrayList<Item>) request.getAttribute("historyItems"));
            if(historyItems != null){
                for(Item item : historyItems ) {%>
        <tr>
            <td><a href="<%=Config.SITE_URL+"item/detail?id="+item.id%>"><%=item.name%></a></td>
        </tr>
        <% }
        }%>
    </table>
</div>
<div class="col-md-4">
    <h3>Rekomendasi Metode A</h3>

    <table class="table table-hover">
        <%
            ArrayList<Item> modelItems = ((ArrayList<Item>) request.getAttribute("modelItems"));
            if(modelItems != null){
                for(Item item : modelItems ) {%>
        <tr>
            <td><a href="<%=Config.SITE_URL+"item/detail?id="+item.id%>"><%=item.name%></a></td>
        </tr>
        <% }
        }%>
    </table>
</div>
<div class="col-md-4">
    <h3>Rekomendasi Metode B</h3>
    <table class="table table-hover">
        <%
            ArrayList<Item> memoryItems = ((ArrayList<Item>) request.getAttribute("memoryItems"));
            if(memoryItems != null){
                for(Item item : memoryItems ) {%>
        <tr>
            <td><a href="<%=Config.SITE_URL+"item/detail?id="+item.id%>"><%=item.name%></a></td>
        </tr>
        <% }
        }%>
    </table>
</div>
</row>
<h3> Dari rekomendasi diatas, menurut Anda metode mana yang lebih relevan?</h3>
<a class="btn btn-default" href="<%=Config.SITE_URL+request.getAttribute("nextUrl")%>?input=mod">Metode A</a>
<a class="btn btn-default" href="<%=Config.SITE_URL+request.getAttribute("nextUrl")%>?input=mem">Metode B</a>
<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>
