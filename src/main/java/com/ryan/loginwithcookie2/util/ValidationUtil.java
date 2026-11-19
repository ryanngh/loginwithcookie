package com.ryan.loginwithcookie2.util;

import jakarta.servlet.http.Part;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_.-]{3,50}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^(0|\\+84)[0-9]{9,10}$");

    private static final Pattern OTP_PATTERN =
            Pattern.compile("^[0-9]{6}$");

    private static final List<String> ALLOWED_IMAGE_EXT =
            Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp");

    private static final List<String> ALLOWED_VIDEO_EXT =
            Arrays.asList(".mp4", ".webm", ".ogg", ".mov");

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return !isBlank(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidUsername(String username) {
        return !isBlank(username) && USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (isBlank(phone)) {
            return true; // Số điện thoại không bắt buộc
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidPassword(String password, int minLength) {
        return password != null && password.trim().length() >= minLength;
    }

    public static boolean isValidOtp(String otp) {
        return !isBlank(otp) && OTP_PATTERN.matcher(otp.trim()).matches();
    }

    public static boolean isValidUrl(String url) {
        if (isBlank(url)) {
            return true;
        }
        String trimmed = url.trim().toLowerCase();
        return trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("/");
    }

    public static boolean isValidLength(String str, int maxLength) {
        return str == null || str.trim().length() <= maxLength;
    }

    public static boolean isValidImagePart(Part part) {
        if (part == null || part.getSize() == 0) {
            return true;
        }
        String contentType = part.getContentType();
        String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString().toLowerCase();
        boolean validExt = ALLOWED_IMAGE_EXT.stream().anyMatch(fileName::endsWith);
        boolean validMime = contentType != null && contentType.toLowerCase().startsWith("image/");
        return validExt || validMime;
    }

    public static boolean isValidVideoPart(Part part) {
        if (part == null || part.getSize() == 0) {
            return true;
        }
        String contentType = part.getContentType();
        String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString().toLowerCase();
        boolean validExt = ALLOWED_VIDEO_EXT.stream().anyMatch(fileName::endsWith);
        boolean validMime = contentType != null && contentType.toLowerCase().startsWith("video/");
        return validExt || validMime;
    }
}
