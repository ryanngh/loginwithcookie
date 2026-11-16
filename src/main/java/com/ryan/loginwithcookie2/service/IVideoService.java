package com.ryan.loginwithcookie2.service;

import com.ryan.loginwithcookie2.model.Video;

import java.util.List;

public interface IVideoService {
    void insert(Video video);
    void update(Video video);
    void delete(int videoId) throws Exception;
    Video findById(int videoId);
    List<Video> findAll();
    List<Video> findTop10Newest();
    List<Video> findByUserId(int userId, int page, int pageSize);
    int countByUserId(int userId);
    List<Video> findAll(int page, int pageSize);
    int countAll();
    void incrementViews(int videoId);
}
