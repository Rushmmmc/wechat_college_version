package com.mywechat.service.impl;

import com.mywechat.dao.ChatDao;
import com.mywechat.dao.UserDao;
import com.mywechat.dao.impl.ChatDaoImpl;
import com.mywechat.dao.impl.UserDaoImpl;
import com.mywechat.model.*;
import com.mywechat.service.ChatService;
import com.mywechat.util.DbUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatImpl implements ChatService {
    private static UserDao userDao = new UserDaoImpl();
    private static ChatDao chatDao = new ChatDaoImpl();

    @Override
    public List<User> getChatList(String loginId) {
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<User> userList = new ArrayList<>();
        List<Chat> chatList = new ArrayList<>();
        try {
            /**
             * 获取最近的聊天列表
             */
            chatList = chatDao.getChatList(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * 通过获取到的聊天列表对里面的成员进行信息收集
         */
        for (Chat chat : chatList) {
            User user = new User();
            user.setUserId(chat.getReceiver());
            try {
                /**
                 * 获取成员信息
                 */
                user.setLoginId(userDao.getUserMsg(user.getUserId(), 2));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                user = userDao.getMessage(user.getLoginId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            userList.add(user);
        }
        return userList;
    }

    @Override
    public String getUserLoginId(int userId) {
        String userLoginId = null;
        try {
            userLoginId = chatDao.getUserLoginId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userLoginId;
    }

    @Override
    public boolean addMessage(Message message) {
        return chatDao.addMessage(message);
    }

    @Override
    public boolean addChat(int friendId, int type, String loginId) {
        Chat chat = new Chat();
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        chat.setSendUser(userId);
        chat.setReceiver(friendId);
        chat.setType(type);
        deleteChat(chat);
        return chatDao.addChat(chat);
    }

    @Override
    public boolean deleteChat(Chat chat) {
        return chatDao.deleteChat(chat);
    }

    @Override
    public String getFriendNickName(int friendId) {
        String nickName = null;
        try {
            nickName = userDao.getUserMsg(friendId, 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nickName;
    }

    @Override
    public List<Message> getMessage(int receiver, int status, String loginId) {
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Message> messages = null;
        try {
            /**
             * 获取用户未接受的消息
             * status ： 0
             * 代表： 消息处于未接受状态
             */
            messages = chatDao.getMessage(userId, receiver, 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * 获取之后将消息状态设置为已接收
         * type：2 （私聊）
         * 已接收
         */
        chatDao.updateMessage(userId, receiver, 2);
        return messages;
    }

    @Override
    public List<Message> getUnAccept(int groupId, String loginId) {
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            System.out.println("获取账户ID失败");
        }
        List<Message> messages = new ArrayList<>();
        try {
            /**
             * 获取未接受的群聊消息
             */
            messages = chatDao.getUnAccept(userId, groupId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * 获取后更新原本消息状态
         */
        chatDao.updateMessage(userId, groupId);
        return messages;
    }

    @Override
    public List<Group> getGroup(String loginId) {
        List<Group> groups = new ArrayList<>();
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            System.out.println("获取用户ID失败");
        }
        try {
            groups = chatDao.getGroup(userId);
        } catch (SQLException e) {
            System.out.println("获取用户群聊列表出错！");
            e.printStackTrace();
        }
        return groups;
    }

    @Override
    public List<User> getGroupMember(int groupId) {
        List<User> userList = new ArrayList<>();
        try {
            userList = chatDao.getGroupMember(groupId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public String getGroupName(int groupId, List<Group> groupList) {
        String groupName = null;
        for (Group group : groupList) {
            if (group.getGroupId() == groupId) {
                groupName = group.getGroupName();
            }
        }
        return groupName;
    }

    @Override
    public int getUserStatus(List<User> userList, String loginId) {
        int status = -1;
        int userId = DbUtil.getUserId(loginId);
        for (User user :
                userList) {
            if (userId == user.getUserId()) {
                status = user.getStatus();
            }
        }
        return status;
    }

    @Override
    public boolean deleteMsgGroup(int groupId, String loginId, String deleteTime) {
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatDao.deleteMsgGroup(userId, groupId, deleteTime);
    }

    @Override
    public boolean deleteMsg(int sendUser, String loginId, String deleteTime) {
        int userId = -1;
        try {
            userId = userDao.getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatDao.deleteMsg(sendUser, userId, deleteTime);
    }

    @Override
    public void produceMsg(List<Message> messages, int id) {
        File file;
        FileWriter fw = null;
        /**
         * 将聊天记录写在项目根目录的message中
         */
        file = new File("E:\\study\\java\\java-code\\mywechat\\messages\\" + Constant.user.getNickName() + "to" + id + ".txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file);
            for (Message message :
                    messages) {
                fw.write("发送者是：" + message.getFrom() + ",");
                fw.write("接受者Id是" + message.getToId() + ",");
                fw.write("发送时间是：" + message.getDate() + "\r\n");
                fw.write("内容是：" + message.getSendMsg() + "\r\n");
                fw.flush();
            }
            System.out.println("写数据成功！");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

    }
}
