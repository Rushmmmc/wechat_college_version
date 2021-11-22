package com.mywechat.service.impl;


import com.mywechat.model.Constant;
import com.mywechat.dao.UserDao;
import com.mywechat.dao.impl.UserDaoImpl;
import com.mywechat.model.User;
import com.mywechat.service.IndexService;
import com.mywechat.util.Md5Util;

import java.sql.SQLException;

public class IndexImpl implements IndexService {
    private static UserDao userDao = new UserDaoImpl();

    @Override
    public boolean queryMessage(String message, String type) {
        String sql = null;
        /**
         * 通过对用户所需信息的判断而执行不同的数据库操作
         */
        if (type.equals(Constant.CHECKID)) {
            sql = "select login_id from db_user where login_id = ?";
        } else if (type.equals(Constant.CHECKMAIL)) {
            sql = "select mailbox from db_user where mailbox = ?";
        } else if (type.equals(Constant.CHECKPHONE)) {
            sql = "select phone from db_user where phone = ?";
        } else if (type.equals(Constant.CHECKIDCARD)) {
            sql = "select id_card from db_user where id_card = ?";
        }
        try {
            return userDao.queryUserDao(message, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean confirmPassword(String loginId, String password) {
        password = Md5Util.getMD5String(password);
        try {
            return userDao.confirmPswDao(loginId, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int addUser(User user) {
        if (userDao.addUserDao(user)) {
            addMyself(user.getLoginId());
        }
        return 0;
    }

    @Override
    public boolean modifyPsw(String loginId, String newPassword) {
        newPassword = Md5Util.getMD5String(newPassword);
        return userDao.modifyPassword(loginId, newPassword);
    }

    @Override
    public boolean modifyInfo(String nickName, String loginId, String signature) {
        return userDao.modifyInformation(nickName, loginId, signature);
    }

    @Override
    public User getUserMsg(String loginId) {
        try {
            Constant.user = userDao.getMessage(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return userDao.getMessage(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean changePortrait(String loginId, String portrait) {
        return userDao.changePortrait(loginId, portrait);
    }

    @Override
    public boolean addMyself(String loginId) {
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userDao.addMyself(userId);
    }
}
