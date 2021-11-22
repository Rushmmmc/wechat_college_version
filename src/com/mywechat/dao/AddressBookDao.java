package com.mywechat.dao;

import com.mywechat.model.User;

import java.sql.SQLException;
import java.util.List;

public interface AddressBookDao {
    /**
     * 获取用户的好友列表
     *
     * @param userId
     * @param pages
     * @return
     * @throws SQLException
     */
    List<User> getFriends(int userId, int pages) throws SQLException;

    /**
     * 获取朋友列表的数量
     *
     * @param userId
     * @return
     */
    int getFriendNum(int userId);

    /**
     * 获取未添加的好友列表
     *
     * @param loginId
     * @param nickName
     * @param userId
     * @param pages
     * @return
     * @throws SQLException
     */
    List<User> searchUser(String loginId, String nickName, int userId, int pages) throws SQLException;

    /**
     * 获取未添加的好友的总数量
     *
     * @param loginId
     * @param nickName
     * @param userId
     * @return
     */
    int getSearchUser(String loginId, String nickName, int userId);

    /**
     * 发送添加好友请求
     *
     * @param userId
     * @param friendId
     * @return
     */
    boolean doAdd(int userId, int friendId);

    /**
     * 判断是否发送过了好友请求
     *
     * @param userId
     * @param friendId
     * @return
     * @throws SQLException
     */
    boolean judgeAdd(int userId, int friendId) throws SQLException;

    /**
     * 确认添加好友,或拒绝添加
     *
     * @param userId
     * @param friendId
     * @param type     type 1:  表示同意增加   2： 拒绝增加   0：未同意也未拒绝
     * @return
     */
    boolean confirmAdd(int userId, int friendId, int type);

    /**
     * 同意好友请求后，在数据库插入自己与好友的关系，好友状态
     *
     * @param userId
     * @param friendId
     * @param type     type 1:  表示同意增加   2： 拒绝增加   0：未同意也未拒绝
     * @return
     */
    boolean addFriend(int userId, int friendId, int type);

    /**
     * 获取申请添加该用户的列表
     *
     * @param userId
     * @param pages
     * @return
     */
    List<User> getRequestFriend(int userId, int pages);

    /**
     * 获取申请添加该用户的列表的数量
     *
     * @param userId
     * @return
     */
    int getRequestNum(int userId);

    /**
     * 删除好友
     *
     * @param userId
     * @param friendId
     * @return
     */
    boolean deleteFriend(int userId, int friendId);
}
