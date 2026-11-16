package com.ryan.loginwithcookie2.service.impl;

import com.ryan.loginwithcookie2.dao.IUserDao;
import com.ryan.loginwithcookie2.dao.impl.UserDaoImpl;
import com.ryan.loginwithcookie2.model.User;
import com.ryan.loginwithcookie2.service.IUserService;
import com.ryan.loginwithcookie2.service.UserService;
import com.ryan.loginwithcookie2.util.EmailUtil;
import com.ryan.loginwithcookie2.util.PasswordUtil;

import java.sql.Date;
import java.util.List;

public class UserServiceImpl implements IUserService, UserService {
    public IUserDao userDao = new UserDaoImpl();

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> searchByName(String keyword) {
        return userDao.searchByName(keyword);
    }

    @Override
    public void insert(User user) {
        User u = this.findByUsername(user.getUserName());
        if (u == null) {
            // Đảm bảo mật khẩu được băm bằng BCrypt
            if (!PasswordUtil.isHashed(user.getPassWord())) {
                user.setPassWord(PasswordUtil.hashPassword(user.getPassWord()));
            }
            userDao.insert(user);
        }
    }

    @Override
    public void update(User user) {
        User u = this.findById(user.getId());
        if (u != null) {
            userDao.update(user);
        }
    }

    @Override
    public void delete(int id) {
        try {
            userDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int count() {
        return userDao.count();
    }

    @Override
    public List<User> findAll(int page, int pagesize) {
        return userDao.findAll(page, pagesize);
    }

    @Override
    public User findByUsername(String username) {
        try {
            return userDao.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User login(String username, String password) {
        User user = this.get(username);
        if (user == null) {
            user = this.getByEmail(username);
        }

        if (user != null && password != null) {
            if (PasswordUtil.checkPassword(password, user.getPassWord())) {
                // Tự động nâng cấp mật khẩu cũ sang hash BCrypt nếu chưa được băm
                if (!PasswordUtil.isHashed(user.getPassWord())) {
                    user.setPassWord(PasswordUtil.hashPassword(password));
                    userDao.update(user);
                }
                return user;
            }
        }
        return null;
    }

    @Override
    public User get(String username) {
        return userDao.get(username);
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        String otp = EmailUtil.generateOtp();
        return registerWithOtp(username, password, email, fullname, phone, otp);
    }

    @Override
    public boolean registerWithOtp(String username, String password, String email, String fullname, String phone, String otp) {
        // Kiểm tra tài khoản đã tồn tại và đã kích hoạt chưa
        User existingUser = userDao.get(username);
        if (existingUser != null && (existingUser.getStatus() == null || existingUser.getStatus() == 1)) {
            return false;
        }

        User existingEmail = userDao.findByEmail(email);
        if (existingEmail != null && (existingEmail.getStatus() == null || existingEmail.getStatus() == 1)) {
            return false;
        }

        // Mã hóa mật khẩu bằng BCrypt
        String hashedPassword = PasswordUtil.hashPassword(password);
        Date currentDate = new Date(System.currentTimeMillis());
        java.util.Date expiry = new java.util.Date(System.currentTimeMillis() + 10 * 60 * 1000); // 10 phút

        if (existingUser != null && existingUser.getStatus() != null && existingUser.getStatus() == 0) {
            // Cập nhật lại thông tin cho tài khoản chưa kích hoạt
            existingUser.setPassWord(hashedPassword);
            existingUser.setEmail(email);
            existingUser.setFullName(fullname);
            existingUser.setPhone(phone);
            existingUser.setOtp(otp);
            existingUser.setOtpExpiry(expiry);
            userDao.update(existingUser);
        } else {
            // Tạo mới tài khoản với status = 0 (chưa kích hoạt)
            User newUser = new User(email, username, fullname, hashedPassword, null, 3, phone, currentDate);
            newUser.setStatus(0);
            newUser.setOtp(otp);
            newUser.setOtpExpiry(expiry);
            userDao.insert(newUser);
        }

        // Gửi mã OTP qua email
        EmailUtil.sendActivationOtp(email, username, otp);
        return true;
    }

    @Override
    public boolean verifyOtpAndActivate(String emailOrUsername, String otp) {
        if (emailOrUsername == null || otp == null) {
            return false;
        }
        User user = userDao.findByEmail(emailOrUsername.trim());
        if (user == null) {
            user = userDao.get(emailOrUsername.trim());
        }

        if (user == null) {
            return false;
        }

        // Kiểm tra OTP và thời hạn
        if (user.getOtp() != null && user.getOtp().trim().equals(otp.trim())) {
            if (user.getOtpExpiry() == null || user.getOtpExpiry().after(new java.util.Date())) {
                user.setStatus(1); // Kích hoạt thành công
                user.setOtp(null);
                user.setOtpExpiry(null);
                userDao.update(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean sendForgotPasswordOtp(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        User user = userDao.findByEmail(email.trim());
        if (user == null) {
            return false;
        }

        String otp = EmailUtil.generateOtp();
        java.util.Date expiry = new java.util.Date(System.currentTimeMillis() + 10 * 60 * 1000); // 10 phút

        user.setOtp(otp);
        user.setOtpExpiry(expiry);
        userDao.update(user);

        EmailUtil.sendForgotPasswordOtp(user.getEmail(), user.getUserName(), otp);
        return true;
    }

    @Override
    public boolean resetPassword(String email, String otp, String newPassword) {
        if (email == null || otp == null || newPassword == null) {
            return false;
        }
        User user = userDao.findByEmail(email.trim());
        if (user == null) {
            return false;
        }

        if (user.getOtp() != null && user.getOtp().trim().equals(otp.trim())) {
            if (user.getOtpExpiry() == null || user.getOtpExpiry().after(new java.util.Date())) {
                user.setPassWord(PasswordUtil.hashPassword(newPassword));
                user.setOtp(null);
                user.setOtpExpiry(null);
                user.setStatus(1); // Đảm bảo tài khoản được kích hoạt
                userDao.update(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean resendOtp(String emailOrUsername) {
        if (emailOrUsername == null) return false;
        User user = userDao.findByEmail(emailOrUsername.trim());
        if (user == null) {
            user = userDao.get(emailOrUsername.trim());
        }
        if (user == null) return false;

        String otp = EmailUtil.generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(new java.util.Date(System.currentTimeMillis() + 10 * 60 * 1000));
        userDao.update(user);

        if (user.getStatus() != null && user.getStatus() == 0) {
            EmailUtil.sendActivationOtp(user.getEmail(), user.getUserName(), otp);
        } else {
            EmailUtil.sendForgotPasswordOtp(user.getEmail(), user.getUserName(), otp);
        }
        return true;
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.checkExistUsername(username);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return userDao.checkExistPhone(phone);
    }
}
