<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Xác nhận tài khoản</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f4f6f9;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
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
    <jsp:include page="topbar.jsp" />

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

            <form action="${pageContext.request.contextPath}/verify-otp" method="post">
                <div class="mb-3 text-start">
                    <label class="form-label fw-semibold">Email</label>
                    <input type="email" name="email" class="form-control" value="${email}" placeholder="Email của bạn" required>
                </div>

                <div class="mb-4 text-start">
                    <label class="form-label fw-semibold">Mã xác nhận</label>
                    <input type="text" name="otp" class="form-control otp-input" maxlength="6" placeholder="000000" required autofocus>
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
