package com.mywechat.dao;

import com.mywechat.model.Chat;
import com.mywechat.model.Group;
import com.mywechat.model.Message;
import com.mywechat.model.User;

import java.sql.SQLException;
import java.util.List;

public interface ChatDao {
    /**
     * 获取用户最近的聊天动态
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    List<Chat> getChatList(int userId) throws SQLException;

    /**
     * 获取用户绑定的账户
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    String getUserLoginId(int userId) throws SQLException;

    /**
     * 增加聊天记录（私聊）
     * 私聊
     *
     * @param message
     * @return
     */
    boolean addMessage(Message message);

    /**
     * 增加用户的最近聊天记录（群聊）
     *
     * @param chat
     * @return
     */
    boolean addChat(Chat chat);

    /**
     * 删除该用户与对应用户之前的聊天情况
     *
     * @param chat
     * @return
     */
    boolean deleteChat(Chat chat);

    /**
     * 获取消息列表
     *
     * @param sendUser
     * @param receiver
     * @param status
     * @return
     * @throws SQLException
     */
    List<Message> getMessage(int sendUser, int receiver, int status) throws SQLException;

    /**
     * 获取未被接受的消息
     *
     * @param userId
     * @param type
     * @return
     * @throws SQLException
     */
    List<Message> getUnAccept(int userId, int type) throws SQLException;

    /**
     * 更新用户的消息接收状态（私聊）
     *
     * @param sendUser
     * @param receiver
     * @param type
     * @return
     */
    boolean updateMessage(int sendUser, int receiver, int type);

    /**
     * 更新用户的消息接收状态（群聊）
     *
     * @param receiver
     * @param type
     * @return
     */
    boolean updateMessage(int receiver, int type);

    /**
     * 获取该用户的群聊列表
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    List<Group> getGroup(int userId) throws SQLException;

    /**
     * 获取群组成员列表
     *
     * @param groupId
     * @return
     * @throws SQLException
     */
    List<User> getGroupMember(int groupId) throws SQLException;

    /**
     * 删除群组聊天记录
     *
     * @param receiverId
     * @param groupId
     * @param deleteTime
     * @return
     */
    boolean deleteMsgGroup(int receiverId, int groupId, String deleteTime);

    /**
     * 删除私聊聊天记录
     *
     * @param sendUser
     * @param receiverId
     * @param deleteTime
     * @return
     */
    boolean deleteMsg(int sendUser, int receiverId, String deleteTime);
}
