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
    <form method="post" action="<%=Config.SITE_URL%>/survey/submitRelevanceTest">
    <div class="col-md-6">
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
        <p>Pada tahap ini anda diminta untuk memilih barang yang menurut anda menarik dari daftar rekomendasi barang di
            sebelah kanan berdasarkan riwayat transaksi. Diasumsikan Anda merupakan orang yang sangat kaya raya sehingga
            harga bukanlah aspek yang dipertimbangkan dalam membeli barang
        </p>
    </div>
    <div class="col-md-6">
        <h3>Rekomendasi</h3>
        <table class="table table-hover">
            <%
                ArrayList<Item> recommendedItems = ((ArrayList<Item>) request.getAttribute("recommendedItems"));
                if(recommendedItems != null){
                    for(Item item : recommendedItems ) {%>
            <tr>
                <td>
                    <label><input type="checkbox" value="<%=item.method%>" name="selectedItem"> <%=item.name%></label>
                </td>
            </tr>
            <% }
            }%>
            <input type="text" value="<%=request.getAttribute("stage")%>" name="stage" hidden>

        </table>
        <input class="btn btn-default" type="submit" value="Submit">
    </div>
    </form>
</row>

<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>
