package com.wechat.dao;


import com.wechat.po.Photo;

import java.sql.SQLException;
import java.util.List;

public interface PhotoDao {
    /**
     * 背景图片的分页展示，返回第pages页展示的sizes条信息
     *
     * @param pages
     * @param sizes
     * @return
     * @throws SQLException
     */
    List<Photo> getPhoto(int pages, int sizes) throws SQLException;

    /**
     * 获取图片数量
     *
     * @return
     * @throws SQLException
     */
    int getPhotoNumber() throws SQLException;

    /**
     * 更新用户背景图
     *
     * @param loginId
     * @param photoUrl
     * @return
     */
    boolean changeBg(String loginId, String photoUrl);
}
