package com.mywechat.service;

import com.mywechat.model.CircleFriends;
import com.mywechat.model.Comment;

import java.sql.SQLException;
import java.util.List;

public interface CircleFriendService {
    /**
     * 获取该用户的好友朋友圈
     *
     * @param papges
     * @param loginId
     * @return
     * @throws SQLException
     */
    CircleFriends getCircleFriend(int papges, String loginId);

    /**
     * 获取用户的好友列表
     *
     * @param pages
     * @param loginId
     * @return
     * @throws SQLException
     */
    List<Integer> getUserFriend(int pages, String loginId);

    /**
     * 获取用户的朋友圈数量
     *
     * @param loginId
     * @return
     * @throws SQLException
     */
    int getCircleNumber(String loginId);

    /**
     * 获取朋友圈评论
     *
     * @param circleId
     * @param pages
     * @return
     * @throws SQLException
     */
    List<Comment> getCircleComment(int circleId, int pages);

    /**
     * 获取评论个数
     *
     * @param circleId
     * @return
     * @throws SQLException
     */
    int commentPages(int circleId);

    /**
     * 发布朋友圈
     *
     * @param circleFriends
     * @return
     */
    boolean issueCircleFriend(CircleFriends circleFriends);

    /**
     * 发布评论
     *
     * @param comment
     * @return
     */
    boolean issueComment(Comment comment);

    /**
     * 判断是否已经点赞
     *
     * @param circleId
     * @param loginId
     * @return
     */
    boolean judgeLike(int circleId, String loginId);

    /**
     * 进行点赞操作
     *
     * @param circleFriends
     * @param loginId
     * @return
     */
    boolean doLike(CircleFriends circleFriends, String loginId);

    /**
     * 取消用户点赞
     *
     * @param circleFriends
     * @param loginId
     * @return
     */
    boolean cancelLike(CircleFriends circleFriends, String loginId);
}
