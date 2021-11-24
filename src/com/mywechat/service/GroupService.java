package com.mywechat.service;

import com.mywechat.model.Message;
import com.mywechat.model.User;

import java.util.List;

public interface GroupService {
    /**
     * 将好友移出群聊
     *
     * @param userId
     * @param groupId
     */
    void deleteMember(int userId, int groupId);

    /**
     * 判断是否有将好友移出群聊的权限
     *
     * @param userId
     * @param deleteId
     * @param userList
     * @return
     */
    boolean judgePrivilege(int userId, int deleteId, List<User> userList);

    /**
     * 判断是否是群组管理员
     *
     * @param userId
     * @param userList
     * @param groupId
     * @return
     */
    boolean judgePrivilege(int userId, List<User> userList, int groupId);

    /**
     * 增加群组的成员
     *
     * @param userId
     * @param groupId
     * @param status
     * @return
     */
    boolean addMember(int userId, int groupId, int status);

    /**
     * 获取好友列表
     *
     * @param loginId
     * @return
     */
    List<User> getFriends(String loginId);

    /**
     * 增加成员(在新建群聊时调用)
     *
     * @param users
     * @param group
     */
    void addMember(String users, int group);

    /**
     * 判断成员是否存在在该群组中
     *
     * @param userId
     * @param groupId
     * @return
     */
    boolean judgeIsExist(int userId, int groupId);

    /**
     * 增加聊天群组
     *
     * @param userId
     * @param group
     * @param users
     * @return
     */
    boolean addGroup(int userId, String group, String users);

    /**
     * 提拔普通用户为管理员
     *
     * @param userId
     * @param groupId
     * @param userList
     * @return
     */
    boolean updateRight(int userId, int groupId, List<User> userList);

    /**
     * 获取私聊聊天记录
     *
     * @param sendId
     * @param receiverId
     * @return
     */
    List<Message> getMessage(int sendId, int receiverId);

    /**
     * 获取群聊聊天记录
     *
     * @param groupId
     * @param receiverId
     * @return
     */
    List<Message> getGroupMsg(int groupId, int receiverId);
}
