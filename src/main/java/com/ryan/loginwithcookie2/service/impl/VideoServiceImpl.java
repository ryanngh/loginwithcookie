package com.ryan.loginwithcookie2.service.impl;

import com.ryan.loginwithcookie2.dao.IVideoDao;
import com.ryan.loginwithcookie2.dao.impl.VideoDaoImpl;
import com.ryan.loginwithcookie2.model.Video;
import com.ryan.loginwithcookie2.service.IVideoService;
import com.ryan.loginwithcookie2.service.VideoService;

import java.util.List;

public class VideoServiceImpl implements IVideoService, VideoService {
    private final IVideoDao videoDao = new VideoDaoImpl();

    @Override
    public void insert(Video video) {
        videoDao.insert(video);
    }

    @Override
    public void update(Video video) {
        videoDao.update(video);
    }

    @Override
    public void delete(int videoId) throws Exception {
        videoDao.delete(videoId);
    }

    @Override
    public Video findById(int videoId) {
        return videoDao.findById(videoId);
    }

    @Override
    public List<Video> findAll() {
        return videoDao.findAll();
    }

    @Override
    public List<Video> findTop10Newest() {
        return videoDao.findTop10Newest();
    }

    @Override
    public List<Video> findByUserId(int userId, int page, int pageSize) {
        return videoDao.findByUserId(userId, page, pageSize);
    }

    @Override
    public int countByUserId(int userId) {
        return videoDao.countByUserId(userId);
    }

    @Override
    public List<Video> findAll(int page, int pageSize) {
        return videoDao.findAll(page, pageSize);
    }

    @Override
    public int countAll() {
        return videoDao.countAll();
    }

    @Override
    public void incrementViews(int videoId) {
        videoDao.incrementViews(videoId);
    }
}
