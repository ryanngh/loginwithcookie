<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hồ sơ cá nhân</title>
    <style>
        .profile-card {
            background: #ffffff;
            border-radius: 12px;
            padding: 28px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.06);
        }
        .avatar-preview {
            width: 110px;
            height: 110px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #e9ecef;
            background-color: #f8f9fa;
        }
        .otp-input {
            letter-spacing: 8px;
            font-size: 20px;
            font-weight: bold;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container my-4">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h3 class="fw-bold mb-0">Hồ sơ cá nhân</h3>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary btn-sm">
                        Trang chủ
                    </a>
                </div>

                <c:if test="${successMsg != null}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${successMsg}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <c:if test="${infoMsg != null}">
                    <div class="alert alert-info alert-dismissible fade show" role="alert">
                        ${infoMsg}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <c:if test="${alert != null}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${alert}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Form xác nhận OTP khi thay đổi Email mới -->
                <c:if test="${sessionScope.pendingNewEmail != null}">
                    <div class="profile-card mb-4 border border-primary">
                        <h5 class="fw-bold text-primary mb-2">Xác nhận email mới</h5>
                        <p class="text-muted small mb-3">
                            Mã xác nhận đã được gửi tới <strong>${sessionScope.pendingNewEmail}</strong>.
                        </p>

                        <form action="${pageContext.request.contextPath}/profile/verify-email"
                              method="post" class="needs-validation" novalidate>
                            <div class="row g-2 align-items-start">
                                <div class="col-sm-6">
                                    <input type="text" name="otp" class="form-control otp-input"
                                           maxlength="6" pattern="^[0-9]{6}$" placeholder="000000" required>
                                    <div class="invalid-feedback">Vui lòng nhập mã xác nhận gồm 6 chữ số.</div>
                                </div>
                                <div class="col-sm-6 d-flex gap-2">
                                    <button type="submit" class="btn btn-primary fw-semibold">
                                        Xác nhận
                                    </button>
                                    <a href="${pageContext.request.contextPath}/profile?resendEmailOtp=true"
                                       class="btn btn-outline-secondary">
                                        Gửi lại
                                    </a>
                                    <a href="${pageContext.request.contextPath}/profile?cancelEmailChange=true"
                                       class="btn btn-outline-danger">
                                        Hủy
                                    </a>
                                </div>
                            </div>
                        </form>
                    </div>
                </c:if>

                <!-- Form cập nhật thông tin cá nhân -->
                <div class="profile-card">
                    <form action="${pageContext.request.contextPath}/profile"
                          method="post"
                          enctype="multipart/form-data"
                          class="needs-validation" novalidate>

                        <div class="d-flex flex-wrap align-items-center gap-4 pb-4 mb-4 border-bottom">
                            <c:choose>
                                <c:when test="${sessionScope.account.avatar != null && !sessionScope.account.avatar.isEmpty()}">
                                    <img id="avatarPreview" src="${sessionScope.account.avatar}" class="avatar-preview" alt="Avatar">
                                </c:when>
                                <c:otherwise>
                                    <img id="avatarPreview" src="https://ui-avatars.com/api/?name=${sessionScope.account.userName}&background=0d6efd&color=fff&size=128"
                                         class="avatar-preview" alt="Avatar">
                                </c:otherwise>
                            </c:choose>

                            <div class="flex-grow-1">
                                <div class="fw-bold fs-5">${sessionScope.account.userName}</div>
                                <div class="text-muted small mb-2">Email hiện tại: ${sessionScope.account.email}</div>
                                <label class="form-label small fw-semibold mb-1">Đổi ảnh đại diện</label>
                                <input type="file" name="avatarFile" id="avatarFile" class="form-control form-control-sm"
                                       accept="image/*" onchange="previewAvatar(event)">
                            </div>
                        </div>

                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Họ và tên <span class="text-danger">*</span></label>
                                <input type="text" name="fullname" class="form-control"
                                       value="${inputFullname != null ? inputFullname : sessionScope.account.fullName}"
                                       maxlength="100" required>
                                <div class="invalid-feedback">Vui lòng nhập họ và tên (tối đa 100 ký tự).</div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Số điện thoại</label>
                                <input type="text" name="phone" class="form-control"
                                       value="${inputPhone != null ? inputPhone : sessionScope.account.phone}"
                                       pattern="^(0|\+84)[0-9]{9,10}$"
                                       placeholder="0901234567">
                                <div class="invalid-feedback">Số điện thoại không hợp lệ (ví dụ: 0901234567).</div>
                            </div>

                            <div class="col-12">
                                <label class="form-label fw-semibold">Email <span class="text-danger">*</span></label>
                                <input type="email" name="email" class="form-control"
                                       value="${inputEmail != null ? inputEmail : sessionScope.account.email}"
                                       required>
                                <div class="invalid-feedback">Vui lòng nhập địa chỉ email hợp lệ.</div>
                                <div class="form-text small">Nếu thay đổi sang email mới, bạn cần nhập mã xác nhận gửi về email mới đó.</div>
                            </div>
                        </div>

                        <div class="mt-4 pt-3 border-top text-end">
                            <button type="submit" class="btn btn-primary px-4 fw-semibold">
                                Lưu thay đổi
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        function previewAvatar(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    document.getElementById('avatarPreview').src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        }
    </script>
</body>
</html>
