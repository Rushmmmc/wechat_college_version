package com.wechat.controller;



import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;


@ServerEndpoint("/chatSocket")//声明webSocket服务器的地址

public class ChatSocket {
    /**
     * sockets用于存放聊天管道，可实现消息的实时通知
     */
    private static Set<ChatSocket> sockets = new HashSet<ChatSocket>();
    /**
     * sMap用于存放用户名与管道的对应，可实现私聊和聊天室聊天
     */
    private static Map<String, Session> sMap = new HashMap<String, Session>();
    /**
     * names用于存放进来聊天室的用户名字
     */
    private static List<String> names = new ArrayList<String>();
    /**
     * 用于存放用户的窗口状态
     */
    private static Map<String, Integer> userStatus = new HashedMap();
    private Session session;
    private String sendId;
    private Integer receiveId;
    private Gson gson = new Gson();
    private ChatService chatService = new ChatImpl();

    @OnOpen
    public void open(Session session) {  //open就是连接之后进行的操作
        this.session = session;
        sockets.add(this);
        /**
         * 获取账户的loginId
         */
        String queryString = session.getQueryString();
        this.sendId = queryString.substring(queryString.indexOf("=") + 1, queryString.indexOf("?"));
        this.receiveId = Integer.parseInt(queryString.substring(queryString.lastIndexOf("=") + 1));
        /**
         * 将该用户的loginId加入名单
         */
        names.add(this.sendId);
        /**
         * 将该用户的loginId与对应的session对应起来
         */
        sMap.put(this.sendId, session);
        /**
         * 更新当前用户的聊天对象
         */
        userStatus.put(sendId, receiveId);
    }

    //接受前端onMessage方法传送过来的数据
    @OnMessage
    public void receive(Session session, String json) {
        /**
         * 获取前端发送至chatsocket的数据，并将其转化成Message类
         */
        Message sendFrom = gson.fromJson(json, Message.class);
        /**
         * 新的消息发送类，用于更新消息状态
         */
        Message sendTo = new Message();
        /**
         * 获取发送用户的loginId（账户）
         */
        sendFrom.setFrom(this.sendId);
        /**
         * 设置消息的发送时间
         */
        sendFrom.setDate(CircleFriends.getCurrentTime());
        /**
         * 查看当前是否是私聊或者是群聊
         * 1 则为群聊 2为私聊
         */
        if (sendFrom.getType() == 2) {
            /**
             * 获取接受者的userId
             */
            int toId = sendFrom.getToId();
            sendTo.setToId(toId);
            /**
             * 获取接受者的loginId
             */
            String to = chatService.getUserLoginId(toId);
            sendFrom.setTo(to);
            sendTo.setFromId(sendFrom.getFromId());
            /**
             * 获取接受者管道
             */
            Session toSession = sMap.get(to);
            Integer status = userStatus.get(to);
            /**
             * 在获取管道之前，先判断接受者是否正在与发送者聊天
             * 如不是，则将聊天消息存储并提醒接受者有新消息
             * 如是，则直接将聊天消息发送至接受者聊天窗口
             */
            if (status == null) {
                sendFrom.setStatus(0);
                sendFrom.setAlert("对方不在线！");
            } else if (status != sendFrom.getFromId()) {
                sendFrom.setStatus(0);
                sendFrom.setAlert("对方正在与别人聊天！");
                sendTo.setStatus(0);
            } else {
                sendFrom.setStatus(1);
            }
            /**
             * 获取发送者的管道
             */
            Session fromSession = sMap.get(this.sendId);
            String currentTime = CircleFriends.getCurrentTime();
            sendFrom.setDate(currentTime);
            try {
                if (status == null) {
                    /**
                     * 状态为空！说明对方未打开聊天管道
                     * 因此将数据存储至数据库，且只发送消息给发送者即可
                     */
                    chatService.addMessage(sendFrom);
                    fromSession.getBasicRemote().sendText(gson.toJson(sendFrom));
                } else if (status != sendFrom.getFromId()) {
                    /**
                     * status不为空，判断此时接受者并未与发送者
                     * 处于同一窗口，于是给接受者发送消息提醒
                     */
                    chatService.addMessage(sendFrom);
                    toSession.getBasicRemote().sendText(gson.toJson(sendTo));
                    fromSession.getBasicRemote().sendText(gson.toJson(sendFrom));
                } else {
                    chatService.addMessage(sendFrom);
                    toSession.getBasicRemote().sendText(gson.toJson(sendFrom));
                    fromSession.getBasicRemote().sendText(gson.toJson(sendFrom));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            /**
             * 根据群聊ID获取群员列表
             */
            List<User> userList = chatService.getGroupMember(receiveId);
            int toId;
            for (User user : userList) {
                Integer status = userStatus.get(user.getLoginId());
                toId = user.getUserId();
                sendFrom.setToId(toId);
                if (status == null || !status.equals(receiveId)) {
                    sendFrom.setStatus(0);
                } else {
                    sendFrom.setStatus(1);
                }
                Session sessionGroup = sMap.get(user.getLoginId());
                if (sessionGroup == null) {
                    chatService.addMessage(sendFrom);
                } else {
                    try {
                        chatService.addMessage(sendFrom);
                        if (sendFrom.getStatus() == 0) {
                        } else {
                            sessionGroup.getBasicRemote().sendText(gson.toJson(sendFrom));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //关闭websocket服务器后会进行的操作
    @OnClose
    public void close(Session session) {
        sockets.remove(this);
        names.remove(this.sendId);
        sMap.remove(this.sendId);
        userStatus.remove(this.sendId);

    }
}