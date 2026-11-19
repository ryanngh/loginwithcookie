<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập</title>
    <style>
        .login-box {
            max-width: 420px;
            margin: 60px auto;
            background: #ffffff;
            padding: 35px 30px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.06);
        }
        .login-box h2 {
            font-weight: 700;
            color: #212529;
            text-align: center;
            margin-bottom: 25px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-box">
            <h2>Đăng nhập</h2>

            <c:if test="${successMsg != null}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${successMsg}
                </div>
            </c:if>

            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${alert}
                    <c:if test="${unverifiedEmail != null}">
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/verify-otp?email=${unverifiedEmail}" class="btn btn-sm btn-outline-danger">
                                Nhập mã xác nhận
                            </a>
                        </div>
                    </c:if>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post" class="needs-validation" novalidate>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Tài khoản hoặc Email</label>
                    <input type="text" name="username" class="form-control"
                           value="${inputUsername}" placeholder="Tên đăng nhập hoặc email" required autofocus>
                    <div class="invalid-feedback">Vui lòng nhập tài khoản hoặc email.</div>
                </div>

                <div class="mb-3">
                    <div class="d-flex justify-content-between">
                        <label class="form-label fw-semibold">Mật khẩu</label>
                        <a href="${pageContext.request.contextPath}/forgot-password" class="text-decoration-none small text-danger">
                            Quên mật khẩu?
                        </a>
                    </div>
                    <input type="password" name="password" class="form-control" placeholder="Mật khẩu" required>
                    <div class="invalid-feedback">Vui lòng nhập mật khẩu.</div>
                </div>

                <div class="mb-3 form-check">
                    <input type="checkbox" name="remember" class="form-check-input" id="rememberMe">
                    <label class="form-check-label text-muted" for="rememberMe">Ghi nhớ đăng nhập</label>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">
                    Đăng nhập
                </button>

                <div class="mt-4 text-center small">
                    <span class="text-muted">Chưa có tài khoản? </span>
                    <a href="${pageContext.request.contextPath}/register" class="text-decoration-none fw-semibold">Đăng ký ngay</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
