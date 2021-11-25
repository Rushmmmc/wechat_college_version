package com.mywechat.controller;

import com.mywechat.model.Constant;
import com.mywechat.model.Message;
import com.mywechat.model.User;
import com.mywechat.service.GroupService;
import com.mywechat.service.impl.GroupImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GroupServlet")
public class GroupServlet extends HttpServlet {
    private static GroupService groupService = new GroupImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String operation = request.getParameter("operation");
        String loginId = (String) request.getSession().getAttribute("loginId");
        User user = (User) request.getSession().getAttribute("user");
        int userId = user.getUserId();
        /**
         * 获取群组的成员列表
         */
        List<User> userList = (List<User>) request.getSession().getAttribute("groupMember");
        if (operation.equals(Constant.DELETE)) {
            /**
             * 踢出群聊
             */
            int groupId = Integer.parseInt(request.getParameter("groupId"));
            int deleteId = Integer.parseInt(request.getParameter("userId"));
            /**
             * 如果踢出的是本人，则视为退群操作
             */
            if (deleteId == userId) {
                groupService.deleteMember(deleteId, groupId);
                request.getSession().setAttribute("groupMemDelete", true);
            } else if (groupService.judgePrivilege(userId, deleteId, userList)) {
                /**
                 * 否则执行判断特权操作，如删除人比发出删除的人的权限高
                 * 则删除失败
                 */
                groupService.deleteMember(deleteId, groupId);
                request.getSession().setAttribute("groupMemDelete", true);
            } else {
                request.getSession().setAttribute("groupMemDelete", false);
            }
            response.sendRedirect("ChatServlet?operation=chatGroup&groupId=" + groupId);
        } else if (operation.equals(Constant.ADD)) {
            /**
             * 增加群成员
             */
            String addId = request.getParameter("users");
            int groupId = Integer.parseInt(request.getParameter("groupId"));
            groupService.addMember(addId, groupId);
            response.sendRedirect("ChatServlet?operation=chatGroup&groupId=" + groupId);
        } else if (operation.equals(Constant.DOADD)) {
            /**
             * 创建群聊的操作
             */
            String addId = request.getParameter("users");
            String groupName = request.getParameter("groupName");
            groupService.addGroup(userId, groupName, addId);
            response.sendRedirect("ChatServlet?operation=chatMulti");
        } else if (operation.equals(Constant.UPDATE)) {
            /**
             * 更新群成员在该群的身份
             */
            int groupId = Integer.parseInt(request.getParameter("groupId"));
            int friendId = Integer.parseInt(request.getParameter("userId"));
            groupService.updateRight(friendId, groupId, userList);
            response.sendRedirect("ChatServlet?operation=chatGroup&groupId=" + groupId);
        } else if (operation.equals(Constant.QUERY)) {
            /**
             * 查询与好友的聊天记录
             */
            int receiverId = Integer.parseInt(request.getParameter("receiverId"));
            int sendId = userId;
            List<Message> messages = groupService.getMessage(sendId, receiverId);
            request.getSession().setAttribute("friendMsg", messages);
            response.sendRedirect("jsp/message.jsp?type=1&friendId=" + receiverId);
        } else if (operation.equals(Constant.QUERYALL)) {
            /**
             * 查询群组的聊天记录
             */
            int groupId = Integer.parseInt(request.getParameter("groupId"));
            int receiverId = userId;
            List<Message> messages = groupService.getGroupMsg(groupId, receiverId);
            request.getSession().setAttribute("groupMsg", messages);
            response.sendRedirect("jsp/message.jsp?type=2&groupId=" + groupId);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
