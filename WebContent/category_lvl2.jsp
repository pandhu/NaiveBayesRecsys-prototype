
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

   <%@ page import="com.coolonWeb.model.Item" %>
        <%@ page import="java.util.ArrayList" %>
        <%@ page import="com.coolonWeb.Config" %>
        <%@ page import="com.coolonWeb.model.Category" %>
        <%@ page import="java.net.URLEncoder" %>

        <jsp:include page="/WEB-INF/jsp/header.inc.jsp"></jsp:include>

        <h1>Daftar Barang</h1>
        <?php $baris = $hasilnya[0]?>
        <ol class="breadcrumb">
            <li><a href="<%=Config.SITE_URL+"/dashboard"%>">Dashboard</a></li>
            <li><a href="<%=Config.SITE_URL+"/category/lvl1?cat1="+ URLEncoder.encode((String) request.getAttribute("cat1"), "UTF-8")%>"><%=request.getAttribute("cat1")%></a></li>
            <li><a><%=request.getAttribute("cat2")%></a></li>
        </ol>
        <div class="col-md-6">
            <table class="table table-hover">
                <tr>
                    <th>No</th>
                    <th>Kategori</th>
                </tr>
                <%
                    int count = 1;
                    for(Category category : (ArrayList<Category>)request.getAttribute("categories")) {%>
                <tr>
                    <td><%=count++%></td><td><a href="<%=Config.SITE_URL+"/category/lvl3?cat1="+URLEncoder.encode((String) request.getAttribute("cat1"), "UTF-8")+"&cat2="+URLEncoder.encode((String) request.getAttribute("cat2"), "UTF-8")+"&cat3="+URLEncoder.encode(category.name, "UTF-8")%>"><%=category.name%></a></td>
                </tr>
                <%}%>
            </table>
        </div>
        <jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>
    </title>
</head>
<body>

</body>
</html>
