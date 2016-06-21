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
<%
    String error = (String) request.getSession().getAttribute("error");
    if(error != null){%>
<div class="alert alert-danger" role="alert">
    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
    Tidak bisa memilih barang yang sama
</div>
<%  }
    request.getSession().removeAttribute("error");
%>
<%
    String success = (String) request.getSession().getAttribute("success");
    if(success != null){%>
<div class="alert alert-success" role="alert">
    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
    Barang berhasil ditambahkan ke riwayat transaksi
</div>
<%  }
    request.getSession().removeAttribute("success");
%>
<h1>Pilih Barang</h1>
<row>
        <div class="col-md-6">
            <h3>Riwayat Transaksi</h3>
            <table class="table table-hover">
                <%int historySize = 0;
                    ArrayList<Item> historyItems = ((ArrayList<Item>) request.getAttribute("historyItems"));
                    if(historyItems != null){
                        historySize = historyItems.size();
                        for(Item item : historyItems ) {%>
                <tr>
                    <td><a><%=item.name%></a></td>
                </tr>
                <%      }
                    }%>
                <%=historyItems.size()%>/<%=request.getSession().getAttribute("buyLimit")%>
            </table>
            <div class="alert alert-warning" role="alert">
                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                Pada tahap ini anda diminta untuk membeli <%= (Integer)request.getSession().getAttribute("buyLimit")- historySize%> lagi barang yang tersedia pada sistem.
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
                        <a class="btn btn-default" href="<%=Config.SITE_URL%>/survey/buy?id=<%=item.id%>">Pilih</a> <%=item.name%>
                    </td>
                </tr>
                <% }
                }%>
            </table>
            <%}%>
        </div>
</row>

<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>
