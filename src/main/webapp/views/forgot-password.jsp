<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quên mật khẩu</title>
    <style>
        .forgot-box {
            max-width: 420px;
            margin: 60px auto;
            background: #ffffff;
            padding: 35px 30px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.06);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="forgot-box">
            <h3 class="fw-bold text-center mb-2">Quên mật khẩu</h3>
            <p class="text-muted small text-center mb-4">Nhập email tài khoản để nhận mã đặt lại mật khẩu.</p>

            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${alert}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/forgot-password" method="post" class="needs-validation" novalidate>
                <div class="mb-4">
                    <label class="form-label fw-semibold">Email</label>
                    <input type="email" name="email" class="form-control"
                           value="${inputEmail}" placeholder="example@gmail.com" required autofocus>
                    <div class="invalid-feedback">Vui lòng nhập địa chỉ email hợp lệ.</div>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">
                    Gửi mã xác nhận
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
