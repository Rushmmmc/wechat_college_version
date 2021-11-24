package com.mywechat.service.impl;

import com.mywechat.dao.GroupDao;
import com.mywechat.dao.UserDao;
import com.mywechat.dao.impl.GroupDaoImpl;
import com.mywechat.dao.impl.UserDaoImpl;
import com.mywechat.model.Constant;
import com.mywechat.model.Message;
import com.mywechat.model.User;
import com.mywechat.service.GroupService;
import com.mywechat.util.DbUtil;

import java.sql.SQLException;
import java.util.List;

public class GroupImpl implements GroupService {

    private static GroupDao groupDao = new GroupDaoImpl();

    @Override
    public void deleteMember(int userId, int groupId) {
        groupDao.deleteMember(userId, groupId);
    }

    @Override
    public boolean judgePrivilege(int userId, int deleteId, List<User> userList) {
        /**
         * 获取发送请求者的权限
         */
        int privilegeUser = -1;
        /**
         * 获取被操作者的权限
         */
        int privilegeDelete = -1;
        for (User user : userList) {
            if (user.getUserId() == userId) {
                privilegeUser = user.getStatus();
            }
            if (user.getUserId() == deleteId) {
                privilegeDelete = user.getStatus();
            }
        }
        /**
         * 对权限进行判断，当出现操作者的权限低于被操作者
         * 则不可进行操作
         */
        if (privilegeUser > privilegeDelete) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean judgePrivilege(int userId, List<User> userList, int groupId) {
        int status = -1;
        for (User user :
                userList) {
            if (userId == user.getUserId()) {
                status = user.getStatus();
            }
        }
        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addMember(int userId, int groupId, int status) {
        return groupDao.addMember(userId, groupId, status);
    }

    @Override
    public List<User> getFriends(String loginId) {
        int userId = DbUtil.getUserId(loginId);
        try {
            return groupDao.getFriends(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addMember(String users, int group) {
        /**
         * users为前端传送的一组用户字符串
         * 通过对其进行切割而获取对应的用户
         * 主要用户与后端的信息传递
         */
        String[] user = users.split("and");
        for (String userString :
                user) {
            int userId = Integer.parseInt(userString);
            if (!judgeIsExist(userId, group)) {
                addMember(userId, group, 0);
            }
        }
    }

    @Override
    public boolean judgeIsExist(int userId, int groupId) {
        try {
            return groupDao.judgeIsExist(userId, groupId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addGroup(int userId, String group, String users) {
        groupDao.addGroup(userId, group);
        /**
         * users为前端传送的一组用户字符串
         * 通过对其进行切割而获取对应的用户
         * 主要用户与后端的信息传递
         */
        String[] user = users.split("and");
        for (String userString :
                user) {
            int memberId = Integer.parseInt(userString);
            if (memberId == userId) {
                groupDao.addMember(userId, 2);
            } else {
                groupDao.addMember(memberId, 0);
            }
        }
        return true;
    }

    @Override
    public boolean updateRight(int userId, int groupId, List<User> userList) {
        int status;
        /**
         * 更新用户权限
         * 1. 更新的用户已经是管理员或群主，则无需更新
         * 2. 操作者本身并没有发起提升权限的权限，返回false
         * 3. 操作者本身是管理员，被操作者是普通成员，则返回true
         */
        for (User user :
                userList) {
            if (userId == user.getUserId()) {
                status = user.getStatus();
                if (status > 0) {
                    return false;
                }
            }
            if (Constant.user.getUserId() == user.getUserId()) {
                if (user.getStatus() == 0) {
                    return false;
                }
            }
        }
        return groupDao.updateRight(userId, groupId);
    }

    @Override
    public List<Message> getMessage(int sendId, int receiverId) {
        try {
            return groupDao.getMessages(sendId, receiverId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Message> getGroupMsg(int groupId, int receiverId) {
        try {
            return groupDao.getGroupMsg(groupId, receiverId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
