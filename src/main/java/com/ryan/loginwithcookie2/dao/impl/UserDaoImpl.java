package com.ryan.loginwithcookie2.dao.impl;

import com.ryan.loginwithcookie2.config.JpaConfig;
import com.ryan.loginwithcookie2.dao.IUserDao;
import com.ryan.loginwithcookie2.dao.UserDao;
import com.ryan.loginwithcookie2.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UserDaoImpl implements IUserDao, UserDao {

    @Override
    public void insert(User user) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.persist(user); // insert vào bảng
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void update(User user) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.merge(user); // update vào bảng
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            User user = enma.find(User.class, id);
            if (user != null) {
                enma.remove(user);
            } else {
                throw new Exception("Không tìm thấy");
            }
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public User findById(int id) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            User user = enma.find(User.class, id);
            return user;
        } finally {
            enma.close();
        }
    }

    @Override
    public User findByUsername(String username) throws Exception {
        EntityManager enma = JpaConfig.getEntityManager();
        String jpql = "SELECT u FROM User u WHERE u.userName = :username";
        try {
            TypedQuery<User> query = enma.createQuery(jpql, User.class);
            query.setParameter("username", username);
            List<User> list = query.getResultList();
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list.get(0);
        } finally {
            enma.close();
        }
    }

    @Override
    public User findByEmail(String email) {
        EntityManager enma = JpaConfig.getEntityManager();
        String jpql = "SELECT u FROM User u WHERE u.email = :email";
        try {
            TypedQuery<User> query = enma.createQuery(jpql, User.class);
            query.setParameter("email", email);
            List<User> list = query.getResultList();
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            enma.close();
        }
    }

    @Override
    public List<User> findAll() {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            TypedQuery<User> query = enma.createNamedQuery("User.findAll", User.class);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<User> findAll(int page, int pagesize) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            TypedQuery<User> query = enma.createNamedQuery("User.findAll", User.class);
            query.setFirstResult(page * pagesize);
            query.setMaxResults(pagesize);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<User> searchByName(String keyword) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT u FROM User u WHERE u.fullName LIKE :keyword OR u.userName LIKE :keyword";
            TypedQuery<User> query = enma.createQuery(jpql, User.class);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public int count() {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT count(u) FROM User u";
            Query query = enma.createQuery(jpql);
            return ((Long) query.getSingleResult()).intValue();
        } finally {
            enma.close();
        }
    }

    @Override
    public User get(String username) {
        try {
            return findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean checkExistEmail(String email) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT count(u) FROM User u WHERE u.email = :email";
            Query query = enma.createQuery(jpql);
            query.setParameter("email", email);
            return ((Long) query.getSingleResult()) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            enma.close();
        }
    }

    @Override
    public boolean checkExistUsername(String username) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT count(u) FROM User u WHERE u.userName = :username";
            Query query = enma.createQuery(jpql);
            query.setParameter("username", username);
            return ((Long) query.getSingleResult()) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            enma.close();
        }
    }

    @Override
    public boolean checkExistPhone(String phone) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT count(u) FROM User u WHERE u.phone = :phone";
            Query query = enma.createQuery(jpql);
            query.setParameter("phone", phone);
            return ((Long) query.getSingleResult()) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            enma.close();
        }
    }
}
