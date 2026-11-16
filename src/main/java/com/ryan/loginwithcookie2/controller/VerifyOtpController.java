package com.ryan.loginwithcookie2.controller;

import com.ryan.loginwithcookie2.service.UserService;
import com.ryan.loginwithcookie2.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = {"/verify-otp", "/resend-otp"})
public class VerifyOtpController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/resend-otp".equals(path)) {
            String email = req.getParameter("email");
            if (email == null || email.trim().isEmpty()) {
                HttpSession session = req.getSession(false);
                if (session != null) {
                    email = (String) session.getAttribute("verifyEmail");
                }
            }
            if (email != null && !email.trim().isEmpty()) {
                userService.resendOtp(email);
                req.setAttribute("message", "Mã OTP mới đã được gửi lại vào email của bạn!");
            }
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
            return;
        }

        String email = req.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                email = (String) session.getAttribute("verifyEmail");
            }
        }
        req.setAttribute("email", email);
        req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String otp = req.getParameter("otp");

        if (email == null || email.trim().isEmpty() || otp == null || otp.trim().isEmpty()) {
            req.setAttribute("alert", "Vui lòng nhập đầy đủ email và mã OTP!");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
            return;
        }

        boolean success = userService.verifyOtpAndActivate(email.trim(), otp.trim());
        if (success) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.removeAttribute("verifyEmail");
            }
            resp.sendRedirect(req.getContextPath() + "/login?msg=activated");
        } else {
            req.setAttribute("alert", "Mã OTP không chính xác hoặc đã hết hạn (10 phút)!");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
        }
    }
}
