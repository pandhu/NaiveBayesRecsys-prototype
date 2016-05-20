<%@ page import="com.coolonWeb.Config" %>
<%@ page import="com.coolonWeb.model.User" %><%--
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
                    <h3 class="masthead-brand">Recommender System - Simulation</h3>
                    <nav>
                        <ul class="nav masthead-nav">
                            <li class="active"><a href="#">Home</a></li>
                        </ul>
                    </nav>
                </div>
            </div>

            <div class="inner cover">
                <form action="<%=Config.SITE_URL%>/user/register" method="POST">
                    <div class="form-group">
                        <label>Field</label>
                        <input type="text" class="form-control" >
                    </div>
                    <div class="form-group">
                        <label>Field</label>
                        <input type="text" class="form-control" >
                    </div>
                    <div class="form-group">
                        <label>Field</label>
                        <input type="text" class="form-control" >
                    </div>
                    <div class="form-group">
                        <label>Field</label>
                        <input type="text" class="form-control" >
                    </div>
                    <button type="submit" class="btn btn-default">Register</button>
                </form>
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
