<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang chủ</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .main-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            padding: 35px;
            margin-top: 40px;
        }
    </style>
</head>
<body>
    <jsp:include page="topbar.jsp" />

    <div class="container">
        <div class="main-card">
            <h1 class="display-6 text-primary"><i class="fa fa-home"></i> Chào mừng đến với Trang Chủ</h1>
            <hr>

            <c:choose>
                <c:when test="${sessionScope.account != null}">
                    <div class="alert alert-success">
                        <h4 class="mb-0"><i class="fa fa-check-circle"></i> Đăng nhập thành công!</h4>
                    </div>

                    <div class="card mt-3">
                        <div class="card-header bg-light">
                            <strong><i class="fa fa-info-circle"></i> Thông tin tài khoản</strong>
                        </div>
                        <div class="card-body">
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item"><strong>ID:</strong> ${sessionScope.account.id}</li>
                                <li class="list-group-item"><strong>Username:</strong> ${sessionScope.account.userName}</li>
                                <li class="list-group-item"><strong>Họ và tên:</strong> ${sessionScope.account.fullName}</li>
                                <li class="list-group-item"><strong>Email:</strong> ${sessionScope.account.email}</li>
                                <li class="list-group-item"><strong>Số điện thoại:</strong> ${sessionScope.account.phone}</li>
                                <li class="list-group-item"><strong>Vai trò:</strong> ${sessionScope.account.roleid}</li>
                                <li class="list-group-item"><strong>Ngày tạo:</strong> ${sessionScope.account.createdDate}</li>
                            </ul>
                        </div>
                    </div>

                    <div class="mt-4">
                        <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">
                            <i class="fa fa-sign-out-alt"></i> Đăng xuất
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info">
                        <p class="mb-0">Bạn chưa đăng nhập vào hệ thống. Vui lòng chọn thao tác bên dưới:</p>
                    </div>
                    <div class="mt-3">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary me-2">
                            <i class="fa fa-sign-in-alt"></i> Đăng nhập ngay
                        </a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-success">
                            <i class="fa fa-user-plus"></i> Đăng ký tài khoản mới
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
