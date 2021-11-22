package com.mywechat.service;


import com.mywechat.model.User;

import java.sql.SQLException;

public interface IndexService {
    /**
     * message存放查找的数据，type存放查找的数据类型（账户，身份证，手机号码等）
     *
     * @param message
     * @param type
     * @return
     * @throws SQLException
     */
    boolean queryMessage(String message, String type);

    /**
     * 检查用户账户和密码
     *
     * @param loginId
     * @param password
     * @return
     * @throws SQLException
     */
    boolean confirmPassword(String loginId, String password);

    /**
     * 用于用户的注册，增加用户
     *
     * @param user
     * @return
     */
    int addUser(User user);

    /**
     * 用于更新用户的账户密码
     *
     * @param loginId
     * @param newPassword
     * @return
     * @throws SQLException
     */
    boolean modifyPsw(String loginId, String newPassword);

    /**
     * 更新用户的基本信息（简介和昵称）
     *
     * @param nickName
     * @param loginId
     * @param signature
     * @return
     */
    boolean modifyInfo(String nickName, String loginId, String signature);

    /**
     * 获取用户的全部信息
     *
     * @param loginId
     * @return
     * @throws SQLException
     */
    User getUserMsg(String loginId);

    /**
     * 更换用户头像
     *
     * @param loginId
     * @param portrait
     * @return
     */
    boolean changePortrait(String loginId, String portrait);

    /**
     * 默认添加自己为好友
     *
     * @param loginId
     * @return
     * @throws SQLException
     */
    boolean addMyself(String loginId);
}
