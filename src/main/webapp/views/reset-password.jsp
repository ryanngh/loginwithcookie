<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đặt lại mật khẩu</title>
    <style>
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
    <div class="container">
        <div class="reset-box">
            <h3 class="fw-bold text-center mb-2">Đặt lại mật khẩu</h3>
            <p class="text-muted small text-center mb-4">Nhập mã xác nhận và thiết lập mật khẩu mới.</p>

            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${alert}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/reset-password" method="post" class="needs-validation" novalidate>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Email</label>
                    <input type="email" name="email" class="form-control" value="${email}" placeholder="Email của bạn" required>
                    <div class="invalid-feedback">Vui lòng nhập địa chỉ email hợp lệ.</div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Mã xác nhận</label>
                    <input type="text" name="otp" class="form-control otp-input"
                           maxlength="6" pattern="^[0-9]{6}$" placeholder="000000" required autofocus>
                    <div class="invalid-feedback">Mã xác nhận gồm đúng 6 chữ số.</div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Mật khẩu mới</label>
                    <input type="password" name="newPassword" id="newPassword" class="form-control"
                           minlength="6" placeholder="Ít nhất 6 ký tự" required>
                    <div class="invalid-feedback">Mật khẩu mới phải có ít nhất 6 ký tự.</div>
                </div>

                <div class="mb-4">
                    <label class="form-label fw-semibold">Nhập lại mật khẩu mới</label>
                    <input type="password" name="confirmPassword" id="confirmPassword" class="form-control"
                           minlength="6" placeholder="Xác nhận mật khẩu mới" required>
                    <div class="invalid-feedback">Vui lòng nhập lại mật khẩu mới khớp với mật khẩu trên.</div>
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
</body>
</html>
