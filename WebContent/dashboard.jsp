<%@ page import="com.coolonWeb.model.Item" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.coolonWeb.Config" %><%--
  Created by IntelliJ IDEA.
  User: root
  Date: 13/04/16
  Time: 10:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/jsp/header.inc.jsp"></jsp:include>

<h1>Daftar Barang</h1>
<?php $baris = $hasilnya[0]?>
<ol class="breadcrumb">

</ol>
<div class="col-md-6">
    <table class="table table-hover">

    </table>
</div>
<div class="col-md-6">
    <?php if(isset($rekomendasi)){
    ?>
    <?php
		if($rekomendasi['isError']==true){
				echo $rekomendasi['message'];
		}else{
				$hasil = $rekomendasi['result'];
		?>
    <table class="table table-hover">
        <tr>
            <th>Nama Produk Rekomendasi</th>
        </tr>

        <%for(Item item : (ArrayList<Item>)request.getAttribute("recommendedItems")) {%>
        <tr>
            <td><a href="<%=Config.SITE_URL+"item/detail?id="+item.id%>"><%=item.name%></a></td>
        </tr>
        <%}%>
	</table>

</div>
<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>
