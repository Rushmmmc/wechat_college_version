package com.mywechat.controller;


import com.mywechat.model.Constant;
import com.mywechat.model.User;
import com.mywechat.service.AddressBookService;
import com.mywechat.service.impl.AddressBookImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 通讯录的SERVLET类
 */
@WebServlet(name = "AddressBookServlet")
public class AddressBookServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AddressBookService addressBookService = new AddressBookImpl();
        PrintWriter out = response.getWriter();
        /**
         * 获取前端传送的操作
         */
        String operation = request.getParameter("operation");
        String loginId = (String) request.getSession().getAttribute("loginId");
        if (operation.equals(Constant.QUERYALL)) {
            /**
             * friendPage存放当前朋友列表页数
             * friendPages存放朋友圈列表的总页数
             */
            int friendPage = Integer.parseInt(request.getParameter("pages"));
            int friendPages = addressBookService.getFriendsNum(loginId);
            request.getSession().setAttribute("friendPage", friendPage);
            request.getSession().setAttribute("friendPages", friendPages);
            List<User> users = addressBookService.getFriends(loginId, friendPage);
            request.getSession().setAttribute("friendList", users);
            request.getSession().setAttribute("friendMsg", users.get(0));
            response.sendRedirect("jsp/addressBook.jsp?message=1");
        } else if (operation.equals(Constant.QUERY)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            List<User> userList = (List<User>) request.getSession().getAttribute("friendList");
            User user = addressBookService.getFriendMsg(userList, userId);
            request.getSession().setAttribute("friendMsg", user);
        } else if (operation.equals(Constant.SEARCH)) {
            /**
             * 用于模糊搜索未添加的好友的名称和账户
             */
            String likeLoginId = request.getParameter("likeLoginId");
            String nickName = request.getParameter("nickName");
            Integer page = Integer.parseInt(request.getParameter("page"));
            /**
             * 由于搜索框会因为点击搜索而清空内容而导致第一页后面的内容
             * 不知搜索的根据是什么，所以设置page=1时，记录下搜索框的内
             * 容，往后的页数可由此来获得搜索框的内容
             */
            if (page == 1) {
                request.getSession().setAttribute("msgInput", likeLoginId);
            } else {
                likeLoginId = (String) request.getSession().getAttribute("msgInput");
                nickName = likeLoginId;
            }
            /**
             * 查询未添加的好友的名单
             */
            List<User> unAddFriend = addressBookService.getUnAddFriend(likeLoginId, nickName, loginId, page);
            request.getSession().setAttribute("unAddFriend", unAddFriend);
            request.getSession().setAttribute("unAddPage", page);
            int pages = addressBookService.getUnAddNum(likeLoginId, nickName, loginId);
            request.getSession().setAttribute("unAddPages", pages);
        } else if (operation.equals(Constant.DOADD)) {
            /**
             * 好友添加的确认操作
             */
            int friendId = Integer.parseInt(request.getParameter("friendId"));
            if (addressBookService.doAddFriend(friendId, loginId)) {
                out.print("{\"msg\":\"true\"}");
            } else {
                out.print("{\"msg\":\"false\"}");
            }
        } else if (operation.equals(Constant.CONFIRMAdd)) {
            String option = request.getParameter("option");
            Integer friendId = Integer.parseInt(request.getParameter("friendId"));
            List<User> userList = (List<User>) request.getSession().getAttribute("requestFriends");
            int type;
            /**
             * 获取前端传送的option，
             * add则为确认添加好友
             * 其他则为拒绝添加好友
             */
            if (option.equals(Constant.ADD)) {
                type = 1;
            } else {
                type = 2;
            }
            if (addressBookService.confirmAdd(friendId, type, loginId)) {
                /**
                 * 在拒绝添加好友的同时移出好友请求名单中
                 */
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getUserId() == friendId) {
                        userList.remove(i);
                    }
                }
                out.print("{\"msg\":\"true\"}");
            } else {
                out.print("{\"msg\":\"false\"}");
            }
        } else if (operation.equals(Constant.ADDREQUEST)) {
            Integer page = Integer.parseInt(request.getParameter("pages"));
            int pages = addressBookService.getRequestNum(loginId);
            List<User> userList = addressBookService.getRequest(loginId, page);
            request.getSession().setAttribute("requestPage", page);
            request.getSession().setAttribute("requestPages", pages);
            request.getSession().setAttribute("requestFriends", userList);
        } else if (operation.equals(Constant.DELETE)) {
            /**
             * 删除好友操作
             */
            int friendId = Integer.parseInt(request.getParameter("friendId"));
            if (addressBookService.deleteFriend(loginId, friendId)) {
                out.print("{\"msg\":\"true\"}");
            } else {
                out.print("{\"msg\":\"false\"}");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
