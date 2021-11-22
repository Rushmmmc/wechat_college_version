package com.mywechat.service.impl;

import com.mywechat.dao.AddressBookDao;
import com.mywechat.dao.UserDao;
import com.mywechat.dao.impl.AddressBookDaoImpl;
import com.mywechat.dao.impl.UserDaoImpl;
import com.mywechat.model.Constant;
import com.mywechat.model.User;
import com.mywechat.service.AddressBookService;
import com.mywechat.util.CountPage;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户的通讯录类
 */
public class AddressBookImpl implements AddressBookService {
    public static AddressBookDao addressBookDao = new AddressBookDaoImpl();
    public static UserDao userDao = new UserDaoImpl();

    @Override
    public List<User> getFriends(String loginId, int pages) {
        List<User> userList = new ArrayList<>();
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            userList = addressBookDao.getFriends(userId, pages);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public int getFriendsNum(String loginId) {
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookDao.getFriendNum(userId);
    }

    @Override
    public User getFriendMsg(List<User> userList, int userId) {
        int i;
        /**
         * 获取好友列表的好友的信息
         * 在循环里找到好友后
         * 复制它的信息
         */
        for (i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserId() == userId) {
                return userList.get(i);
            }
        }
        return null;
    }

    @Override
    public List<User> getUnAddFriend(String likeLoginId, String nickName, String loginId, int pages) {
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return addressBookDao.searchUser(likeLoginId, nickName, userId, pages);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getUnAddNum(String loginId, String nickName, String userId) {
        int userId1 = -1;
        try {
            userId1 = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int pages = addressBookDao.getSearchUser(loginId, nickName, userId1);
        return CountPage.getSize(pages, Constant.ADDRESSSIZE);
    }

    @Override
    public boolean doAddFriend(int friendId, String loginId) {
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (addressBookDao.judgeAdd(userId, friendId)) {
                return addressBookDao.doAdd(userId, friendId);
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean confirmAdd(int friendId, int type, String loginId) {
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (addressBookDao.confirmAdd(userId, friendId, type)) {
            return addressBookDao.addFriend(userId, friendId, type);
        } else {
            return false;
        }
    }

    @Override
    public List<User> getRequest(String loginId, int pages) {
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookDao.getRequestFriend(userId, pages);
    }

    @Override
    public int getRequestNum(String userId) {
        int userId1 = -1;
        try {
            userId1 = userDao.getUserId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int pages = addressBookDao.getRequestNum(userId1);
        return CountPage.getSize(pages, Constant.ADDRESSSIZE);
    }

    @Override
    public boolean deleteFriend(String loginId, int friendId) {
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (userId == friendId) {
            return false;
        }
        addressBookDao.deleteFriend(friendId, userId);
        return addressBookDao.deleteFriend(userId, friendId);
    }
}
