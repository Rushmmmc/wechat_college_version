package com.mywechat.service;

import com.mywechat.model.Photo;

import java.util.List;

public interface PersonService {
    /**
     * 获取当前页数的图片
     *
     * @param pages
     * @param sizes
     * @return
     */
    List<Photo> getPhotos(int pages, int sizes);

    /**
     * 获取图库的背景图数量
     *
     * @return
     */
    int getPhotoSize();

    /**
     * 更换背景
     *
     * @param loginId
     * @param photoName
     * @return
     */
    boolean changeBg(String loginId, String photoName);
}
