<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đặt lại mật khẩu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f4f6f9;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .reset-box {
            max-width: 420px;
            margin: 50px auto;
            background: #ffffff;
            padding: 35px 30px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.06);
        }
        .otp-input {
            letter-spacing: 8px;
            font-size: 22px;
            font-weight: bold;
            text-align: center;
        }
    </style>
</head>
<body>
    <jsp:include page="topbar.jsp" />

    <div class="container">
        <div class="reset-box">
            <h3 class="fw-bold text-center mb-2">Đặt lại mật khẩu</h3>
            <p class="text-muted small text-center mb-4">Nhập mã xác nhận và thiết lập mật khẩu mới.</p>

            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${alert}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/reset-password" method="post">
                <div class="mb-3">
                    <label class="form-label fw-semibold">Email</label>
                    <input type="email" name="email" class="form-control" value="${email}" placeholder="Email của bạn" required>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Mã xác nhận</label>
                    <input type="text" name="otp" class="form-control otp-input" maxlength="6" placeholder="000000" required autofocus>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Mật khẩu mới</label>
                    <input type="password" name="newPassword" class="form-control" placeholder="Mật khẩu mới" required>
                </div>

                <div class="mb-4">
                    <label class="form-label fw-semibold">Nhập lại mật khẩu mới</label>
                    <input type="password" name="confirmPassword" class="form-control" placeholder="Xác nhận mật khẩu mới" required>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">
                    Lưu mật khẩu
                </button>

                <div class="mt-4 text-center">
                    <a href="${pageContext.request.contextPath}/login" class="text-decoration-none small text-muted">
                        Quay lại đăng nhập
                    </a>
                </div>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
