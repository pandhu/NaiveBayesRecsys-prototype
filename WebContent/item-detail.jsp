<%@ page import="com.coolonWeb.model.Item" %><%--
  Created by IntelliJ IDEA.
  User: root
  Date: 11/04/16
  Time: 13:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Item item = (Item)request.getAttribute("item");
%>
<html>
<head>
    <title><%= item.name %></title>
</head>
<body>

</body>
</html>
