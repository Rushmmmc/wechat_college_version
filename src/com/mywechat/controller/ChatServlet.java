package com.mywechat.controller;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 聊天的servlet类
 */
@WebServlet(name = "ChatServlet")
public class ChatServlet extends HttpServlet {
    ChatService chatService = new ChatImpl();
    GroupService groupService = new GroupImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String operation = request.getParameter("operation");
        String loginId = (String) request.getSession().getAttribute("loginId");
        List<User> chatList;
        chatList = chatService.getChatList(loginId);
        System.out.println(chatList);
        request.getSession().setAttribute("chatList", chatList);
        int friendId = -1, groupId = -1;
        if (request.getParameter("friendId") != null) {
            friendId = Integer.parseInt(request.getParameter("friendId"));
        }
        if (request.getParameter("groupId") != null) {
            groupId = Integer.parseInt(request.getParameter("groupId"));
        }
        if (operation.equals(Constant.CHATTYPEONLY)) {
            /**
             * 因为这里是单聊，所以type是2
             */
            chatService.addChat(friendId, 2, loginId);
            /**
             * 获取用户的聊天列表
             */
            List<User> users = chatService.getChatList(loginId);
            /**
             * 获取因下线而未接受到的信息
             */
            List<Message> messages = chatService.getMessage(friendId, 0, loginId);
            String nickName = chatService.getFriendNickName(friendId);
            request.getSession().setAttribute("friendNickName", nickName);
            request.getSession().setAttribute("chatList", users);
            request.getSession().setAttribute("chatUnAccept", messages);
            response.sendRedirect("jsp/user.jsp?type=2&friendId=" + friendId);
        } else if (operation.equals(Constant.CHATMULTI)) {
            /**、
             * 获取用户的群聊列表，以及自己的好友列表
             * 并将它们存放至session
             */
            List<Group> groups = chatService.getGroup(loginId);
            List<User> friendList = groupService.getFriends(loginId);
            request.getSession().setAttribute("friendMem", friendList);
            request.getSession().setAttribute("groupList", groups);
            response.sendRedirect("jsp/chatGroup.jsp?type=0&groupId=0");
        } else if (operation.equals(Constant.QUERY)) {
            response.sendRedirect("jsp/user.jsp?type=0&friendId=0");
        } else if (operation.equals(Constant.CHATGROUP)) {
            List<Group> groupList = (List<Group>) request.getSession().getAttribute("groupList");
            /**
             * 获取该群未接受到的消息记录
             */
            List<Message> getUnAccept = chatService.getUnAccept(groupId, loginId);
            List<User> groupMember = chatService.getGroupMember(groupId);
            /**
             * 获取本人在该群的权限状态
             */
            int status = chatService.getUserStatus(groupMember, loginId);
            String groupName = chatService.getGroupName(groupId, groupList);
            request.getSession().setAttribute("thisUserStatus", status);
            request.getSession().setAttribute("unAcceptGroup", getUnAccept);
            request.getSession().setAttribute("groupName", groupName);
            request.getSession().setAttribute("groupMember", groupMember);
            response.sendRedirect("jsp/chatGroup.jsp?type=1&groupId=" + groupId);
        } else if (operation.equals(Constant.DELETE)) {
            /**
             * 删除聊天记录操作
             */
            if (friendId != -1) {
                chatService.deleteMsg(friendId, loginId, CircleFriends.getCurrentTime());
            } else if (groupId != -1) {
                chatService.deleteMsgGroup(groupId, loginId, CircleFriends.getCurrentTime());
            }
            response.sendRedirect("jsp/chatGroup.jsp?type=0&groupId=0");
        } else if (operation.equals(Constant.ISSUE)) {
            /**
             * 下载聊天记录操作
             * 下载的文件存放在项目根目录的message文件夹中
             */
            if (friendId != -1) {
                List<Message> messages = (List<Message>) request.getSession().getAttribute("friendMsg");
                chatService.produceMsg(messages, friendId);
                request.getSession().setAttribute("produceMsg", true);
                response.sendRedirect("jsp/message.jsp?type=1&friendId=" + friendId);
            } else if (groupId != -1) {
                List<Message> messages = (List<Message>) request.getSession().getAttribute("groupMsg");
                chatService.produceMsg(messages, groupId);
                request.getSession().setAttribute("produceMsg", true);
                response.sendRedirect("jsp/message.jsp?type=2&groupId=" + groupId);
            }

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
