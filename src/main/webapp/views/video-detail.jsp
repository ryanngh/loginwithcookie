<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${video.title}</title>
    <style>
        .player-container {
            background-color: #000;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 20px rgba(0,0,0,0.12);
        }
        video {
            width: 100%;
            max-height: 520px;
            display: block;
        }
        .video-meta-box {
            background: #ffffff;
            border-radius: 12px;
            padding: 24px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            margin-top: 20px;
        }
        .sidebar-card {
            background: #ffffff;
            border-radius: 12px;
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        .related-item {
            display: flex;
            gap: 12px;
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 1px solid #f1f1f1;
            text-decoration: none;
            color: inherit;
            transition: transform 0.2s;
        }
        .related-item:hover {
            transform: translateX(4px);
        }
        .related-thumb {
            width: 110px;
            height: 65px;
            object-fit: cover;
            border-radius: 6px;
            background-color: #000;
            flex-shrink: 0;
        }
        .related-title {
            font-size: 14px;
            font-weight: 600;
            line-height: 1.3;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }
    </style>
</head>
<body>
    <div class="container my-4">
        <!-- Breadcrumb -->
        <div class="d-flex justify-content-between align-items-center mb-3">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb mb-0 small">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-decoration-none">Trang chủ</a></li>
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/video" class="text-decoration-none">Video</a></li>
                    <li class="breadcrumb-item active" aria-current="page">${video.title}</li>
                </ol>
            </nav>
            <a href="javascript:history.back()" class="btn btn-outline-secondary btn-sm">
                Quay lại
            </a>
        </div>

        <div class="row g-4">
            <!-- Video Player & Chi tiết -->
            <div class="col-lg-8">
                <div class="player-container">
                    <c:choose>
                        <c:when test="${video.url.contains('youtube.com') || video.url.contains('youtu.be')}">
                            <div class="ratio ratio-16x9">
                                <iframe src="${video.url.replace('watch?v=', 'embed/')}"
                                        title="${video.title}"
                                        allowfullscreen></iframe>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <video controls autoplay poster="${video.poster}">
                                <source src="${video.url}" type="video/mp4">
                                Trình duyệt không hỗ trợ thẻ video.
                            </video>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="video-meta-box">
                    <h3 class="fw-bold text-dark mb-2">${video.title}</h3>

                    <div class="d-flex flex-wrap justify-content-between align-items-center py-2 border-bottom mb-3 text-muted small">
                        <div>
                            <span class="me-3"><i class="fa fa-eye"></i> ${video.views} lượt xem</span>
                        </div>
                        <div>
                            <c:if test="${sessionScope.account != null && (sessionScope.account.roleid == 1 || sessionScope.account.id == video.user.id)}">
                                <a href="${pageContext.request.contextPath}/video/manage?editId=${video.videoId}" class="btn btn-outline-secondary btn-sm me-1">
                                    Sửa
                                </a>
                                <a href="${pageContext.request.contextPath}/video/delete?id=${video.videoId}"
                                   class="btn btn-outline-danger btn-sm"
                                   onclick="return confirm('Bạn có chắc muốn xóa video này?');">
                                    Xóa
                                </a>
                            </c:if>
                        </div>
                    </div>

                    <!-- Người đăng -->
                    <div class="d-flex align-items-center p-3 bg-light rounded mb-3">
                        <div class="me-3">
                            <i class="fa fa-user-circle fa-2x text-secondary"></i>
                        </div>
                        <div>
                            <div class="fw-bold">
                                ${video.user != null ? (video.user.fullName != null && !video.user.fullName.isEmpty() ? video.user.fullName : video.user.userName) : 'Quản trị viên'}
                            </div>
                            <div class="text-muted small">
                                ${video.user != null ? video.user.email : ''}
                            </div>
                        </div>
                    </div>

                    <!-- Mô tả -->
                    <c:if test="${video.description != null && !video.description.isEmpty()}">
                        <h6 class="fw-bold mt-3 mb-2">Mô tả</h6>
                        <p class="text-secondary small" style="white-space: pre-line;">
                            ${video.description}
                        </p>
                    </c:if>
                </div>
            </div>

            <!-- Video đề xuất -->
            <div class="col-lg-4">
                <div class="sidebar-card">
                    <h6 class="fw-bold mb-3 pb-2 border-bottom">Video khác</h6>

                    <c:forEach var="rel" items="${relatedVideos}">
                        <a href="${pageContext.request.contextPath}/video/detail?id=${rel.videoId}" class="related-item">
                            <img src="${rel.poster}" class="related-thumb" alt="${rel.title}"
                                 onerror="this.src='https://picsum.photos/seed/default/600/360'">
                            <div class="d-flex flex-column justify-content-between">
                                <div class="related-title text-dark">${rel.title}</div>
                                <div class="text-muted small mt-1">
                                    <span><i class="fa fa-eye"></i> ${rel.views}</span>
                                </div>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
