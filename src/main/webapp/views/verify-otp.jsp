<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Xác nhận tài khoản</title>
    <style>
        .otp-box {
            max-width: 420px;
            margin: 60px auto;
            background: #ffffff;
            padding: 35px 30px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.06);
            text-align: center;
        }
        .otp-input {
            letter-spacing: 8px;
            font-size: 24px;
            font-weight: bold;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="otp-box">
            <h3 class="fw-bold mb-2">Xác nhận tài khoản</h3>
            <p class="text-muted small mb-4">Nhập mã xác nhận đã được gửi đến email của bạn.</p>

            <c:if test="${message != null}">
                <div class="alert alert-success alert-dismissible fade show text-start" role="alert">
                    ${message}
                </div>
            </c:if>

            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show text-start" role="alert">
                    ${alert}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/verify-otp" method="post" class="needs-validation" novalidate>
                <div class="mb-3 text-start">
                    <label class="form-label fw-semibold">Email</label>
                    <input type="email" name="email" class="form-control" value="${email}" placeholder="Email của bạn" required>
                    <div class="invalid-feedback">Vui lòng nhập email hợp lệ.</div>
                </div>

                <div class="mb-4 text-start">
                    <label class="form-label fw-semibold">Mã xác nhận</label>
                    <input type="text" name="otp" class="form-control otp-input"
                           maxlength="6" pattern="^[0-9]{6}$" placeholder="000000" required autofocus>
                    <div class="invalid-feedback">Mã xác nhận gồm đúng 6 chữ số.</div>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold mb-3">
                    Xác nhận
                </button>
            </form>

            <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top small">
                <span class="text-muted">Chưa nhận được mã?</span>
                <a href="${pageContext.request.contextPath}/resend-otp?email=${email}" class="text-decoration-none fw-semibold">
                    Gửi lại mã
                </a>
            </div>
            <div class="mt-2 text-center">
                <a href="${pageContext.request.contextPath}/login" class="text-decoration-none small text-muted">
                    Quay lại đăng nhập
                </a>
            </div>
        </div>
    </div>
</body>
</html>
