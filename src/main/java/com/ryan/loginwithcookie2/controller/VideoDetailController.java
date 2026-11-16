package com.ryan.loginwithcookie2.controller;

import com.ryan.loginwithcookie2.model.Video;
import com.ryan.loginwithcookie2.service.VideoService;
import com.ryan.loginwithcookie2.service.impl.VideoServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/video/detail")
public class VideoDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final VideoService videoService = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/video");
            return;
        }

        try {
            int videoId = Integer.parseInt(idStr);
            // Tự động tăng lượt xem
            videoService.incrementViews(videoId);

            Video video = videoService.findById(videoId);
            if (video == null) {
                resp.sendRedirect(req.getContextPath() + "/video");
                return;
            }

            // Lấy danh sách video đề xuất
            List<Video> relatedVideos = videoService.findTop10Newest();
            // Lọc bớt video hiện tại
            relatedVideos.removeIf(v -> v.getVideoId() == videoId);

            req.setAttribute("video", video);
            req.setAttribute("relatedVideos", relatedVideos);
            req.getRequestDispatcher("/views/video-detail.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/video");
        }
    }
}
