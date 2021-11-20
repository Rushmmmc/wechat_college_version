package com.wechat.dao.impl;


import com.wechat.dao.PhotoDao;
import com.wechat.po.Photo;
import com.wechat.util.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhotoDaoImpl implements PhotoDao {
    @Override
    public List<Photo> getPhoto(int pages, int sizes) throws SQLException {
        List<Photo> photos = new ArrayList<>();
        String sql = "select bg_name from db_background order by bg_id limit ?, ?";
        Object[] params = {(pages - 1) * sizes, sizes};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            Photo photo = new Photo();
            photo.setPhotoName(resultSet.getString("bg_name"));
            photos.add(photo);
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return photos;
    }

    @Override
    public int getPhotoNumber() throws SQLException {
        String sql = "SELECT COUNT(*)FROM db_background";
        ResultSet resultSet = DbUtil.executeQuery(sql, null);
        int result = -1;
        while (resultSet.next()) {
            result = resultSet.getInt("count(*)");
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return result;
    }

    @Override
    public boolean changeBg(String loginId, String photoUrl) {
        String sql = "update db_user set background = ? where login_id = ?";
        Object[] params = {photoUrl, loginId};
        return DbUtil.executeUpdate(sql, params);
    }
}
