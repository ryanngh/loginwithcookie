package com.ryan.loginwithcookie2.controller;

import com.ryan.loginwithcookie2.model.User;
import com.ryan.loginwithcookie2.service.UserService;
import com.ryan.loginwithcookie2.service.impl.UserServiceImpl;
import com.ryan.loginwithcookie2.util.Constant;
import com.ryan.loginwithcookie2.util.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");

        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            req.setAttribute("alert", "Vui lòng nhập đầy đủ các trường bắt buộc!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        User existU = service.get(username.trim());
        if (existU != null && (existU.getStatus() == null || existU.getStatus() == 1)) {
            req.setAttribute("alert", "Tài khoản đã tồn tại và đã được kích hoạt!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        User existE = service.getByEmail(email.trim());
        if (existE != null && (existE.getStatus() == null || existE.getStatus() == 1)) {
            req.setAttribute("alert", "Email này đã được đăng ký!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        String otp = EmailUtil.generateOtp();
        boolean isSuccess = service.registerWithOtp(username.trim(), password.trim(), email.trim(),
                fullname != null ? fullname.trim() : "",
                phone != null ? phone.trim() : "",
                otp);

        if (isSuccess) {
            HttpSession session = req.getSession(true);
            session.setAttribute("verifyEmail", email.trim());
            resp.sendRedirect(req.getContextPath() + "/verify-otp?email=" + email.trim());
        } else {
            req.setAttribute("alert", "Hệ thống gặp lỗi, đăng ký thất bại!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
        }
    }
}
