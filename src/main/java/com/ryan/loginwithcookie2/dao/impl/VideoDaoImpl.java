package com.ryan.loginwithcookie2.dao.impl;

import com.ryan.loginwithcookie2.config.JpaConfig;
import com.ryan.loginwithcookie2.dao.IVideoDao;
import com.ryan.loginwithcookie2.dao.VideoDao;
import com.ryan.loginwithcookie2.model.Video;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class VideoDaoImpl implements IVideoDao, VideoDao {

    @Override
    public void insert(Video video) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(video);
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Video video) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(video);
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int videoId) throws Exception {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Video video = em.find(Video.class, videoId);
            if (video != null) {
                em.remove(video);
            } else {
                throw new Exception("Không tìm thấy video với ID = " + videoId);
            }
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Video findById(int videoId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.find(Video.class, videoId);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findAll() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Video> query = em.createNamedQuery("Video.findAll", Video.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findTop10Newest() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v WHERE v.active = true ORDER BY v.videoId DESC";
            TypedQuery<Video> query = em.createQuery(jpql, Video.class);
            query.setFirstResult(0);
            query.setMaxResults(10);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findByUserId(int userId, int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v WHERE v.user.id = :userId ORDER BY v.videoId DESC";
            TypedQuery<Video> query = em.createQuery(jpql, Video.class);
            query.setParameter("userId", userId);
            query.setFirstResult(page * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public int countByUserId(int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT count(v) FROM Video v WHERE v.user.id = :userId";
            Query query = em.createQuery(jpql);
            query.setParameter("userId", userId);
            return ((Long) query.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findAll(int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v WHERE v.active = true ORDER BY v.videoId DESC";
            TypedQuery<Video> query = em.createQuery(jpql, Video.class);
            query.setFirstResult(page * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public int countAll() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT count(v) FROM Video v WHERE v.active = true";
            Query query = em.createQuery(jpql);
            return ((Long) query.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public void incrementViews(int videoId) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Video video = em.find(Video.class, videoId);
            if (video != null) {
                video.setViews(video.getViews() + 1);
                em.merge(video);
            }
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
