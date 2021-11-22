package com.mywechat.service.impl;

import com.mywechat.dao.PhotoDao;
import com.mywechat.dao.impl.PhotoDaoImpl;
import com.mywechat.model.Constant;
import com.mywechat.model.Photo;
import com.mywechat.service.PersonService;
import com.mywechat.util.CountPage;

import java.sql.SQLException;
import java.util.List;

public class PersonImpl implements PersonService {
    @Override
    public List<Photo> getPhotos(int pages, int sizes) {
        PhotoDao photoDao = new PhotoDaoImpl();
        try {
            return photoDao.getPhoto(pages, sizes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getPhotoSize() {
        PhotoDao photoDao = new PhotoDaoImpl();
        int number = 0;
        try {
            number = photoDao.getPhotoNumber();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return CountPage.getSize(number, Constant.SIZES);
    }

    @Override
    public boolean changeBg(String loginId, String photoName) {
        String photoUrl = Constant.DEFAULTBG + photoName;
        PhotoDao photoDao = new PhotoDaoImpl();
        return photoDao.changeBg(loginId, photoUrl);
    }
}
