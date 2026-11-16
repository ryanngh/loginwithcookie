package com.ryan.loginwithcookie2.controller;

import com.ryan.loginwithcookie2.model.Video;
import com.ryan.loginwithcookie2.service.VideoService;
import com.ryan.loginwithcookie2.service.impl.VideoServiceImpl;
import com.ryan.loginwithcookie2.util.Constant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/home", "/"})
public class HomeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final VideoService videoService = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Hiển thị 10 video mới nhất lên trang chủ
        List<Video> newestVideos = videoService.findTop10Newest();
        req.setAttribute("videos", newestVideos);
        req.getRequestDispatcher(Constant.Path.HOME).forward(req, resp);
    }
}
