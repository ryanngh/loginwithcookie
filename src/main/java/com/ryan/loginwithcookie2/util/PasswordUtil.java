package com.ryan.loginwithcookie2.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Mã hóa mật khẩu plain text sang chuỗi hash BCrypt
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) {
            return null;
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * Kiểm tra mật khẩu plain text với mật khẩu lưu trong CSDL.
     * Hỗ trợ backward compatibility: nếu mật khẩu cũ lưu dạng plain text thì vẫn khớp,
     * sau đó tự nâng cấp lên BCrypt.
     */
    public static boolean checkPassword(String plainPassword, String storedPassword) {
        if (plainPassword == null || storedPassword == null) {
            return false;
        }
        // Kiểm tra xem storedPassword có phải là BCrypt hash không (bắt đầu bằng $2a$, $2b$, hoặc $2y$)
        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
            try {
                return BCrypt.checkpw(plainPassword, storedPassword);
            } catch (Exception e) {
                return false;
            }
        }
        // Trường hợp mật khẩu cũ lưu dạng plain text
        return plainPassword.equals(storedPassword);
    }

    /**
     * Kiểm tra xem chuỗi có phải đã được băm bằng BCrypt hay chưa
     */
    public static boolean isHashed(String password) {
        if (password == null) return false;
        return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
    }
}
