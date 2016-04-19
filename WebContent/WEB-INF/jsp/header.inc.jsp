<%@ page import="com.coolonWeb.Config" %>
<%@ page import="com.coolonWeb.model.User" %>
<!DOCTYPE html>
<html>
<head>
  <title>Recommender System (Prototype)</title>
  <link rel="stylesheet" type="text/css" href="<%= Config.SITE_URL %>/assets/base/css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="<%= Config.SITE_URL %>/assets/base/css/local.css"/>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">Recommender System </a>
    </div>
    <ul class="nav navbar-nav navbar-right">
      <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
          <%=((User)request.getSession().getAttribute("user")).id%><span class="caret"></span>
        </a>
        <ul class="dropdown-menu">
          <li><a href="<%=Config.SITE_URL+"/user/transactions"%>">Purchase History</a></li>
          <li><a href="<%=Config.SITE_URL+"/auth/logout"%>">Logout</a></li>
        </ul>
      </li>
    </ul>
  </div>
</nav>

<div class="container">

     
