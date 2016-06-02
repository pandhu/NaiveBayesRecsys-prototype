<%@ page import="com.coolonWeb.model.Item" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.coolonWeb.Config" %>
<%@ page import="com.coolonWeb.model.Category" %>
<%@ page import="java.net.URLEncoder" %><%--
  Created by IntelliJ IDEA.
  User: root
  Date: 13/04/16
  Time: 10:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/jsp/header.inc.jsp"></jsp:include>

<h1>Informasi Umum</h1>
<ol class="breadcrumb">
    <li>Informasi Umum</li>
</ol>

<div class="inner cover">
    <div class="col-md-12">
        Berikut ini merupakan informasi umum mengenai riset dan skenario yang dijalankan.
        <ol>
            <li>Riset ini menguji 2 buah metode Sistem Perekomendasi, Metode A dan Metode B.</li>
            <li>Pada riset ini Anda seolah-olah menjadi seorang user e-commerce.</li>
            <li>Pada bagian awal anda diharuskan memilih 3 barang (bisa barang yang sudah anda miliki).</li>
            <li>Skenario pada uji coba terbagi menjadi 3 bagian.</li>
            <li>Setiap bagian dilakukan pengujian sebagai berikut:
                <ul>
                    <li>Waktu komputasi Metode A</li>
                    <li>Waktu komputasi Metode B</li>
                    <li>Relevansi hasil Rekomendasi</li>
                </ul>
            </li>
            <li>Jika terjadi masalah/ingin bertanya lebih lanjut mengenai riset ini Anda dapat menghubungi kami di 085697912042 (Pandhu) atau pandhu.hutomo@ui.ac.id</li>
        </ol>
        <a class="btn btn-default" href="<%=Config.SITE_URL+"/survey/chooseItem"%>">Lanjutkan</a>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/footer.inc.jsp"></jsp:include>
