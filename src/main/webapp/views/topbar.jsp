<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="topbar" style="background-color: #212529; color: #fff; padding: 12px 25px; display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #0d6efd;">
    <div style="font-size: 18px; font-weight: bold;">
        <a href="${pageContext.request.contextPath}/home" style="color: #fff; text-decoration: none;">
            <i class="fa fa-graduation-cap"></i> HCMUTE - Hệ Thống Đăng Nhập
        </a>
    </div>
    <div>
        <c:choose>
            <c:when test="${sessionScope.account == null}">
                <div class="auth-links" style="display: flex; gap: 15px;">
                    <a href="${pageContext.request.contextPath}/login" style="color: #fff; text-decoration: none; font-weight: 500;">
                        <i class="fa fa-sign-in"></i> Đăng nhập
                    </a>
                    <span style="color: #6c757d;">|</span>
                    <a href="${pageContext.request.contextPath}/register" style="color: #fff; text-decoration: none; font-weight: 500;">
                        <i class="fa fa-user-plus"></i> Đăng ký
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="user-links" style="display: flex; gap: 15px; align-items: center;">
                    <span style="color: #ffc107;">
                        <i class="fa fa-user-circle"></i> ${sessionScope.account.fullName}
                    </span>
                    <span style="color: #6c757d;">|</span>
                    <a href="${pageContext.request.contextPath}/logout" style="color: #ff6b6b; text-decoration: none; font-weight: bold;">
                        <i class="fa fa-sign-out"></i> Đăng Xuất
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
