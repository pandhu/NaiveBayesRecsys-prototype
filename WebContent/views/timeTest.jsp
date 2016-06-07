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
<h1>Uji <%= request.getAttribute("method")%></h1>
<div class="col-md-6">
    <div class="alert alert-warning" role="alert">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        Pada tahap ini anda diminta untuk memilih barang yang menurut anda menarik dari daftar rekomendasi barang di
        sebelah kanan berdasarkan riwayat transaksi.
    </div>
    <div class="alert alert-warning" role="alert">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        Diasumsikan Anda merupakan orang yang sangat kaya raya sehingga
        harga bukanlah aspek yang dipertimbangkan dalam membeli barang
    </div>

    <div class="alert alert-warning" role="alert">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        Maksimal memilih 2. Boleh tidak memilih.
    </div>
    <div class="col-md-12">
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
</div>
<div class="col-md-6">
    <form method="post" action="<%=Config.SITE_URL%>/survey/test/submit">
    <table class="table table-hover">
        <%
            ArrayList<Item> items = ((ArrayList<Item>) request.getAttribute("items"));
            if(items != null){
                int position  = 1;
                for(Item item : items ) {%>
        <tr>
            <td><label><input type="checkbox" value="<%=item.id+"-"+position%>" name="selectedItem"> <%=item.name%></label></td>
        </tr>
        <%      position++;
                }
        }%>
    </table>
        <div class="form-group">
            <label>Apakah waktu yang dibutuhkan sistem untuk memberikan rekomendasi masih dapat ditoleransi?</label>
            <div class="radio">
                <label><input type="radio" value="1" name="isTolarable" required>Ya</label>
            </div>
            <div class="radio">
                <label><input type="radio" value="0" name="isTolarable">Tidak</label>
            </div>
        </div>
        <input type="text" value="<%=Config.SITE_URL+request.getAttribute("nextUrl")%>" name="nextUrl" hidden>
        <input type="text" value="<%=request.getAttribute("identifier")%>" name="identifier" hidden>
        <input class="btn btn-default" type="submit" value="Submit">
    </form>
</div>

<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>
