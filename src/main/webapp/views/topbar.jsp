<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark px-3 shadow-sm border-bottom border-primary">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold text-white" href="${pageContext.request.contextPath}/home">
            <i class="fa fa-play-circle text-primary me-2"></i>HCMUTE Video
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#topbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="topbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/home">
                        <i class="fa fa-home"></i> Trang chủ
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/video">
                        <i class="fa fa-film"></i> Video
                    </a>
                </li>
                <c:if test="${sessionScope.account != null}">
                    <li class="nav-item">
                        <a class="nav-link text-warning" href="${pageContext.request.contextPath}/video/manage">
                            <i class="fa fa-cog"></i> Quản lý Video
                        </a>
                    </li>
                </c:if>
            </ul>
            <div class="d-flex align-items-center">
                <c:choose>
                    <c:when test="${sessionScope.account == null}">
                        <div class="auth-links d-flex gap-2">
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-sm">
                                <i class="fa fa-sign-in-alt"></i> Đăng nhập
                            </a>
                            <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">
                                <i class="fa fa-user-plus"></i> Đăng ký
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="user-links d-flex align-items-center gap-2">
                            <a href="${pageContext.request.contextPath}/profile"
                               class="btn btn-outline-light btn-sm d-flex align-items-center gap-2"
                               title="Hồ sơ cá nhân">
                                <c:choose>
                                    <c:when test="${sessionScope.account.avatar != null && !sessionScope.account.avatar.isEmpty()}">
                                        <img src="${sessionScope.account.avatar}" alt="Avatar"
                                             style="width: 22px; height: 22px; border-radius: 50%; object-fit: cover;">
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa fa-user-circle"></i>
                                    </c:otherwise>
                                </c:choose>
                                <span>${sessionScope.account.fullName != null && !sessionScope.account.fullName.isEmpty() ? sessionScope.account.fullName : sessionScope.account.userName}</span>
                            </a>
                            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">
                                <i class="fa fa-sign-out-alt"></i> Đăng xuất
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>
