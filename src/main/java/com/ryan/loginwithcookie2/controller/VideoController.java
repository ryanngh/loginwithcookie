package com.ryan.loginwithcookie2.controller;

import com.ryan.loginwithcookie2.model.User;
import com.ryan.loginwithcookie2.model.Video;
import com.ryan.loginwithcookie2.service.VideoService;
import com.ryan.loginwithcookie2.service.impl.VideoServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/video")
public class VideoController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final VideoService videoService = new VideoServiceImpl();
    private static final int PAGE_SIZE = 6; // Phân trang 6 sp/trang

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("account") : null;

        int page = 1;
        String pageStr = req.getParameter("page");
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            } catch (NumberFormatException ignored) {}
        }

        List<Video> videos;
        int totalVideos;
        String filter = req.getParameter("filter");

        if (user != null && !"all".equals(filter)) {
            // Hiển thị tất cả video của user hiện tại, phân trang 6 video/trang
            videos = videoService.findByUserId(user.getId(), page - 1, PAGE_SIZE);
            totalVideos = videoService.countByUserId(user.getId());
            req.setAttribute("viewingMode", "my");
        } else {
            // Chưa đăng nhập hoặc chọn xem tất cả video công khai
            videos = videoService.findAll(page - 1, PAGE_SIZE);
            totalVideos = videoService.countAll();
            req.setAttribute("viewingMode", "all");
        }

        int totalPages = (int) Math.ceil((double) totalVideos / PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;

        req.setAttribute("videos", videos);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalVideos", totalVideos);

        req.getRequestDispatcher("/views/video.jsp").forward(req, resp);
    }
}
