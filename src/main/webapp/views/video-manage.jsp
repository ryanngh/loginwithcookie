<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Video</title>
    <style>
        .manage-card {
            background: white;
            border-radius: 12px;
            padding: 24px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            margin-bottom: 25px;
        }
        .table-thumb {
            width: 70px;
            height: 45px;
            object-fit: cover;
            border-radius: 4px;
        }
    </style>
</head>
<body>
    <div class="container my-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="fw-bold text-dark mb-0">Quản lý Video</h3>
            <a href="${pageContext.request.contextPath}/video" class="btn btn-outline-secondary btn-sm">
                Xem danh sách video
            </a>
        </div>

        <c:if test="${alert != null}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${alert}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="row g-4">
            <!-- Form Thêm / Sửa Video -->
            <div class="col-lg-4">
                <div class="manage-card">
                    <h5 class="fw-bold mb-3">
                        <c:choose>
                            <c:when test="${videoEdit != null}">Chỉnh sửa video</c:when>
                            <c:otherwise>Thêm video mới</c:otherwise>
                        </c:choose>
                    </h5>

                    <form action="${pageContext.request.contextPath}${videoEdit != null ? '/video/edit' : '/video/create'}"
                          method="post"
                          enctype="multipart/form-data"
                          class="needs-validation" novalidate>

                        <c:if test="${videoEdit != null}">
                            <input type="hidden" name="videoId" value="${videoEdit.videoId}">
                        </c:if>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Tiêu đề <span class="text-danger">*</span></label>
                            <input type="text" name="title" class="form-control"
                                   value="${inputTitle != null ? inputTitle : (videoEdit != null ? videoEdit.title : '')}"
                                   maxlength="255" placeholder="Tiêu đề video" required>
                            <div class="invalid-feedback">Vui lòng nhập tiêu đề video (tối đa 255 ký tự).</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Ảnh bìa</label>
                            <input type="file" name="posterFile" class="form-control" accept="image/*">
                        </div>

                        <div class="mb-3">
                            <label class="form-label text-muted small">Hoặc đường dẫn ảnh</label>
                            <input type="text" name="poster" class="form-control form-control-sm"
                                   value="${inputPoster != null ? inputPoster : (videoEdit != null ? videoEdit.poster : '')}"
                                   pattern="^(https?://|/).*$" placeholder="https://...">
                            <div class="invalid-feedback">Đường dẫn phải bắt đầu bằng http://, https:// hoặc /.</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Tệp video</label>
                            <input type="file" name="videoFile" class="form-control" accept="video/mp4,video/*">
                        </div>

                        <div class="mb-3">
                            <label class="form-label text-muted small">Hoặc đường dẫn video</label>
                            <input type="text" name="url" class="form-control form-control-sm"
                                   value="${inputUrl != null ? inputUrl : (videoEdit != null ? videoEdit.url : '')}"
                                   pattern="^(https?://|/).*$" placeholder="https://...">
                            <div class="invalid-feedback">Đường dẫn phải bắt đầu bằng http://, https:// hoặc /.</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Mô tả</label>
                            <textarea name="description" class="form-control" rows="3" maxlength="4000"
                                      placeholder="Mô tả video">${inputDescription != null ? inputDescription : (videoEdit != null ? videoEdit.description : '')}</textarea>
                            <div class="invalid-feedback">Mô tả tối đa 4000 ký tự.</div>
                        </div>

                        <div class="mb-3 form-check">
                            <input type="checkbox" name="active" class="form-check-input" id="activeCheck" value="1"
                                   ${(inputActive != null ? inputActive : (videoEdit == null || videoEdit.active)) ? 'checked' : ''}>
                            <label class="form-check-label text-muted" for="activeCheck">Công khai video</label>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary fw-semibold">
                                ${videoEdit != null ? 'Lưu thay đổi' : 'Đăng video'}
                            </button>
                            <c:if test="${videoEdit != null}">
                                <a href="${pageContext.request.contextPath}/video/manage" class="btn btn-outline-secondary btn-sm">
                                    Hủy
                                </a>
                            </c:if>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Bảng danh sách Video -->
            <div class="col-lg-8">
                <div class="manage-card">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="fw-bold mb-0">Danh sách video</h5>
                        <span class="text-muted small">${videos.size()} video</span>
                    </div>

                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th style="width: 50px;">ID</th>
                                    <th style="width: 80px;">Ảnh</th>
                                    <th>Tiêu đề</th>
                                    <th style="width: 80px;">Lượt xem</th>
                                    <th style="width: 80px;">Trạng thái</th>
                                    <th style="width: 120px;" class="text-end">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="v" items="${videos}">
                                    <tr>
                                        <td><strong>#${v.videoId}</strong></td>
                                        <td>
                                            <img src="${v.poster}" class="table-thumb" alt="Poster"
                                                 onerror="this.src='https://picsum.photos/seed/default/600/360'">
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}"
                                               class="fw-semibold text-dark text-decoration-none">
                                                ${v.title}
                                            </a>
                                            <div class="text-muted small">
                                                ${v.user != null ? (v.user.fullName != null && !v.user.fullName.isEmpty() ? v.user.fullName : v.user.userName) : 'Admin'}
                                            </div>
                                        </td>
                                        <td>${v.views}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${v.active}">
                                                    <span class="badge bg-success-subtle text-success">Hiện</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary-subtle text-secondary">Ẩn</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-end">
                                            <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}"
                                               class="btn btn-sm btn-outline-primary" title="Xem">
                                                <i class="fa fa-play"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/video/manage?editId=${v.videoId}"
                                               class="btn btn-sm btn-outline-secondary" title="Sửa">
                                                <i class="fa fa-edit"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/video/delete?id=${v.videoId}"
                                               class="btn btn-sm btn-outline-danger" title="Xóa"
                                               onclick="return confirm('Bạn có chắc muốn xóa video này?');">
                                                <i class="fa fa-trash"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
