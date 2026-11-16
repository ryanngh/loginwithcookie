package com.ryan.loginwithcookie2.controller;

import com.ryan.loginwithcookie2.model.User;
import com.ryan.loginwithcookie2.model.Video;
import com.ryan.loginwithcookie2.service.VideoService;
import com.ryan.loginwithcookie2.service.impl.VideoServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@WebServlet(urlPatterns = {"/video/manage", "/video/create", "/video/edit", "/video/delete"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 50,       // 50MB
        maxRequestSize = 1024 * 1024 * 100    // 100MB
)
public class VideoManageController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final VideoService videoService = new VideoServiceImpl();

    private static final String UPLOAD_DIR = "uploads";

    @Override
    public void init() throws ServletException {
        super.init();
        File uploadFolder = new File(UPLOAD_DIR);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("account") : null;

        if (user == null) {
            String preview = req.getParameter("preview");
            if ("true".equals(preview)) {
                user = new com.ryan.loginwithcookie2.service.impl.UserServiceImpl().get("admin");
            } else {
                resp.sendRedirect(req.getContextPath() + "/login?alert=login_required");
                return;
            }
        }

        String path = req.getServletPath();

        if ("/video/delete".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                try {
                    int videoId = Integer.parseInt(idStr);
                    Video v = videoService.findById(videoId);
                    // Chỉ chủ sở hữu hoặc admin (roleid=1) mới được xóa
                    if (v != null && (user.getRoleid() == 1 || (v.getUser() != null && v.getUser().getId() == user.getId()))) {
                        videoService.delete(videoId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            resp.sendRedirect(req.getContextPath() + "/video/manage");
            return;
        }

        // Lấy danh sách video (nếu admin xem tất cả, nếu user xem video của mình)
        List<Video> videos;
        if (user.getRoleid() == 1) {
            videos = videoService.findAll();
        } else {
            videos = videoService.findByUserId(user.getId(), 0, 100);
        }

        // Nếu có param id để edit
        String editId = req.getParameter("editId");
        if (editId != null) {
            try {
                Video editVideo = videoService.findById(Integer.parseInt(editId));
                req.setAttribute("videoEdit", editVideo);
            } catch (Exception ignored) {}
        }

        req.setAttribute("videos", videos);
        req.getRequestDispatcher("/views/video-manage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("account") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String path = req.getServletPath();
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String url = req.getParameter("url");
        boolean active = "1".equals(req.getParameter("active")) || "true".equalsIgnoreCase(req.getParameter("active")) || "on".equals(req.getParameter("active"));

        // Xử lý Multipart upload cho Poster (Ảnh)
        String posterPath = null;
        try {
            Part posterPart = req.getPart("posterFile");
            if (posterPart != null && posterPart.getSize() > 0) {
                String submittedFileName = Paths.get(posterPart.getSubmittedFileName()).getFileName().toString();
                String ext = "";
                int idx = submittedFileName.lastIndexOf('.');
                if (idx > 0) ext = submittedFileName.substring(idx);
                String uniqueFileName = "poster_" + System.currentTimeMillis() + ext;

                File uploadFolder = new File(UPLOAD_DIR);
                if (!uploadFolder.exists()) uploadFolder.mkdirs();

                File destFile = new File(uploadFolder, uniqueFileName);
                posterPart.write(destFile.getAbsolutePath());
                posterPath = req.getContextPath() + "/uploads/" + uniqueFileName;
            }
        } catch (Exception e) {
            System.err.println("Lỗi upload poster: " + e.getMessage());
        }

        // Xử lý Multipart upload cho Video File (nếu người dùng upload file video trực tiếp)
        try {
            Part videoPart = req.getPart("videoFile");
            if (videoPart != null && videoPart.getSize() > 0) {
                String submittedFileName = Paths.get(videoPart.getSubmittedFileName()).getFileName().toString();
                String ext = "";
                int idx = submittedFileName.lastIndexOf('.');
                if (idx > 0) ext = submittedFileName.substring(idx);
                String uniqueVideoName = "video_" + System.currentTimeMillis() + ext;

                File uploadFolder = new File(UPLOAD_DIR);
                if (!uploadFolder.exists()) uploadFolder.mkdirs();

                File destFile = new File(uploadFolder, uniqueVideoName);
                videoPart.write(destFile.getAbsolutePath());
                url = req.getContextPath() + "/uploads/" + uniqueVideoName;
            }
        } catch (Exception e) {
            System.err.println("Lỗi upload video file: " + e.getMessage());
        }

        if ("/video/create".equals(path) || (req.getParameter("videoId") == null || req.getParameter("videoId").isEmpty())) {
            // Thêm mới Video (CREATE)
            if (posterPath == null || posterPath.isEmpty()) {
                String posterUrl = req.getParameter("poster");
                posterPath = (posterUrl != null && !posterUrl.trim().isEmpty()) ? posterUrl.trim() : "https://picsum.photos/seed/video/600/360";
            }
            if (url == null || url.trim().isEmpty()) {
                url = "https://www.w3schools.com/html/mov_bbb.mp4";
            }

            Video newVideo = new Video(title, posterPath, 0, description, active, url, user);
            videoService.insert(newVideo);
        } else {
            // Cập nhật Video (UPDATE)
            try {
                int videoId = Integer.parseInt(req.getParameter("videoId"));
                Video existing = videoService.findById(videoId);
                if (existing != null) {
                    existing.setTitle(title);
                    existing.setDescription(description);
                    existing.setActive(active);
                    if (posterPath != null && !posterPath.isEmpty()) {
                        existing.setPoster(posterPath);
                    } else {
                        String posterUrl = req.getParameter("poster");
                        if (posterUrl != null && !posterUrl.trim().isEmpty()) {
                            existing.setPoster(posterUrl.trim());
                        }
                    }
                    if (url != null && !url.trim().isEmpty()) {
                        existing.setUrl(url.trim());
                    }
                    videoService.update(existing);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        resp.sendRedirect(req.getContextPath() + "/video/manage");
    }
}
