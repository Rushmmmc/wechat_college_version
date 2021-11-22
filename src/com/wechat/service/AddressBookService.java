package com.wechat.service;

import com.wechat.po.User;

import java.util.List;

public interface AddressBookService {
    /**
     * 获取好友列表
     *
     * @param loginId
     * @param pages
     * @return
     */
    List<User> getFriends(String loginId, int pages);

    /**
     * 获取该用户的用户列表的数量
     *
     * @param loginId
     * @return
     */
    int getFriendsNum(String loginId);

    /**
     * 获取好友信息
     *
     * @param userList
     * @param userId
     * @return
     */
    User getFriendMsg(List<User> userList, int userId);

    /**
     * 获取未添加好友的列表，调用dao层方法
     *
     * @param loginId
     * @param nickName
     * @param userId
     * @param pages
     * @return
     */
    List<User> getUnAddFriend(String loginId, String nickName, String userId, int pages);

    /**
     * 获取未添加的好友的数量
     *
     * @param loginId
     * @param nickName
     * @param userId
     * @return
     */
    int getUnAddNum(String loginId, String nickName, String userId);

    /**
     * 发送添加好友的请求
     *
     * @param friendId
     * @param loginId
     * @return
     */
    boolean doAddFriend(int friendId, String loginId);

    /**
     * 确认是否添加好友
     * type 1:  表示同意增加   2： 拒绝增加   0：未同意也未拒绝
     *
     * @param friendId
     * @param type
     * @param loginId
     * @return
     */
    boolean confirmAdd(int friendId, int type, String loginId);

    /**
     * 获取申请添加该用户为好友的用户列表
     *
     * @param userId
     * @param pages
     * @return
     */
    List<User> getRequest(String userId, int pages);

    /**
     * 获取申请添加该用户为好友的用户列表数量
     *
     * @param userId
     * @return
     */
    int getRequestNum(String userId);

    /**
     * 删除好友操作
     *
     * @param loginId
     * @param friendId
     * @return
     */
    boolean deleteFriend(String loginId, int friendId);
}
