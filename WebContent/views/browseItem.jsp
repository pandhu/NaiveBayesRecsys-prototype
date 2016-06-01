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
    <form method="get" action="<%=Config.SITE_URL%>/survey/chooseItem">
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
            <div class="alert alert-warning" role="alert">
                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                Pada tahap ini anda diminta untuk membeli 3 buah barang yang tersedia pada sistem.
                Anda dapat menggunakan fitur search di samping.
            </div>
        </div>
        <div class="col-md-6">
            <form method="get" action="<%=Config.SITE_URL%>/survey/chooseItem">
                <div class="col-lg-12">
                    <div class="input-group">
                        <input type="text" class="form-control" name="q" placeholder="Search for...">
                          <span class="input-group-btn">
                            <input class="btn btn-default" type="submit" value="Cari">
                          </span>
                    </div>
                </div>
            </form>
            <h3>Hasil Pencarian</h3>
            <%
                ArrayList<Item> resultItems = ((ArrayList<Item>) request.getAttribute("resultItems"));
                if (resultItems.size() == 0){
            %>
            <div class="alert alert-danger" role="alert">
                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                Mohon maaf, barang yang Anda cari tidak tersedia.
            </div>
            <%} else {%>
            <table class="table table-hover">
                <%
                    if(resultItems != null){
                        for(Item item : resultItems ) {%>
                <tr>
                    <td>
                        <%=item.name%> <a class="btn btn-default" href="<%=Config.SITE_URL%>/survey/buy/<%=item.id%>">Beli</a>
                    </td>
                </tr>
                <% }
                }%>
                <input type="text" value="<%=request.getAttribute("stage")%>" name="stage" hidden>
            </table>
            <%}%>
            <input class="btn btn-default" type="submit" value="Submit">
        </div>
    </form>
</row>

<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>
