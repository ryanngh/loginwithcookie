<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng ký tài khoản</title>
    <style>
        .register-box {
            max-width: 460px;
            margin: 40px auto;
            background: #ffffff;
            padding: 35px 30px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.06);
        }
        .register-box h2 {
            font-weight: 700;
            color: #212529;
            text-align: center;
            margin-bottom: 25px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="register-box">
            <h2>Tạo tài khoản</h2>

            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${alert}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post" class="needs-validation" novalidate>
                <div class="mb-3">
                    <label class="form-label fw-semibold">Tên tài khoản <span class="text-danger">*</span></label>
                    <input type="text" name="username" class="form-control"
                           value="${inputUsername}"
                           pattern="^[a-zA-Z0-9_.-]{3,50}$"
                           placeholder="Tên đăng nhập (3–50 ký tự)" required autofocus>
                    <div class="invalid-feedback">Tên tài khoản 3–50 ký tự, chỉ gồm chữ cái, số và _, ., -.</div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Mật khẩu <span class="text-danger">*</span></label>
                    <input type="password" name="password" class="form-control"
                           minlength="6" placeholder="Ít nhất 6 ký tự" required>
                    <div class="invalid-feedback">Mật khẩu phải có ít nhất 6 ký tự.</div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Email <span class="text-danger">*</span></label>
                    <input type="email" name="email" class="form-control"
                           value="${inputEmail}" placeholder="example@gmail.com" required>
                    <div class="invalid-feedback">Vui lòng nhập địa chỉ email hợp lệ.</div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Họ và tên</label>
                    <input type="text" name="fullname" class="form-control"
                           value="${inputFullname}" maxlength="100" placeholder="Họ và tên đầy đủ">
                    <div class="invalid-feedback">Họ và tên tối đa 100 ký tự.</div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Số điện thoại</label>
                    <input type="text" name="phone" class="form-control"
                           value="${inputPhone}"
                           pattern="^(0|\+84)[0-9]{9,10}$" placeholder="0901234567">
                    <div class="invalid-feedback">Số điện thoại không hợp lệ (ví dụ: 0901234567).</div>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">
                    Đăng ký
                </button>

                <div class="mt-4 text-center small">
                    <span class="text-muted">Đã có tài khoản? </span>
                    <a href="${pageContext.request.contextPath}/login" class="text-decoration-none fw-semibold">Đăng nhập</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
