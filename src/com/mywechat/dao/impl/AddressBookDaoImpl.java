package com.mywechat.dao.impl;

import com.mywechat.dao.AddressBookDao;
import com.mywechat.model.Constant;
import com.mywechat.model.User;
import com.mywechat.util.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDaoImpl implements AddressBookDao {
    @Override
    public List<User> getFriends(int userId, int pages) throws SQLException {
        String sql = "SELECT *FROM db_user WHERE userId IN (SELECT friend_id FROM db_friends WHERE user_id = ? AND STATUS = 1) ORDER BY nick_name LIMIT ?, ?";
        Object[] params = {userId, (pages - 1) * Constant.FRIENDS, Constant.FRIENDS};
        List<User> userList = new ArrayList<>();
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            User user = new User();
            user.setUserId(resultSet.getInt("userId"));
            user.setLoginId(resultSet.getString("login_id"));
            user.setNickName(resultSet.getString("nick_name"));
            user.setHeadPortrait(resultSet.getString("head_portrait"));
            user.setMailBox(resultSet.getString("mailbox"));
            user.setPhone(resultSet.getString("phone"));
            user.setSignature(resultSet.getString("signature"));
            userList.add(user);
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return userList;
    }

    @Override
    public int getFriendNum(int userId) {
        String sql = "SELECT count(*) FROM db_friends WHERE user_id = ? AND STATUS = 1";
        int number = 1;
        Object[] params = {number};
        try {
            number = DbUtil.getNumber(params, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return number;
    }

    @Override
    public List<User> searchUser(String loginId, String nickName, int userId, int pages) throws SQLException {
        String sql = "SELECT * FROM  db_user WHERE (login_id LIKE ? OR nick_name LIKE ?) AND userId NOT IN (SELECT friend_id FROM db_friends WHERE user_id = ? AND STATUS = 1) limit ?, ?";
        Object[] params = {"%" + loginId + "%", "%" + nickName + "%", userId, (pages - 1) * Constant.ADDRESSSIZE, Constant.ADDRESSSIZE};
        List<User> userList = getUserList(sql, params);
        return userList;
    }

    @Override
    public int getSearchUser(String loginId, String nickName, int userId) {
        int pages = 1;
        String sql = "SELECT count(*) FROM  db_user WHERE (login_id LIKE ? OR nick_name LIKE ?) AND userId NOT IN (SELECT friend_id FROM db_friends WHERE user_id = ? AND STATUS = 1)";
        Object[] params = {"%" + loginId + "%", "%" + nickName + "%", userId};
        try {
            pages = DbUtil.getNumber(params, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pages;
    }

    @Override
    public boolean doAdd(int userId, int friendId) {
        String sql = "INSERT INTO db_friends (db_friend_id, friend_id, user_id, friend_group, nick_name, STATUS)VALUES(NULL,?,?,'好友','未设定', 0)";
        Object[] params = {friendId, userId};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean judgeAdd(int userId, int friendId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM db_friends WHERE user_id = ? AND friend_id = ?";
        Object[] params = {userId, friendId};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        int number = 0;
        while (resultSet.next()) {
            number = resultSet.getInt("count(*)");
        }
        if (number == 0) {
            /**
             * 如果未发送请求，则返回true
             */
            return true;
        } else {
            /**
             * 如果未发送过请求，则返回false
             */
            return false;
        }
    }

    @Override
    public boolean confirmAdd(int userId, int friendId, int type) {
        String sql = "UPDATE db_friends SET STATUS = ? WHERE friend_id = ? AND user_id = ?";
        Object[] params = {type, userId, friendId};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean addFriend(int userId, int friendId, int type) {
        String sql = "INSERT INTO db_friends (db_friend_id, friend_id, user_id, friend_group, nick_name, STATUS)VALUES(NULL,?,?,'好友','未设定', ?)";
        Object[] params = {friendId, userId, type};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public List<User> getRequestFriend(int userId, int pages) {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT *FROM db_user WHERE userId in (SELECT user_id FROM db_friends WHERE friend_id = ? AND STATUS = 0) limit ?, ?";
        Object[] params = {userId, (pages - 1) * Constant.ADDRESSSIZE, Constant.ADDRESSSIZE};
        try {
            userList = getUserList(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public int getRequestNum(int userId) {
        int pages = 1;
        String sql = "SELECT count(*) FROM db_user WHERE userId in (SELECT user_id FROM db_friends WHERE friend_id = ? AND STATUS = 0)";
        Object[] params = {userId};
        try {
            pages = DbUtil.getNumber(params, sql);
        } catch (SQLException e) {
            System.out.println("数据库使用异常");
        }
        return pages;
    }

    @Override
    public boolean deleteFriend(int userId, int friendId) {
        String sql = "DELETE FROM db_friends WHERE friend_id = ? AND user_id = ?";
        Object[] params = {friendId, userId};
        return DbUtil.executeUpdate(sql, params);
    }

    private List<User> getUserList(String sql, Object[] params) throws SQLException {
        List<User> unAddFriends = new ArrayList<>();
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            User user = new User();
            user.setUserId(resultSet.getInt("userId"));
            user.setNickName(resultSet.getString("nick_name"));
            user.setLoginId(resultSet.getString("login_id"));
            unAddFriends.add(user);
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return unAddFriends;
    }
}
