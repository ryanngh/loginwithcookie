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
            max-width: 480px;
            margin: 40px auto;
            background: #ffffff;
            padding: 35px 30px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .register-box h2 {
            font-weight: 700;
            color: #333;
            text-align: center;
            margin-bottom: 25px;
        }
    </style>
</head>
<body>
    <jsp:include page="topbar.jsp" />

    <div class="container">
        <div class="register-box">
            <h2><i class="fa fa-user-plus text-success"></i> Tạo tài khoản mới</h2>

            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fa fa-exclamation-triangle"></i> ${alert}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="mb-3">
                    <label class="form-label font-weight-bold">Tài khoản (Username) <span class="text-danger">*</span></label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa fa-user"></i></span>
                        <input type="text" name="username" class="form-control" placeholder="Nhập tài khoản" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label font-weight-bold">Mật khẩu <span class="text-danger">*</span></label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa fa-lock"></i></span>
                        <input type="password" name="password" class="form-control" placeholder="Nhập mật khẩu" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label font-weight-bold">Họ và tên</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa fa-id-card"></i></span>
                        <input type="text" name="fullname" class="form-control" placeholder="Nhập họ và tên đầy đủ">
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label font-weight-bold">Email <span class="text-danger">*</span></label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa fa-envelope"></i></span>
                        <input type="email" name="email" class="form-control" placeholder="example@email.com" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label font-weight-bold">Số điện thoại</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa fa-phone"></i></span>
                        <input type="text" name="phone" class="form-control" placeholder="Nhập số điện thoại">
                    </div>
                </div>

                <button type="submit" class="btn btn-success w-100 py-2">
                    <i class="fa fa-check-circle"></i> Đăng ký
                </button>

                <div class="mt-4 text-center">
                    <span>Đã có tài khoản? </span>
                    <a href="${pageContext.request.contextPath}/login" class="text-decoration-none font-weight-bold">Đăng nhập tại đây</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
