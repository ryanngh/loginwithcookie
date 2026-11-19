<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang chủ</title>
    <style>
        .hero-banner {
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            color: white;
            padding: 28px 24px;
            border-radius: 12px;
            margin-bottom: 30px;
        }
        .video-card {
            border: none;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            height: 100%;
            background: #fff;
        }
        .video-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 6px 18px rgba(0,0,0,0.1);
        }
        .video-thumb-container {
            position: relative;
            width: 100%;
            padding-top: 56.25%;
            background-color: #000;
        }
        .video-thumb {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        .video-play-overlay {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            font-size: 36px;
            color: rgba(255, 255, 255, 0.85);
            transition: color 0.2s, transform 0.2s;
        }
        .video-card:hover .video-play-overlay {
            color: #0d6efd;
            transform: translate(-50%, -50%) scale(1.1);
        }
        .video-title {
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            font-size: 15px;
            line-height: 1.4;
            min-height: 42px;
        }
    </style>
</head>
<body>
    <div class="container my-4">
        <!-- Banner chào mừng -->
        <div class="hero-banner shadow-sm">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h1 class="h3 fw-bold mb-2">
                        Chào mừng đến với HCMUTE Video
                    </h1>
                    <p class="mb-0 text-light opacity-90">
                        Khám phá các video nổi bật và quản lý nội dung dễ dàng.
                    </p>
                </div>
                <div class="col-md-4 text-md-end mt-3 mt-md-0">
                    <c:choose>
                        <c:when test="${sessionScope.account != null}">
                            <div class="bg-white text-dark p-3 rounded shadow-sm text-start">
                                <div class="fw-bold text-primary mb-1">
                                    <i class="fa fa-user-circle"></i> ${sessionScope.account.fullName != null && !sessionScope.account.fullName.isEmpty() ? sessionScope.account.fullName : sessionScope.account.userName}
                                </div>
                                <div class="small text-muted mb-2">${sessionScope.account.email}</div>
                                <a href="${pageContext.request.contextPath}/profile" class="btn btn-sm btn-outline-primary me-1">
                                    Hồ sơ
                                </a>
                                <a href="${pageContext.request.contextPath}/video" class="btn btn-sm btn-primary me-1">
                                    Video của tôi
                                </a>
                                <a href="${pageContext.request.contextPath}/video/manage" class="btn btn-sm btn-outline-dark">
                                    Thêm video
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-warning fw-bold px-3 py-2 me-2">
                                Đăng nhập
                            </a>
                            <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-light fw-bold px-3 py-2">
                                Đăng ký
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Section: Video mới nhất -->
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="fw-bold mb-0 text-dark">
                Video mới nhất
            </h4>
            <a href="${pageContext.request.contextPath}/video" class="btn btn-outline-primary btn-sm">
                Xem tất cả <i class="fa fa-arrow-right ms-1"></i>
            </a>
        </div>

        <c:choose>
            <c:when test="${videos != null && !videos.isEmpty()}">
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-5 g-3">
                    <c:forEach var="v" items="${videos}">
                        <div class="col">
                            <div class="card video-card">
                                <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" class="text-decoration-none">
                                    <div class="video-thumb-container">
                                        <img src="${v.poster}" class="video-thumb" alt="${v.title}"
                                             onerror="this.src='https://picsum.photos/seed/default/600/360'">
                                        <div class="video-play-overlay">
                                            <i class="fa fa-circle-play"></i>
                                        </div>
                                    </div>
                                </a>
                                <div class="card-body p-3 d-flex flex-column justify-content-between">
                                    <div>
                                        <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}"
                                           class="text-decoration-none fw-semibold text-dark video-title"
                                           title="${v.title}">
                                            ${v.title}
                                        </a>
                                    </div>
                                    <div class="mt-2 pt-2 border-top d-flex justify-content-between align-items-center text-muted small">
                                        <span><i class="fa fa-eye"></i> ${v.views}</span>
                                        <span class="text-truncate" style="max-width: 90px;">
                                            ${v.user != null ? (v.user.fullName != null && !v.user.fullName.isEmpty() ? v.user.fullName : v.user.userName) : 'Admin'}
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-secondary text-center py-4">
                    <p class="mb-0">Chưa có video nào.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
