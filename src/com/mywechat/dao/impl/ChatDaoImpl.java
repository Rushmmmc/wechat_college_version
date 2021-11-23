package com.mywechat.dao.impl;

import com.mywechat.dao.ChatDao;
import com.mywechat.dao.UserDao;
import com.mywechat.model.Chat;
import com.mywechat.model.Group;
import com.mywechat.model.Message;
import com.mywechat.model.User;
import com.mywechat.util.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatDaoImpl implements ChatDao {
    @Override
    public List<Chat> getChatList(int userId) throws SQLException {
        List<Chat> chats = new ArrayList<>();
        UserDao userDao = new UserDaoImpl();
        String sql = "SELECT * FROM db_recent_chat where userId = ? ORDER BY chatId DESC LIMIT 9";
        Object[] params = {userId};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            Chat chat = new Chat();
            chat.setChatId(resultSet.getInt("chatId"));
            chat.setReceiver(resultSet.getInt("receive"));
            chat.setSendUser(resultSet.getInt("userId"));
            chat.setType(resultSet.getInt("type"));
            chat.setReceiverName(userDao.getUserMsg(chat.getReceiver(), 1));
            chats.add(chat);
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return chats;
    }

    @Override
    public String getUserLoginId(int userId) throws SQLException {
        String sql = "select login_id from db_user where userId = ?";
        String loginId = null;
        Object[] params = {userId};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            loginId = resultSet.getString("login_id");
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return loginId;
    }

    @Override
    public boolean addMessage(Message message) {
        String sql = "insert into db_messages values (null,?, ?, ?, ?, ?, ?)";
        Object[] params = {message.getFromId(), message.getToId(), message.getSendMsg(), message.getDate(), message.getStatus(), message.getType()};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean addChat(Chat chat) {
        String sql = "insert into db_recent_chat values (null, ?, ?, ?)";
        Object[] params = {chat.getType(), chat.getSendUser(), chat.getReceiver()};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean deleteChat(Chat chat) {
        String sql = "delete from db_recent_chat where type = ? and userId = ? and receive = ?";
        Object[] params = {chat.getType(), chat.getSendUser(), chat.getReceiver()};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public List<Message> getMessage(int sendUser, int receiver, int status) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT `send_user`, `recipients`,`db_msg_text`,`send_time`, login_id FROM db_messages m, db_user u WHERE m.`send_user`=u.`userId`AND send_user = ? AND recipients = ? AND STATUS = ?";
        Object[] params = {receiver, sendUser, status};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            Message message = new Message();
            message.setSendMsg(resultSet.getString("db_msg_text"));
            message.setDate(resultSet.getString("send_time"));
            message.setFrom(resultSet.getString("login_id"));
            messages.add(message);
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return messages;
    }

    @Override
    public List<Message> getUnAccept(int userId, int type) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = " SELECT send_user, db_msg_text, send_time, login_id FROM db_messages,db_user WHERE db_messages.`send_user`=db_user.`userId` AND recipients = ? AND STATUS = 0 AND TYPE = ?";
        Object[] params = {userId, type};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            Message message = new Message();
            message.setFromId(resultSet.getInt(1));
            message.setSendMsg(resultSet.getString(2));
            message.setDate(resultSet.getString(3));
            message.setFrom(resultSet.getString(4));
            messages.add(message);
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return messages;
    }

    @Override
    public boolean updateMessage(int sendUser, int receiver, int type) {
        String sql = "UPDATE db_messages SET STATUS = 1 WHERE send_user = ? AND recipients = ? and type = ?";
        Object[] params = {receiver, sendUser, type};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean updateMessage(int receiver, int type) {
        String sql = "UPDATE db_messages SET STATUS = 1 WHERE  recipients = ? and type = ?";
        Object[] params = {receiver, type};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public List<Group> getGroup(int userId) throws SQLException {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT * FROM db_group WHERE db_group_id IN (SELECT group_id FROM db_group_to_user WHERE user_id = ?)";
        Object[] params = {userId};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            Group group = new Group();
            group.setGroupId(resultSet.getInt("db_group_id"));
            group.setGroupIcon(resultSet.getString("group_icon"));
            group.setUserId(resultSet.getInt("user_id"));
            group.setGroupName(resultSet.getString("group_name"));
            group.setNotice(resultSet.getString("group_notice"));
            groups.add(group);
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return groups;
    }

    @Override
    public List<User> getGroupMember(int groupId) throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT *,`status` FROM db_user u, db_group_to_user g WHERE u.`userId`=g.`user_id` AND u.`userId` IN (SELECT user_id FROM\n" +
                "db_group_to_user WHERE group_id = ?)AND g.`group_id`=?;";
        Object[] params = {groupId, groupId};
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        while (resultSet.next()) {
            User user = new User();
            user.setUserId(resultSet.getInt("userId"));
            user.setNickName(resultSet.getString("nick_name"));
            user.setHeadPortrait(resultSet.getString("head_portrait"));
            user.setLoginId(resultSet.getString("login_id"));
            user.setStatus(resultSet.getInt("status"));
            userList.add(user);
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return userList;
    }

    @Override
    public boolean deleteMsgGroup(int receiverId, int groupId, String deleteTime) {
        String sql = "insert into db_messages_delete values(null, ?, ?, ?)";
        Object[] params = {groupId, deleteTime, receiverId};
        return DbUtil.executeUpdate(sql, params);
    }

    @Override
    public boolean deleteMsg(int sendUser, int receiverId, String deleteTime) {
        String sql = "insert into db_messages_delete  values(null, ?, ?, ?)";
        Object[] params = {sendUser, deleteTime, receiverId};
        return DbUtil.executeUpdate(sql, params);
    }
}
