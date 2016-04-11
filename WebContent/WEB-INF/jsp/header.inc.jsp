<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>${pageTitle} - ${siteName}</title>
<meta name="description" content="${metaDescription}" />
<meta name="keywords" content="${metaKeywords}" />
  <meta name="author" content="Pierre-Henry Soria" />
  <link rel="stylesheet" href="/NaiveBayesRecSys/assets/base/css/bootstrap.css" />
  <link rel="stylesheet" href="/NaiveBayesRecSys/assets/base/css/bootstrap-responsive.css" />
  <link rel="stylesheet" href="/NaiveBayesRecSys/assets/base/css/common.css" />
  <link rel="stylesheet" href="/NaiveBayesRecSys/assets/base/css/ajph.css" />
  <link rel="stylesheet" href="/NaiveBayesRecSys                                                                                                                                                                                                                                                                  /assets/base/css/tipsy.css" />
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
  <jsp:include page="/WEB-INF/jsp/analytics.inc.jsp"></jsp:include>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/menus/top.inc.jsp"></jsp:include>
<div class="container">
<hgroup>

<% if(request.getAttribute("pageHeading") != null) { %>
  <h1 class="hero-unit">${pageHeading}</h1>
<% } %>

</hgroup>

    <div id="ajph">
      <div id="sub_ajph">
     
