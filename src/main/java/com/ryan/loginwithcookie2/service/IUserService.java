package com.ryan.loginwithcookie2.service;

import com.ryan.loginwithcookie2.model.User;

import java.util.List;

public interface IUserService {
    void insert(User user);
    void update(User user);
    void delete(int id);
    User findById(int id);
    User findByUsername(String username);
    User getByEmail(String email);
    List<User> findAll();
    List<User> findAll(int page, int pagesize);
    List<User> searchByName(String keyword);
    int count();

    User login(String username, String password);
    User get(String username);
    boolean register(String username, String password, String email, String fullname, String phone);
    boolean registerWithOtp(String username, String password, String email, String fullname, String phone, String otp);
    boolean verifyOtpAndActivate(String emailOrUsername, String otp);
    boolean sendForgotPasswordOtp(String email);
    boolean resetPassword(String email, String otp, String newPassword);
    boolean resendOtp(String emailOrUsername);
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);
}
