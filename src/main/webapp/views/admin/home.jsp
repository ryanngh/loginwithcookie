<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang Quản Trị (Admin)</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body { background-color: #f8f9fa; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        .admin-card { background: white; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.08); padding: 35px; margin-top: 40px; }
    </style>
</head>
<body>
    <jsp:include page="../topbar.jsp" />

    <div class="container">
        <div class="admin-card">
            <h1 class="display-6 text-danger"><i class="fa fa-shield-alt"></i> Khu vực Quản trị viên (Admin Home)</h1>
            <hr>
            <div class="alert alert-danger">
                <strong>Xin chào Admin:</strong> ${sessionScope.account.fullName} (${sessionScope.account.userName}) - Role ID: ${sessionScope.account.roleid}
            </div>
            <div class="mt-4">
                <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary me-2"><i class="fa fa-arrow-left"></i> Về Trang chủ</a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger"><i class="fa fa-sign-out-alt"></i> Đăng xuất</a>
            </div>
        </div>
    </div>
</body>
</html>
