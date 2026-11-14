package com.ryan.loginwithcookie2.service.impl;

import com.ryan.loginwithcookie2.dao.IUserDao;
import com.ryan.loginwithcookie2.dao.impl.UserDaoImpl;
import com.ryan.loginwithcookie2.model.User;
import com.ryan.loginwithcookie2.service.IUserService;
import com.ryan.loginwithcookie2.service.UserService;

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
    public User login(String username, String password) {
        User user = this.get(username);
        if (user != null && password != null && password.equals(user.getPassWord())) {
            return user;
        }
        return null;
    }

    @Override
    public User get(String username) {
        return userDao.get(username);
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (userDao.checkExistUsername(username)) {
            return false;
        }
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        // Default roleid = 3 (Người dùng thông thường / user)
        userDao.insert(new User(email, username, fullname, password, null, 3, phone, date));
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
