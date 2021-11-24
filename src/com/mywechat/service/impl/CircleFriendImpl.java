package com.mywechat.service.impl;

import com.mywechat.dao.FriendCircleDao;
import com.mywechat.dao.UserDao;
import com.mywechat.dao.impl.FriendCircleDaoImpl;
import com.mywechat.dao.impl.UserDaoImpl;
import com.mywechat.model.CircleFriends;
import com.mywechat.model.Comment;
import com.mywechat.model.Constant;
import com.mywechat.service.CircleFriendService;
import com.mywechat.util.CountPage;
import com.mywechat.util.DbUtil;

import java.sql.SQLException;
import java.util.List;

public class CircleFriendImpl implements CircleFriendService {
    private static FriendCircleDao friendCircleDao = new FriendCircleDaoImpl();
    private static UserDao userDao = new UserDaoImpl();

    @Override
    public CircleFriends getCircleFriend(int pages, String loginId) {
        int userId = DbUtil.getUserId(loginId);
        CircleFriends circleFriends = null;
        try {
            circleFriends = friendCircleDao.getFriendCircle(userId, pages);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            circleFriends.setSendUserName(userDao.getUserMsg(circleFriends.getSendUser(), 1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return circleFriends;
    }

    @Override
    public List<Integer> getUserFriend(int pages, String loginId) {
        int userId = DbUtil.getUserId(loginId);
        try {
            return friendCircleDao.getUserFriend(userId, pages);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCircleNumber(String loginId) {
        int userId = DbUtil.getUserId(loginId);
        try {
            return friendCircleDao.getCircleNumber(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public List<Comment> getCircleComment(int circleId, int pages) {
        try {
            return friendCircleDao.getComment(circleId, pages);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int commentPages(int circleId) {
        int number = 0;
        try {
            number = friendCircleDao.getCommentNum(circleId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return CountPage.getSize(number, Constant.FRIENDLIST);
    }

    @Override
    public boolean issueCircleFriend(CircleFriends circleFriends) {
        circleFriends.setSendTime();
        try {
            circleFriends.setSendUser(userDao.getUserId(circleFriends.getSendUserName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendCircleDao.issueCircleFriend(circleFriends);
    }

    @Override
    public boolean issueComment(Comment comment) {
        comment.setSendTime(CircleFriends.getCurrentTime());
        try {
            comment.setSendUser(userDao.getUserId(comment.getSendUserName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendCircleDao.issueComment(comment);
    }

    @Override
    public boolean judgeLike(int circleId, String loginId) {
        int userId = DbUtil.getUserId(loginId);
        return friendCircleDao.judgeLike(circleId, userId);
    }

    @Override
    public boolean doLike(CircleFriends circleFriends, String loginId) {
        return likeOpera(circleFriends, loginId, 1);
    }

    @Override
    public boolean cancelLike(CircleFriends circleFriends, String loginId) {
        return likeOpera(circleFriends, loginId, 2);
    }

    private static boolean likeOpera(CircleFriends circleFriends, String loginId, int type) {
        /**
         * 更新点赞状态并更新点赞数
         */
        if (type == 1) {
            circleFriends.setLike(circleFriends.getLike() + 1);
        } else {
            circleFriends.setLike(circleFriends.getLike() - 1);
        }
        friendCircleDao.updateLike(circleFriends);
        int userId = DbUtil.getUserId(loginId);
        if (type == 1) {
            return friendCircleDao.doLike(circleFriends.getCircleId(), userId);
        } else {
            return friendCircleDao.cancelLike(circleFriends.getCircleId(), userId);
        }
    }
}
