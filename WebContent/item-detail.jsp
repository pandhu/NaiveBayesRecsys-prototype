<%@ page import="com.coolonWeb.model.Item" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.coolonWeb.Config" %><%--
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
        Item item = (Item) request.getAttribute("item");
    %>
    <ol class="breadcrumb">
        <li><a href="">Home</a></li>
        <li><a href=""><%=item.category1%></a></li>
        <li><a href=""><%=item.category2%></a></li>
        <li><a href=""><%=item.category3%></a></li>
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

        <a class="btn btn-default btn-sm" href="<%=Config.SITE_URL+"/buy?item="+item.id%>">Beli</a>
        <input type="button" class="btn btn-default btn-lg" value="Kembali" onClick="history.back(1)">
    </div>
    <div class="col-md-6">
	  <table class="table table-hover">
            <tr>
                <th>Nama Produk Rekomendasi</th>
            </tr>
              <%for(Item recommendedItem : (ArrayList<Item>)request.getAttribute("recommendedItems")) {%>
            <tr>
                <td><a href="<%=Config.SITE_URL+"/item/detail?id="+recommendedItem.id%>"><%=recommendedItem.name%></a></td>
            </tr>
            <%}%>
		</table>

    </div>
</div>
<br/><br/>


<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>

