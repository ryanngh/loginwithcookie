package com.ryan.loginwithcookie2.controller;

import com.ryan.loginwithcookie2.model.User;
import com.ryan.loginwithcookie2.service.UserService;
import com.ryan.loginwithcookie2.service.impl.UserServiceImpl;
import com.ryan.loginwithcookie2.util.Constant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msg = req.getParameter("msg");
        if ("activated".equals(msg)) {
            req.setAttribute("successMsg", "Kích hoạt tài khoản thành công! Vui lòng đăng nhập.");
        } else if ("reset_success".equals(msg)) {
            req.setAttribute("successMsg", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập với mật khẩu mới.");
        }

        // 1. Kiểm tra Login với Session
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        // 2. Kiểm tra Login với Cookie (Remember Me)
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Constant.COOKIE_REMEMBER.equals(cookie.getName())) {
                    String username = cookie.getValue();
                    User user = service.get(username);
                    if (user != null && (user.getStatus() == null || user.getStatus() == 1)) {
                        session = req.getSession(true);
                        session.setAttribute("account", user);
                        resp.sendRedirect(req.getContextPath() + "/waiting");
                        return;
                    }
                }
            }
        }

        req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean isRememberMe = false;
        String remember = req.getParameter("remember");

        if ("on".equals(remember)) {
            isRememberMe = true;
        }

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            req.setAttribute("alert", "Tài khoản hoặc mật khẩu không được rỗng");
            req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
            return;
        }

        User user = service.login(username.trim(), password.trim());
        if (user != null) {
            // Kiểm tra trạng thái kích hoạt tài khoản
            if (user.getStatus() != null && user.getStatus() == 0) {
                req.setAttribute("alert", "Tài khoản chưa được kích hoạt qua mã OTP!");
                req.setAttribute("unverifiedEmail", user.getEmail());
                req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("account", user);

            if (isRememberMe) {
                saveRememberMe(resp, user.getUserName());
            }

            resp.sendRedirect(req.getContextPath() + "/waiting");
        } else {
            req.setAttribute("alert", "Tài khoản hoặc mật khẩu không đúng");
            req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
        }
    }

    private void saveRememberMe(HttpServletResponse response, String username) {
        Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, username);
        cookie.setMaxAge(30 * 60); // 30 phút
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
