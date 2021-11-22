package com.mywechat.dao;


import com.mywechat.model.CircleFriends;
import com.mywechat.model.Comment;

import java.sql.SQLException;
import java.util.List;

public interface FriendCircleDao {
    /**
     * 获取用户的好友朋友圈
     *
     * @param userId
     * @param pages
     * @return
     * @throws SQLException
     */
    CircleFriends getFriendCircle(int userId, int pages) throws SQLException;

    /**
     * 获取用户的好友列表
     *
     * @param loginId
     * @param pages
     * @return
     * @throws SQLException
     */
    List<Integer> getUserFriend(int loginId, int pages) throws SQLException;

    /**
     * 获取用户朋友圈数量
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    int getCircleNumber(int userId) throws SQLException;

    /**
     * 获取绑定朋友圈的评论
     *
     * @param circleId
     * @param pages
     * @return
     * @throws SQLException
     */
    List<Comment> getComment(int circleId, int pages) throws SQLException;

    /**
     * 获取朋友圈评论条数
     *
     * @param circleId
     * @return
     * @throws SQLException
     */
    int getCommentNum(int circleId) throws SQLException;

    /**
     * 发布朋友圈
     *
     * @param circleFriends
     * @return
     */
    boolean issueCircleFriend(CircleFriends circleFriends);

    /**
     * 发表评论
     *
     * @param comment
     * @return
     */
    boolean issueComment(Comment comment);

    /**
     * 获取用户是否点赞的状态
     *
     * @param circleId
     * @param userId
     * @return
     */
    boolean judgeLike(int circleId, int userId);

    /**
     * 对朋友圈进行点赞
     *
     * @param circleId
     * @param userId
     * @return
     */
    boolean doLike(int circleId, int userId);

    /**
     * 更新数据库的赞数
     *
     * @param circleFriends
     * @return
     */
    boolean updateLike(CircleFriends circleFriends);

    /**
     * 取消用户点赞
     *
     * @param circleId
     * @param userId
     * @return
     */
    boolean cancelLike(int circleId, int userId);
}
