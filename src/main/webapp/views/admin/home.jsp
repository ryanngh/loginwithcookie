<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang Quản Trị</title>
    <style>
        .admin-card { background: white; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.08); padding: 35px; margin-top: 40px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="admin-card">
            <h1 class="display-6 text-danger"><i class="fa fa-shield-alt"></i> Khu vực Quản trị viên</h1>
            <hr>
            <div class="alert alert-danger">
                <strong>Xin chào Admin:</strong> ${sessionScope.account.fullName} (${sessionScope.account.userName}) - Role ID: ${sessionScope.account.roleid}
            </div>
            <div class="mt-4">
                <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary me-2"><i class="fa fa-arrow-left"></i> Về Trang chủ</a>
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-primary me-2"><i class="fa fa-user"></i> Hồ sơ</a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger"><i class="fa fa-sign-out-alt"></i> Đăng xuất</a>
            </div>
        </div>
    </div>
</body>
</html>
