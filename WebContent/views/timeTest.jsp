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
<h1>Barang Rekomendasi</h1>
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
    <h3> Apakah waktu yang dibutuhkan sistem untuk memberikan rekomendasi masih dapat ditoleransi?</h3>
    <a class="btn btn-default" href="<%=Config.SITE_URL+request.getAttribute("nextUrl")%>?input=1">Ya</a>
    <a class="btn btn-default" href="<%=Config.SITE_URL+request.getAttribute("nextUrl")%>?input=0">Tidak</a>
</div>
<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>
