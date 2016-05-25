<%@ page import="com.coolonWeb.Config" %><%--
  Created by IntelliJ IDEA.
  User: root
  Date: 13/04/16
  Time: 6:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Recommender System (Prototype)</title>
    <link rel="stylesheet" type="text/css" href="<%= Config.SITE_URL %>/assets/base/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%= Config.SITE_URL %>/assets/base/css/cover.css"/>
</head>
<body>
<div class="site-wrapper">

    <div class="site-wrapper-inner">

        <div class="cover-container">

            <div class="masthead clearfix">
                <div class="inner">
                    <h3 class="masthead-brand">Recommender System - Uji Waktu Komputasi</h3>
                    <nav>
                        <ul class="nav masthead-nav">
                            <li class="active"><a href="#">Home</a></li>
                        </ul>
                    </nav>
                </div>
            </div>

            <div class="inner cover">
                <h1 class="cover-heading">Uji Waktu Komputasi</h1>
                <p>
                    Pada tahap ini akan diujikan waktu komputasi dan toleransi user terhadap waktu komputasi sistem dalam memberikan rekomendasi.
                </p>
                <a class="btn btn-lg btn-default" href="<%=Config.SITE_URL+"/"+request.getAttribute("nextUrl")%>">Mulai</a>
            </div>
            <div class="mastfoot">
                <div class="inner">
                    <p>Tim Riset Recommender System</p>
                </div>
            </div>

        </div>

    </div>

</div>
</body>
</html>
