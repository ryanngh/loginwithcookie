package com.ryan.loginwithcookie2.controller;

import com.ryan.loginwithcookie2.model.User;
import com.ryan.loginwithcookie2.service.UserService;
import com.ryan.loginwithcookie2.service.impl.UserServiceImpl;
import com.ryan.loginwithcookie2.util.CloudinaryUtil;
import com.ryan.loginwithcookie2.util.Constant;
import com.ryan.loginwithcookie2.util.EmailUtil;
import com.ryan.loginwithcookie2.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Date;

@WebServlet(urlPatterns = {"/profile", "/profile/verify-email"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 20     // 20MB
)
public class ProfileController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User sessionUser = (session != null) ? (User) session.getAttribute("account") : null;
        if (sessionUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User dbUser = userService.findById(sessionUser.getId());
        if (dbUser != null) {
            session.setAttribute("account", dbUser);
        }

        if ("true".equals(req.getParameter("cancelEmailChange"))) {
            clearPendingEmailChange(session);
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        if ("true".equals(req.getParameter("resendEmailOtp"))) {
            String pendingEmail = (String) session.getAttribute("pendingNewEmail");
            if (pendingEmail != null) {
                String otp = EmailUtil.generateOtp();
                session.setAttribute("emailChangeOtp", otp);
                session.setAttribute("emailChangeOtpExpiry", new Date(System.currentTimeMillis() + 10 * 60 * 1000));
                EmailUtil.sendEmailChangeOtp(pendingEmail, sessionUser.getUserName(), otp);
                req.setAttribute("infoMsg", "Đã gửi lại mã xác nhận tới " + pendingEmail);
            }
        }

        req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User sessionUser = (session != null) ? (User) session.getAttribute("account") : null;
        if (sessionUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User dbUser = userService.findById(sessionUser.getId());
        if (dbUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String path = req.getServletPath();
        if ("/profile/verify-email".equals(path)) {
            handleVerifyEmailOtp(req, resp, session, dbUser);
            return;
        }

        handleUpdateProfile(req, resp, session, dbUser);
    }

    private void handleUpdateProfile(HttpServletRequest req, HttpServletResponse resp, HttpSession session, User dbUser)
            throws ServletException, IOException {
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");

        req.setAttribute("inputFullname", fullname);
        req.setAttribute("inputPhone", phone);
        req.setAttribute("inputEmail", email);

        if (ValidationUtil.isBlank(fullname) || !ValidationUtil.isValidLength(fullname, 100)) {
            req.setAttribute("alert", "Họ và tên không được để trống và tối đa 100 ký tự.");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        if (!ValidationUtil.isValidPhone(phone)) {
            req.setAttribute("alert", "Số điện thoại không hợp lệ (ví dụ: 0901234567).");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("alert", "Địa chỉ email không hợp lệ.");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        Part avatarPart = null;
        try {
            avatarPart = req.getPart("avatarFile");
        } catch (Exception ignored) {}

        if (avatarPart != null && avatarPart.getSize() > 0 && !ValidationUtil.isValidImagePart(avatarPart)) {
            req.setAttribute("alert", "Ảnh đại diện không hợp lệ (chỉ chấp nhận JPG, PNG, GIF, WEBP).");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        if (avatarPart != null && avatarPart.getSize() > 0) {
            try {
                String uploadedAvatarUrl = CloudinaryUtil.uploadImage(avatarPart, req.getContextPath());
                if (uploadedAvatarUrl != null) {
                    dbUser.setAvatar(uploadedAvatarUrl);
                }
            } catch (IOException e) {
                req.setAttribute("alert", "Lỗi khi tải ảnh lên: " + e.getMessage());
                req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
                return;
            }
        }

        dbUser.setFullName(fullname.trim());
        dbUser.setPhone(phone != null ? phone.trim() : "");

        String trimmedEmail = email.trim();
        boolean emailChanged = dbUser.getEmail() == null || !trimmedEmail.equalsIgnoreCase(dbUser.getEmail());

        if (emailChanged) {
            User existingByEmail = userService.getByEmail(trimmedEmail);
            if (existingByEmail != null && existingByEmail.getId() != dbUser.getId()) {
                req.setAttribute("alert", "Email mới đã được tài khoản khác sử dụng.");
                req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
                return;
            }

            // Lưu fullname, phone, avatar trước; email mới chờ xác nhận OTP
            userService.update(dbUser);
            session.setAttribute("account", dbUser);

            String otp = EmailUtil.generateOtp();
            session.setAttribute("pendingNewEmail", trimmedEmail);
            session.setAttribute("emailChangeOtp", otp);
            session.setAttribute("emailChangeOtpExpiry", new Date(System.currentTimeMillis() + 10 * 60 * 1000));

            EmailUtil.sendEmailChangeOtp(trimmedEmail, dbUser.getUserName(), otp);
            req.setAttribute("infoMsg", "Đã lưu thông tin. Mã xác nhận đã được gửi tới email mới (" + trimmedEmail + ").");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        userService.update(dbUser);
        session.setAttribute("account", dbUser);
        req.setAttribute("successMsg", "Cập nhật thông tin cá nhân thành công.");
        req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
    }

    private void handleVerifyEmailOtp(HttpServletRequest req, HttpServletResponse resp, HttpSession session, User dbUser)
            throws ServletException, IOException {
        String otp = req.getParameter("otp");
        String pendingNewEmail = (String) session.getAttribute("pendingNewEmail");
        String sessionOtp = (String) session.getAttribute("emailChangeOtp");
        Date expiry = (Date) session.getAttribute("emailChangeOtpExpiry");

        if (pendingNewEmail == null || sessionOtp == null || expiry == null) {
            req.setAttribute("alert", "Phiên xác nhận email đã hết hạn. Vui lòng cập nhật lại.");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        if (!ValidationUtil.isValidOtp(otp)) {
            req.setAttribute("alert", "Mã xác nhận phải gồm đúng 6 chữ số.");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        if (expiry.before(new Date())) {
            clearPendingEmailChange(session);
            req.setAttribute("alert", "Mã xác nhận đã hết hạn. Vui lòng gửi lại yêu cầu.");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        if (!otp.trim().equals(sessionOtp)) {
            req.setAttribute("alert", "Mã xác nhận không chính xác.");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        User existingByEmail = userService.getByEmail(pendingNewEmail);
        if (existingByEmail != null && existingByEmail.getId() != dbUser.getId()) {
            clearPendingEmailChange(session);
            req.setAttribute("alert", "Email mới đã được tài khoản khác sử dụng.");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        dbUser.setEmail(pendingNewEmail);
        userService.update(dbUser);
        session.setAttribute("account", dbUser);
        clearPendingEmailChange(session);

        req.setAttribute("successMsg", "Xác nhận thành công! Email mới đã được cập nhật.");
        req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
    }

    private void clearPendingEmailChange(HttpSession session) {
        session.removeAttribute("pendingNewEmail");
        session.removeAttribute("emailChangeOtp");
        session.removeAttribute("emailChangeOtpExpiry");
    }
}
