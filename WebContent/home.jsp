<%@ page import="com.coolonWeb.Main" %>
<%@ page import="com.coolonWeb.DataObject" %>
<%@ page import="com.coolonWeb.model.Item" %>
<%@ page import="java.util.ArrayList" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%
request.setAttribute("pageHeading", "Selamat datang di sistem rekomendasi E-Commerce (prototype)!");
  String title = "Home";
request.setAttribute("pageTitle", title);
%>
<jsp:include page="/WEB-INF/jsp/header.inc.jsp"></jsp:include>


<div class="center">
  <h2>Username Kamu adalah ${username}</h2>
  <h3>Silahkan Pilih barang dibawah ini</h3>
  <%for(Item item : (ArrayList<Item>)request.getAttribute("items")) {%>
  <ul>
  <li><a href="/NaiveBayesRecSys/buy?item=<%= item.id %>"><%= item.name%></a></li>
  </ul>
  <%}%>
</div>

<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>