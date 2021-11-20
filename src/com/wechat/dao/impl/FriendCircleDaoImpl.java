package com.wechat.dao.impl;

import com.wechat.constant.Constant;
import com.wechat.dao.FriendCircleDao;
import com.wechat.po.CircleFriends;
import com.wechat.po.Comment;
import com.wechat.util.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendCircleDaoImpl implements FriendCircleDao {

    @Override
    public CircleFriends getFriendCircle(int userId, int pages) throws SQLException {
        String sql = "SELECT *FROM db_circle_friends  WHERE send_user IN (SELECT friend_id FROM db_friends WHERE user_id = ? AND STATUS=1) ORDER BY send_time desc limit ?, 1";
        ResultSet resultSet;
        CircleFriends circleFriends = new CircleFriends();
        Object[] params = {userId, pages - 1};
        resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            circleFriends.setCircleId(resultSet.getInt("circle_friend_id"));
            circleFriends.setSendUser(resultSet.getInt("send_user"));
            circleFriends.setText(resultSet.getString("circle_text"));
            circleFriends.setSendTime(resultSet.getString("send_time"));
            circleFriends.setLike(resultSet.getInt("likes"));
            circleFriends.setPhotoUrl(resultSet.getString("photo_url"));
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return circleFriends;
    }

    @Override
    public List<Integer> getUserFriend(int userId, int pages) throws SQLException {
        String sql = "SELECT friend_id FROM db_friends WHERE user_id = ? AND STATUS=1 limit ?, ?";
        Object[] params = {userId, (pages - 1) * Constant.FRIENDLIST, Constant.FRIENDLIST};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        List<Integer> friendIdList = new ArrayList<>();
        while (resultSet.next()) {
            int friendId = resultSet.getInt("friend_id");
            friendIdList.add(friendId);
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return friendIdList;
    }

    @Override
    public int getCircleNumber(int userId) throws SQLException {
        String sql = "SELECT COUNT(*)FROM db_circle_friends  WHERE send_user IN (SELECT friend_id FROM db_friends WHERE user_id = ? AND STATUS=1)";
        Object[] params = {userId};
        return DbUtil.getNumber(params, sql);
    }

    @Override
    public List<Comment> getComment(int circleId, int pages) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT *FROM db_comment WHERE circle_id = ? limit ?, ?";
        Object[] params = {circleId, (pages - 1) * Constant.SIZES, Constant.SIZES};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            Comment comment = new Comment();
            comment.setCircleId(resultSet.getInt("circle_id"));
            comment.setCommentText(resultSet.getString("text"));
            comment.setSendTime(resultSet.getString("send_time"));
            comment.setSendUser(resultSet.getInt("send_user"));
            comment.setSendUserName(new UserDaoImpl().getUserMsg(comment.getSendUser(), 1));
            comments.add(comment);
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return comments;
    }

    @Override
    public int getCommentNum(int circleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM db_comment  WHERE circle_id = ?";
        Object[] params = {circleId};
        return DbUtil.getNumber(params, sql);
    }

    @Override
    public boolean issueCircleFriend(CircleFriends circleFriends) {
        String sql = "INSERT INTO db_circle_friends (circle_friend_id, send_user, circle_text, send_time, likes, photo_url) VALUES (NULL,?, ?,?, 0, ?)";
        Object[] params = {circleFriends.getSendUser(), circleFriends.getText(), circleFriends.getSendTime(), circleFriends.getPhotoUrl()};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean issueComment(Comment comment) {
        String sql = "INSERT INTO db_comment (comment_id, TEXT, send_user, send_time, circle_id) VALUES(NULL, ?, ?, ?, ?)";
        Object[] params = {comment.getCommentText(), comment.getSendUser(), comment.getSendTime(), comment.getCircleId()};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean judgeLike(int circleId, int userId) {
        String sql = "SELECT *FROM db_circle_like WHERE circle_id = ? AND user_id = ?";
        Object[] params = {circleId, userId};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (true) {
            try {
                if (!resultSet.next()) {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
            return true;
        }
    }

    @Override
    public boolean doLike(int circleId, int userId) {
        String sql = "INSERT INTO db_circle_like SET circle_id = ?, user_id = ?";
        Object[] params = {circleId, userId};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean updateLike(CircleFriends circleFriends) {
        String sql = "UPDATE db_circle_friends SET likes = ? WHERE circle_friend_id = ?";
        Object[] params = {circleFriends.getLike(), circleFriends.getCircleId()};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean cancelLike(int circleId, int userId) {
        String sql = "DELETE  FROM db_circle_like WHERE circle_id = ? AND user_id = ?";
        Object[] params = {circleId, userId};
        return DbUtil.executeUpdate(sql, params);
    }
}
