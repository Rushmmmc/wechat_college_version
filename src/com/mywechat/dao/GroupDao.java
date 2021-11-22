package com.mywechat.dao;


import com.mywechat.model.Message;
import com.mywechat.model.User;

import java.sql.SQLException;
import java.util.List;

public interface GroupDao {
    /**
     * 删除群成员
     *
     * @param userId
     * @param groupId
     */
    void deleteMember(int userId, int groupId);

    /**
     * 增加群成员（好友邀请时）
     *
     * @param userId
     * @param groupId
     * @param status
     * @return
     */
    boolean addMember(int userId, int groupId, int status);

    /**
     * 增加群成员（创建群聊时）
     *
     * @param userId
     * @param status
     * @return
     */
    boolean addMember(int userId, int status);

    /**
     * 获取好友列表
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    List<User> getFriends(int userId) throws SQLException;

    /**
     * 判断群组内该成员是否已存在
     *
     * @param userId
     * @param groupId
     * @return
     * @throws SQLException
     */
    boolean judgeIsExist(int userId, int groupId) throws SQLException;

    /**
     * 增加群组
     *
     * @param userId
     * @param groupName
     * @return
     */
    boolean addGroup(int userId, String groupName);

    /**
     * 更新权限
     *
     * @param userId
     * @param groupId
     * @return
     */
    boolean updateRight(int userId, int groupId);

    /**
     * 获取私聊的消息记录
     *
     * @param sendId
     * @param receiverId
     * @return
     * @throws SQLException
     */
    List<Message> getMessages(int sendId, int receiverId) throws SQLException;

    /**
     * 获取群聊的聊天记录
     *
     * @param groupId
     * @param receiverId
     * @return
     * @throws SQLException
     */
    List<Message> getGroupMsg(int groupId, int receiverId) throws SQLException;
}
