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

@WebServlet(urlPatterns = "/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        req.setAttribute("inputEmail", email);

        if (!com.ryan.loginwithcookie2.util.ValidationUtil.isValidEmail(email)) {
            req.setAttribute("alert", "Địa chỉ email không hợp lệ.");
            req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
            return;
        }

        boolean sent = userService.sendForgotPasswordOtp(email.trim());
        if (sent) {
            HttpSession session = req.getSession(true);
            session.setAttribute("resetEmail", email.trim());
            resp.sendRedirect(req.getContextPath() + "/reset-password?email=" + email.trim());
        } else {
            req.setAttribute("alert", "Email không tồn tại trong hệ thống!");
            req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
        }
    }
}
