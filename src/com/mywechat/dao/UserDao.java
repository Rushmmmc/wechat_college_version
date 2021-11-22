package com.mywechat.dao;


import com.mywechat.model.User;

import java.sql.SQLException;

/**
 * 功能： 用户的登录，注册，检查等数据库操作
 */
public interface UserDao {
    /**
     * 用于确定该用户（手机号码，身份证，邮箱）是否存在于数据库中
     *
     * @param message
     * @param sql
     * @return
     * @throws SQLException
     */
    boolean queryUserDao(String message, String sql) throws SQLException;

    /**
     * 功能: 用于增加用户，数据库的insert操作
     *
     * @param user
     * @return
     */
    boolean addUserDao(User user);

    /**
     * 用于检测账户和密码的匹配
     *
     * @param loginId
     * @param password
     * @return
     * @throws SQLException
     */
    boolean confirmPswDao(String loginId, String password) throws SQLException;

    /**
     * 用于更新用户的账户密码
     *
     * @param loginId
     * @param newPassword
     * @return
     */
    boolean modifyPassword(String loginId, String newPassword);

    /**
     * 更新用户的基本信息（用户名，个人简介）
     *
     * @param nickName
     * @param loginId
     * @param signature
     * @return
     */
    boolean modifyInformation(String nickName, String loginId, String signature);

    /**
     * 获取用户的所有信息
     *
     * @param loginId
     * @return
     * @throws SQLException
     */
    User getMessage(String loginId) throws SQLException;

    /**
     * 更换用户头像
     *
     * @param loginId
     * @param portrait
     * @return
     */
    boolean changePortrait(String loginId, String portrait);

    /**
     * 通过账户获取用户ID
     *
     * @param loginId
     * @return
     * @throws SQLException
     * @throws SQLException
     */
    int getUserId(String loginId) throws SQLException;

    /**
     * 获取用户名称
     *
     * @param userId
     * @param type
     * @return
     * @throws SQLException
     */
    String getUserMsg(int userId, int type) throws SQLException;

    /**
     * 增加自己为好友
     *
     * @param userId
     * @return
     */
    boolean addMyself(int userId);
}
