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
                    <h3 class="masthead-brand">Recommender System Elevenia - Simulation</h3>
                    <nav>
                        <ul class="nav masthead-nav">
                            <li class="active"><a href="#">Home</a></li>
                        </ul>
                    </nav>
                </div>
            </div>

<div class="inner cover">
    <h1 class="cover-heading">Login Page</h1>
    <form method="post" action="<%= Config.SITE_URL %>/auth/doLogin">

    <div class="form-group">
        <label for="exampleInputEmail1">Member ID</label>
        <input type="text" class="form-control input-lg" name="vusername" size="20" placeholder="Member ID Disini">
    </div>
    <input type="submit" class="btn btn-lg btn-default" value="login">
    </form>
    </p>
</div>
<div id="message">
    Contoh Memno : 1466269, 520538696, 2246391147, 4292770747, 4287127849
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
