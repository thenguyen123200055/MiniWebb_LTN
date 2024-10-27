<%-- 
    Document   : list_product
    Created on : Oct 22, 2024, 2:42:27 PM
    Author     : ADMIN
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.text.DecimalFormat"%>
<%@page import="model.Hoa"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>
    .navigate-arrow-left, .navigate-arrow-right {
        position: fixed;
        top: 50%;
        transform: translateY(-50%);
        z-index: 1000;
        font-size: 2rem;
        color: black;
    }
    .navigate-arrow-left {
        left: 20px; 
    }
    .navigate-arrow-right {
        right: 20px; 
    }
</style>

<jsp:include page="../shared/header.jsp"/>
<jsp:include page="../shared/nav.jsp"/>

<div class="container">
    <h2> Danh sách sản phẩm</h2>
    <div class="mb-2 text-end">
        <a href="ManageProduct?action=add" class="btn btn-success"> <i class="bi bi-plus-circle"></i> Thêm mới</a>
    </div>
    <table class="table table-bordered table-striped">
        <tr>
            <th>Tên hoa</th>
            <th>Giá</th>
            <th>Hình ảnh</th>
            <th>Loại</th>
            <th>Action</th>
        </tr>  

        <%
            DecimalFormat fmt = new DecimalFormat("#,##0");
            List<Hoa> productList = (List<Hoa>) request.getAttribute("productList");
            if (productList != null && !productList.isEmpty()) {
                for (Hoa x : productList) {
        %>
        <tr>
            <td><%=x.getTenhoa()%></td>
            <td><%=fmt.format(x.getGia())%></td>
            <td><img src="assets/images/products/<%=x.getHinh()%>" style="width: 100px"></td>
            <td><%=x.getMaloai()%></td>
            <td>
                <a href="ManageProduct?action=edit&mahoa=<%=x.getMahoa()%>" class="btn btn-secondary"> <i class="bi bi-pencil-square"></i> Sửa</a>
                <a href="ManageProduct?action=delete&mahoa=<%=x.getMahoa()%>" class="btn btn-danger"> <i class="bi bi-trash"></i> Xoá</a>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr><td colspan="5">Không có sản phẩm nào để hiển thị.</td></tr>
        <%
            }
        %>
    </table>

<c:if test="${currentPage > 1}">
    <a href="ManageProduct?page=${currentPage - 1}" class="navigate-arrow-left">
        <i class="bi bi-arrow-left-circle"></i>
    </a>
</c:if>
<c:if test="${currentPage < totalPages}">
    <a href="ManageProduct?page=${currentPage + 1}" class="navigate-arrow-right">
        <i class="bi bi-arrow-right-circle"></i>
    </a>
</c:if>

<jsp:include page="../shared/footer.jsp" />

<script>
    const currentPage = ${currentPage};
    const totalPages = ${totalPages};

    // Lắng nghe sự kiện nhấn phím
    document.addEventListener('keydown', function (event) {
        if (event.key === 'ArrowLeft' && currentPage > 1) { 
            window.location.href = "ManageProduct?page=" + (currentPage - 1);
        } else if (event.key === 'ArrowRight' && currentPage < totalPages) { 
            window.location.href = "ManageProduct?page=" + (currentPage + 1);
        }
    });
</script>

