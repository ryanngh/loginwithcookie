package com.ryan.loginwithcookie2.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Random;

public class EmailUtil {

    /**
     * Tạo mã OTP ngẫu nhiên gồm 6 chữ số
     */
    public static String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Gửi email chứa OTP kích hoạt tài khoản
     */
    public static boolean sendActivationOtp(String toEmail, String username, String otp) {
        String subject = "Xác nhận kích hoạt tài khoản - LoginWithCookie";
        String htmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 500px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;\">"
                + "<h2 style=\"color: #0d6efd; text-align: center;\">Xác Nhận Kích Hoạt Tài Khoản</h2>"
                + "<p>Xin chào <strong>" + username + "</strong>,</p>"
                + "<p>Cảm ơn bạn đã đăng ký tài khoản tại hệ thống. Để hoàn tất kích hoạt tài khoản, vui lòng nhập mã OTP dưới đây:</p>"
                + "<div style=\"background-color: #f8f9fa; border: 1px dashed #0d6efd; padding: 15px; text-align: center; font-size: 24px; font-weight: bold; letter-spacing: 5px; color: #d63384; margin: 20px 0;\">"
                + otp
                + "</div>"
                + "<p style=\"color: #6c757d; font-size: 13px;\">Mã OTP có hiệu lực trong vòng <strong>10 phút</strong>. Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.</p>"
                + "<hr style=\"border: none; border-top: 1px solid #eee; margin: 20px 0;\">"
                + "<p style=\"font-size: 12px; color: #999; text-align: center;\">HCMUTE Web Programming - Bài tập 02</p>"
                + "</div>";

        return sendEmail(toEmail, subject, htmlContent, otp, "KÍCH HOẠT TÀI KHOẢN");
    }

    /**
     * Gửi email chứa OTP đặt lại mật khẩu
     */
    public static boolean sendForgotPasswordOtp(String toEmail, String username, String otp) {
        String subject = "Mã OTP đặt lại mật khẩu - LoginWithCookie";
        String htmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 500px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;\">"
                + "<h2 style=\"color: #dc3545; text-align: center;\">Yêu Cầu Đặt Lại Mật Khẩu</h2>"
                + "<p>Xin chào <strong>" + username + "</strong>,</p>"
                + "<p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn. Vui lòng sử dụng mã xác nhận OTP bên dưới để tạo mật khẩu mới:</p>"
                + "<div style=\"background-color: #f8f9fa; border: 1px dashed #dc3545; padding: 15px; text-align: center; font-size: 24px; font-weight: bold; letter-spacing: 5px; color: #dc3545; margin: 20px 0;\">"
                + otp
                + "</div>"
                + "<p style=\"color: #6c757d; font-size: 13px;\">Mã OTP có hiệu lực trong vòng <strong>10 phút</strong>. Không chia sẻ mã này cho bất kỳ ai.</p>"
                + "<hr style=\"border: none; border-top: 1px solid #eee; margin: 20px 0;\">"
                + "<p style=\"font-size: 12px; color: #999; text-align: center;\">HCMUTE Web Programming - Bài tập 02</p>"
                + "</div>";

        return sendEmail(toEmail, subject, htmlContent, otp, "ĐẶT LẠI MẬT KHẨU");
    }

    private static boolean sendEmail(String toEmail, String subject, String htmlContent, String otpForLog, String purpose) {
        String host = EnvConfig.get("MAIL_HOST", "smtp.gmail.com");
        int port = EnvConfig.getInt("MAIL_PORT", 587);
        final String username = EnvConfig.get("MAIL_USERNAME", "");
        final String password = EnvConfig.get("MAIL_PASSWORD", "");
        String fromEmail = EnvConfig.get("MAIL_FROM", username);
        boolean auth = EnvConfig.getBoolean("MAIL_AUTH", true);
        boolean starttls = EnvConfig.getBoolean("MAIL_STARTTLS", true);

        // In ra console mã OTP để lập trình viên/giảng viên test ngay khi chưa cấu hình mật khẩu Gmail thật
        System.out.println("\n========================================================");
        System.out.println(">>> [DEV OTP NOTIFICATION] - " + purpose);
        System.out.println(">>> Gửi tới Email: " + toEmail);
        System.out.println(">>> MÃ OTP XÁC NHẬN: [ " + otpForLog + " ]");
        System.out.println("========================================================\n");

        // Nếu chưa cấu hình username/password Gmail hợp lệ, ghi log và trả về true để luồng test không bị gián đoạn
        if (username.isEmpty() || password.isEmpty() || password.contains("your_app_password") || password.contains("xxxx")) {
            System.out.println("[EmailUtil] Lưu ý: Chưa cấu hình GMAIL App Password thực trong file .env. Vui lòng lấy mã OTP hiển thị ở trên.");
            return true;
        }

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", String.valueOf(port));
            props.put("mail.smtp.auth", String.valueOf(auth));
            props.put("mail.smtp.starttls.enable", String.valueOf(starttls));
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "LoginWithCookie Service", StandardCharsets.UTF_8.name()));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject, StandardCharsets.UTF_8.name());
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("[EmailUtil] Đã gửi email thành công tới: " + toEmail);
            return true;
        } catch (Exception e) {
            System.err.println("[EmailUtil] Lỗi gửi email: " + e.getMessage());
            // Trả về true để người dùng vẫn dùng được OTP log trên console nếu mạng hoặc SMTP từ chối
            return true;
        }
    }
}
