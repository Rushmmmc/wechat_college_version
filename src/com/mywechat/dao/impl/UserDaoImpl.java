package com.mywechat.dao.impl;

import com.mywechat.dao.UserDao;
import com.mywechat.model.User;
import com.mywechat.util.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean queryUserDao(String message, String sql) throws SQLException {
        Object[] params = {message};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        if (!resultSet.next()) {
            DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
            return false;
        } else {
            DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
            return true;
        }
    }

    @Override
    public boolean addUserDao(User user) {
        String sql = "insert into db_user (userId, login_id, password, nick_name, head_portrait, mailbox, phone, id_card ,signature, background)values(null,?, ?, ?, ?, ?, ?, ? ,?, ?)";
        Object[] params = {user.getLoginId(), user.getPassword(), user.getNickName(), user.getHeadPortrait(), user.getMailBox(), user.getPhone(), user.getIdCard(), user.getSignature(), user.getBackground()};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean confirmPswDao(String loginId, String password) throws SQLException {
        String sql = "select password from db_user where login_id = ?";
        Object[] params = {loginId};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        if (resultSet.next()) {
            if (resultSet.getString("password").equals(password)) {
                DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
                return true;
            } else {
                DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
                return false;
            }
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return false;
    }

    @Override
    public boolean modifyPassword(String loginId, String newPassword) {
        String sql = "update db_user set password = ? where login_id = ?";
        Object[] params = {newPassword, loginId};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean modifyInformation(String nickName, String loginId, String signature) {
        String sql = "update db_user set nick_name = ?, signature = ? where login_id = ?";
        Object[] params = {nickName, signature, loginId};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public User getMessage(String loginId) throws SQLException {
        User user = new User();
        String sql = "select * from db_user where login_id = ?";
        Object[] params = {loginId};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            user.setLoginId(loginId);
            user.setUserId(resultSet.getInt("userId"));
            user.setMailBox(resultSet.getString("mailbox"));
            user.setPhone(resultSet.getString("phone"));
            user.setIdCard(resultSet.getString("id_card"));
            user.setSignature(resultSet.getString("signature"));
            user.setHeadPortrait(resultSet.getString("head_portrait"));
            user.setNickName(resultSet.getString("nick_name"));
            user.setBackground(resultSet.getString("background"));
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return user;
    }

    @Override
    public boolean changePortrait(String loginId, String portrait) {
        String sql = "update db_user set head_portrait = ? where login_id = ?";
        Object[] params = {portrait, loginId};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public int getUserId(String loginId) throws SQLException {
        String sql = "SELECT userId FROM db_user WHERE login_id = ?";
        Object[] params = {loginId};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        int userId = -1;
        while (resultSet.next()) {
            userId = resultSet.getInt("userId");
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return userId;
    }

    @Override
    public String getUserMsg(int userId, int type) throws SQLException {
        String sql;
        if (type == 1) {
            sql = "SELECT nick_name FROM db_user WHERE userId = ?";
        } else {
            sql = "SELECT login_id FROM db_user WHERE userId = ?";
        }
        Object[] params = {userId};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        String userMsg = null;
        while (resultSet.next()) {
            if (type == 1) {
                userMsg = resultSet.getString("nick_name");
            } else {
                userMsg = resultSet.getString("login_id");
            }
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return userMsg;
    }

    @Override
    public boolean addMyself(int userId) {
        String sql = "INSERT INTO db_friends (db_friend_id, friend_id, user_id, friend_group, nick_name, status)VALUES(NULL,?,?,?,?,1)";
        Object[] params = {userId, userId, "好友", "自己"};
        return DbUtil.executeUpdate(sql, params);
    }

}
