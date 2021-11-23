package com.mywechat.dao.impl;

import com.mywechat.dao.GroupDao;
import com.mywechat.model.Constant;
import com.mywechat.model.Message;
import com.mywechat.model.User;
import com.mywechat.util.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDaoImpl implements GroupDao {
    @Override
    public void deleteMember(int userId, int groupId) {
        String sql = "delete from db_group_to_user where user_id = ? and group_id = ?";
        Object[] params = {userId, groupId};
        DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean addMember(int userId, int groupId, int status) {
        String sql = "insert into db_group_to_user values(null, ?, ?, ?)";
        Object[] params = {userId, groupId, status};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean addMember(int userId, int status) {
        String sql = " INSERT INTO db_group_to_user VALUES(NULL, ?, (SELECT db_group_id FROM db_group ORDER BY db_group_id DESC LIMIT 1),?)";
        Object[] params = {userId, status};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public List<User> getFriends(int userId) throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT userId, login_id,nick_name FROM db_user WHERE userId IN (SELECT friend_id FROM db_friends WHERE user_id = ? and status = 1);";
        Object[] params = {userId};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            User user = new User();
            user.setUserId(resultSet.getInt(1));
            user.setLoginId(resultSet.getString(2));
            user.setNickName(resultSet.getString(3));
            userList.add(user);
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return userList;
    }

    @Override
    public boolean judgeIsExist(int userId, int groupId) throws SQLException {
        boolean flag;
        String sql = "SELECT db_group_user_id FROM db_group_to_user WHERE user_id = ? AND group_id = ?";
        Object[] params = {userId, groupId};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        if (resultSet.next()) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean addGroup(int userId, String groupName) {
        String sql = "insert into db_group values(null, ?, ?, ?, null)";
        Object[] params = {userId, groupName, Constant.GROUPICON};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean updateRight(int userId, int groupId) {
        String sql = "update db_group_to_user set status = 1 where user_id = ? and group_id = ?";
        Object[] params = {userId, groupId};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public List<Message> getMessages(int sendId, int receiverId) throws SQLException {
        List<Message> messages;
        String sql;
        ResultSet resultSet;
        if (isDelete(sendId, receiverId)) {
            sql = " SELECT `send_user`, `recipients`,`db_msg_text`,`send_time`, login_id FROM db_messages m, db_user u WHERE m.`send_user`=u.`userId`AND send_user = ? and type =2 AND recipients = ? and send_time > (SELECT delete_time FROM db_messages_delete WHERE delete_user = ? AND  to_id = ?  ORDER BY delete_id DESC LIMIT 1) " +
                    "UNION  SELECT `send_user`, `recipients`,`db_msg_text`,`send_time`, login_id FROM db_messages m, db_user u WHERE m.`send_user`=u.`userId`AND send_user = ? AND recipients = ? and type =2 and send_time >(SELECT delete_time FROM db_messages_delete WHERE delete_user = ? AND  to_id = ?  ORDER BY delete_id DESC LIMIT 1) ORDER BY send_time";
            Object[] params = {sendId, receiverId, sendId, receiverId, receiverId, sendId, sendId, receiverId};
            resultSet = DbUtil.executeQuery(sql, params);
        } else {
            sql = "SELECT `send_user`, `recipients`,`db_msg_text`,`send_time`, login_id FROM db_messages m, db_user u WHERE m.`send_user`=u.`userId`AND send_user = ? and type =2 AND recipients = ? UNION  SELECT `send_user`, `recipients`,`db_msg_text`,`send_time`, login_id FROM db_messages m, db_user u WHERE m.`send_user`=u.`userId`AND send_user = ? AND recipients = ? and type =2 order by send_time";
            Object[] params = {sendId, receiverId, receiverId, sendId};
            resultSet = DbUtil.executeQuery(sql, params);
        }
        messages = getMessage(resultSet);
        return messages;
    }

    @Override
    public List<Message> getGroupMsg(int groupId, int receiverId) throws SQLException {
        List<Message> messages;
        String sql;
        ResultSet resultSet;
        if (isDelete(receiverId, groupId)) {
            sql = " SELECT `send_user`, `recipients`,`db_msg_text`,`send_time`, login_id FROM db_messages m, db_user u WHERE m.`send_user` = u.`userId` " +
                    "AND m.`recipients` = ? AND TYPE = ? and send_time > (SELECT delete_time FROM db_messages_delete WHERE delete_user = ? AND  to_id = ?  ORDER BY delete_id DESC LIMIT 1)";
            Object[] params = {receiverId, groupId, receiverId, groupId};
            resultSet = DbUtil.executeQuery(sql, params);
        } else {
            sql = " SELECT `send_user`, `recipients`,`db_msg_text`,`send_time`, login_id FROM db_messages m, db_user u WHERE m.`send_user` = u.`userId` AND m.`recipients` = ? AND TYPE = ?";
            Object[] params = {receiverId, groupId};
            resultSet = DbUtil.executeQuery(sql, params);
        }
        messages = getMessage(resultSet);
        return messages;
    }

    private static List<Message> getMessage(ResultSet resultSet) throws SQLException {
        List<Message> messages = new ArrayList<>();
        while (resultSet.next()) {
            Message message = new Message();
            message.setFromId(resultSet.getInt(1));
            message.setToId(resultSet.getInt(2));
            message.setSendMsg(resultSet.getString(3));
            message.setDate(resultSet.getString(4));
            message.setFrom(resultSet.getString(5));
            messages.add(message);
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return messages;
    }

    private boolean isDelete(int userId, int receiveId) throws SQLException {
        String sql = "select count(*) from db_messages_delete where delete_user = ? and to_id = ?";
        Object[] params = {userId, receiveId};
        boolean flag = false;
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            int number = resultSet.getInt(1);
            if (number > 0) {
                flag = true;
            }
        }
        return flag;
    }
}
