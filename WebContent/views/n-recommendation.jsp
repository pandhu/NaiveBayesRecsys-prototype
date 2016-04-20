<%@ page import="com.coolonWeb.model.Item" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.coolonWeb.Config" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.coolonWeb.model.Transaction" %><%--
  Created by IntelliJ IDEA.
  User: root
  Date: 11/04/16
  Time: 13:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/jsp/header.inc.jsp"></jsp:include>
<div id="container">
    <h1>Detil Produk</h1>
    <%
        ArrayList<Item> recommendation = (ArrayList<Item>) request.getSession().getAttribute("recommendedItem");
        int stage = Integer.parseInt(request.getParameter("stage"));
        Item item = Item.find(recommendation.get(stage-1).id);
    %>
    <ol class="breadcrumb">
        <li><a href="<%=Config.SITE_URL+"/dashboard"%>">Dashboard</a></li>
        <li><a href="<%=Config.SITE_URL+"/category/lvl1?cat1="+ URLEncoder.encode((String) item.category1, "UTF-8")%>"><%=item.category1%></a></li>
        <li><a href="<%=Config.SITE_URL+"/category/lvl2?cat1="+URLEncoder.encode((String) item.category2, "UTF-8")+"&cat2="+URLEncoder.encode((String) item.category2, "UTF-8")%>"><%=item.category2%></a></li>
        <li><a href="<%=Config.SITE_URL+"/category/lvl2?cat1="+URLEncoder.encode((String) item.category3, "UTF-8")+"&cat2="+URLEncoder.encode((String) item.category2, "UTF-8")+"&cat3="+URLEncoder.encode((String) item.category3, "UTF-8")%>"><%=item.category3%></a></li>
        <li class="active"><%=item.name%></li>
    </ol>

    <div class="col-md-6">
        <table class="table table-hover">
            <tr>
                <td>Nomor Produk</td>
                <td>:</td>
                <td><%=item.id%>
            </tr>
            <tr>
                <td>Nama Produk</td>
                <td>:</td>
                <td><%=item.name%>
            </tr>
            <tr>
                <td>Kategori Lv 1</td>
                <td>:</td>
                <td><%=item.category1%>
            </tr>
            <tr>
                <td>Kategori Lv 2</td>
                <td>:</td>
                <td><%=item.category2%>
            </tr>
            <tr>
                <td>Kategori Lv 3</td>
                <td>:</td>
                <td><%=item.category3%>
            </tr>
        </table>

        <a class="btn btn-default btn-lg" href="<%=Config.SITE_URL+"/item/upvote?id="+item.id+"&stage="+stage%>">Beli</a>
        <a class="btn btn-default btn-lg" href="<%=Config.SITE_URL+"/item/downvote?id="+item.id+"&stage="+stage%>">Tidak</a>
    </div>
    <div class="col-md-6">
        <jsp:include page="/views/trasactionHistory.jsp"></jsp:include>
    </div>
</div>
<br/><br/>


<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>


