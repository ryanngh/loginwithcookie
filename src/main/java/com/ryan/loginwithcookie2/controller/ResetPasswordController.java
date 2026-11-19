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

@WebServlet(urlPatterns = "/reset-password")
public class ResetPasswordController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                email = (String) session.getAttribute("resetEmail");
            }
        }
        req.setAttribute("email", email);
        req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String otp = req.getParameter("otp");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        req.setAttribute("email", email);

        if (!com.ryan.loginwithcookie2.util.ValidationUtil.isValidEmail(email)) {
            req.setAttribute("alert", "Địa chỉ email không hợp lệ.");
            req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
            return;
        }

        if (!com.ryan.loginwithcookie2.util.ValidationUtil.isValidOtp(otp)) {
            req.setAttribute("alert", "Mã xác nhận phải gồm đúng 6 chữ số.");
            req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
            return;
        }

        if (!com.ryan.loginwithcookie2.util.ValidationUtil.isValidPassword(newPassword, 6)) {
            req.setAttribute("alert", "Mật khẩu mới phải có ít nhất 6 ký tự.");
            req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("alert", "Mật khẩu xác nhận không khớp.");
            req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
            return;
        }

        boolean success = userService.resetPassword(email.trim(), otp.trim(), newPassword.trim());
        if (success) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.removeAttribute("resetEmail");
            }
            resp.sendRedirect(req.getContextPath() + "/login?msg=reset_success");
        } else {
            req.setAttribute("alert", "Mã OTP không đúng hoặc đã hết hạn!");
            req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
        }
    }
}
