<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách Video</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
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
            font-size: 38px;
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
            font-weight: 600;
            color: #212529;
            line-height: 1.4;
            min-height: 42px;
        }
        .video-desc {
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            font-size: 13px;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <jsp:include page="topbar.jsp" />

    <div class="container my-4">
        <!-- Header & Action controls -->
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-2 border-bottom gap-2">
            <div>
                <h3 class="fw-bold mb-1">
                    <c:choose>
                        <c:when test="${viewingMode == 'my'}">Video của bạn</c:when>
                        <c:otherwise>Tất cả Video</c:otherwise>
                    </c:choose>
                </h3>
                <span class="text-muted small">
                    Trang ${currentPage} / ${totalPages} (${totalVideos} video)
                </span>
            </div>

            <div class="d-flex align-items-center gap-2">
                <c:if test="${sessionScope.account != null}">
                    <div class="btn-group me-2">
                        <a href="${pageContext.request.contextPath}/video?filter=my"
                           class="btn btn-sm ${viewingMode == 'my' ? 'btn-primary' : 'btn-outline-primary'}">
                            Của tôi
                        </a>
                        <a href="${pageContext.request.contextPath}/video?filter=all"
                           class="btn btn-sm ${viewingMode == 'all' ? 'btn-primary' : 'btn-outline-primary'}">
                            Tất cả
                        </a>
                    </div>
                    <a href="${pageContext.request.contextPath}/video/manage" class="btn btn-success btn-sm">
                        <i class="fa fa-plus"></i> Đăng video
                    </a>
                </c:if>
                <c:if test="${sessionScope.account == null}">
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-sm">
                        Đăng nhập
                    </a>
                </c:if>
            </div>
        </div>

        <!-- Video Grid (6 sp / trang) -->
        <c:choose>
            <c:when test="${videos != null && !videos.isEmpty()}">
                <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4 mb-4">
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
                                           class="text-decoration-none video-title"
                                           title="${v.title}">
                                            ${v.title}
                                        </a>
                                        <p class="video-desc mt-2 mb-0">
                                            ${v.description != null && !v.description.isEmpty() ? v.description : ''}
                                        </p>
                                    </div>

                                    <div class="mt-3 pt-2 border-top d-flex justify-content-between align-items-center text-muted small">
                                        <span><i class="fa fa-eye"></i> ${v.views} lượt xem</span>
                                        <span class="text-muted">
                                            ${v.user != null ? (v.user.fullName != null && !v.user.fullName.isEmpty() ? v.user.fullName : v.user.userName) : 'Admin'}
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- Pagination (Phân trang 6 sp / trang) -->
                <c:if test="${totalPages > 1}">
                    <nav aria-label="Page navigation" class="d-flex justify-content-center my-4">
                        <ul class="pagination pagination-sm shadow-sm">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/video?page=${currentPage - 1}&filter=${viewingMode}">
                                    Trước
                                </a>
                            </li>

                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/video?page=${i}&filter=${viewingMode}">${i}</a>
                                </li>
                            </c:forEach>

                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/video?page=${currentPage + 1}&filter=${viewingMode}">
                                    Sau
                                </a>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </c:when>
            <c:otherwise>
                <div class="card p-5 text-center shadow-sm">
                    <h4>Chưa có video nào</h4>
                    <p class="text-muted small">
                        <c:choose>
                            <c:when test="${viewingMode == 'my'}">
                                Bạn chưa đăng video nào.
                            </c:when>
                            <c:otherwise>
                                Chưa có video nào được đăng tải.
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <div class="mt-2">
                        <a href="${pageContext.request.contextPath}/video/manage" class="btn btn-primary btn-sm">
                            Thêm video mới
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
