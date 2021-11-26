package com.mywechat.controller;

import com.google.gson.Gson;
import com.mywechat.model.CircleFriends;
import com.mywechat.model.Message;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * 公共聊天室实现类
 */
@ServerEndpoint("/traverseSocket")
public class TraverseSocket {

    private static Set<TraverseSocket> sockets = new HashSet<TraverseSocket>();

    private Session session;
    private String username;
    private Gson gson = new Gson();

    @OnOpen
    public void open(Session session) {
        this.session = session;
        sockets.add(this);

        String queryString = session.getQueryString();
        this.username = queryString.substring(queryString.indexOf("=") + 1);
        Message message = new Message();
        message.setAlert(this.username + "进入聊天室！！");
        broadcast(sockets, gson.toJson(message));

    }

    @OnMessage
    public void receive(Session session, String messageSend) {
        Message message = new Message();
        message.setSendMsg(messageSend);
        message.setFrom(this.username);
        message.setDate(CircleFriends.getCurrentTime());
        broadcast(sockets, gson.toJson(message));
    }

    @OnClose
    public void close(Session session) {
        sockets.remove(this);
        Message message = new Message();
        message.setAlert(this.username + "退出聊天室！！");
        broadcast(sockets, gson.toJson(message));
    }

    public void broadcast(Set<TraverseSocket> ss, String msg) {

        for (Iterator iterator = ss.iterator(); iterator.hasNext(); ) {
            TraverseSocket traverseSocket = (TraverseSocket) iterator.next();
            try {
                traverseSocket.session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}