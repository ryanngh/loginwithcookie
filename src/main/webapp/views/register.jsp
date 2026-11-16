<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng ký tài khoản</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f4f6f9;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
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
    <jsp:include page="topbar.jsp" />

    <div class="container">
        <div class="register-box">
            <h2>Tạo tài khoản</h2>

            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${alert}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="mb-3">
                    <label class="form-label fw-semibold">Tên tài khoản <span class="text-danger">*</span></label>
                    <input type="text" name="username" class="form-control" placeholder="Tên đăng nhập" required autofocus>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Mật khẩu <span class="text-danger">*</span></label>
                    <input type="password" name="password" class="form-control" placeholder="Mật khẩu" required>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Email <span class="text-danger">*</span></label>
                    <input type="email" name="email" class="form-control" placeholder="example@gmail.com" required>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Họ và tên</label>
                    <input type="text" name="fullname" class="form-control" placeholder="Họ và tên đầy đủ">
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Số điện thoại</label>
                    <input type="text" name="phone" class="form-control" placeholder="Số điện thoại">
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
