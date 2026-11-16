package com.ryan.loginwithcookie2.dao;

import com.ryan.loginwithcookie2.model.User;

import java.util.List;

public interface IUserDao {
    void insert(User user);
    void update(User user);
    void delete(int id) throws Exception;
    User findById(int id);
    User findByUsername(String username) throws Exception;
    User findByEmail(String email);
    List<User> findAll();
    List<User> findAll(int page, int pagesize);
    List<User> searchByName(String keyword);
    int count();

    // Các phương thức đăng nhập & đăng ký
    User get(String username);
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);
}
