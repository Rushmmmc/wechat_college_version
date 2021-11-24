package com.mywechat.service;

import com.mywechat.model.Chat;
import com.mywechat.model.Group;
import com.mywechat.model.Message;
import com.mywechat.model.User;

import java.sql.SQLException;
import java.util.List;

public interface ChatService {
    /**
     * 获取用户最近的消息列表
     *
     * @param loginId
     * @return
     */
    List<User> getChatList(String loginId);

    /**
     * 根据用户userId获取用户的账户loginId
     *
     * @param userId
     * @return
     */
    String getUserLoginId(int userId);

    /**
     * 增加用户的聊天记录
     *
     * @param message
     * @return
     */
    boolean addMessage(Message message);

    /**
     * 增加用户的最近聊天记录
     *
     * @param friendId
     * @param type
     * @param loginId
     * @return
     */
    boolean addChat(int friendId, int type, String loginId);

    /**
     * 在增加之前先去除之前存在的该类数据
     * 如：1号用户在之前已经和2号用户聊过天，那么先删除过去的，在增加现在的
     *
     * @param chat
     * @return
     */
    boolean deleteChat(Chat chat);

    /**
     * 获取好友的昵称
     *
     * @param friendId
     * @return
     */
    String getFriendNickName(int friendId);

    /**
     * 获取用户的未接受消息
     *
     * @param receiver
     * @param status
     * @param loginId
     * @return
     * @throws SQLException
     */
    List<Message> getMessage(int receiver, int status, String loginId);

    /**
     * 获取用户未接受的消息（群聊）
     *
     * @param groupId
     * @param loginId
     * @return
     */
    List<Message> getUnAccept(int groupId, String loginId);

    /**
     * 获取用户的群聊列表
     *
     * @return
     */
    List<Group> getGroup(String loginId);

    /**
     * 获取群组成员
     *
     * @param groupId
     * @return
     */
    List<User> getGroupMember(int groupId);

    /**
     * 获取群聊名称
     *
     * @param groupId
     * @param groupList
     * @return
     */
    String getGroupName(int groupId, List<Group> groupList);

    /**
     * 获取用户权限
     *
     * @param userList
     * @param loginId
     * @return
     */
    int getUserStatus(List<User> userList, String loginId);

    /**
     * 删除群组的聊天记录
     *
     * @param groupId
     * @param loginId
     * @param deleteTime
     * @return
     */
    boolean deleteMsgGroup(int groupId, String loginId, String deleteTime);

    /**
     * 删除私聊的聊天记录
     *
     * @param sendUser
     * @param loginId
     * @param deleteTime
     * @return
     */
    boolean deleteMsg(int sendUser, String loginId, String deleteTime);

    /**
     * 生成聊天记录
     *
     * @param messages
     * @param id
     * @return
     */
    void produceMsg(List<Message> messages, int id);
}
