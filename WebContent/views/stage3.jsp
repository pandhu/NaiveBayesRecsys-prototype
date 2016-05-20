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
                    <h3 class="masthead-brand">Recommender System - Simulation Stage 3</h3>
                </div>
            </div>

            <div class="inner cover">
                <h1 class="cover-heading">Tahap 3 - Final</h1>
                <p>
                    Pada tahap ini akan disimulasikan skenario yang sama seperti tahap sebelumnya namun dengan penambahan data dalam database untuk melihat scalability dan adaptibility sistem
                </p>
                <a class="btn btn-lg btn-default" href="<%=Config.SITE_URL%>/survey/testTime/methodA/part3">Lanjutkan</a>
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
