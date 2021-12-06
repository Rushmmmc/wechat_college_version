package com.mywechat.controller;

import com.mywechat.model.CircleFriends;
import com.mywechat.model.Comment;
import com.mywechat.model.Constant;
import com.mywechat.service.CircleFriendService;
import com.mywechat.service.impl.CircleFriendImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CircleFriendServlet")
public class CircleFriendServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        CircleFriendService circleFriendService = new CircleFriendImpl();
        String operation = request.getParameter("operation");
        PrintWriter out = response.getWriter();
        String loginId = (String) request.getSession().getAttribute("loginId");
        if (operation.equals(Constant.QUERYALL)) {
            /**
             * pages代表当前页数， commentPage代表总体页数
             */
            int pages = Integer.parseInt(request.getParameter("pages"));
            int commentPage = Integer.parseInt(request.getParameter("commentPage"));
            CircleFriends circleFriends = circleFriendService.getCircleFriend(pages, loginId);
            /**
             * 获取朋友圈的评论并存放至变量中
             */
            circleFriends.setComments(circleFriendService.getCircleComment(circleFriends.getCircleId(), commentPage));
            System.out.println(circleFriends.getComments().get(0).getCommentText());
            request.getSession().setAttribute("friendCircle", circleFriends);
            if (circleFriendService.judgeLike(circleFriends.getCircleId(), loginId)) {
                request.getSession().setAttribute("like", 1);
            } else {
                request.getSession().setAttribute("like", 0);
            }
            int circlePages = circleFriendService.getCircleNumber(loginId);
            int commentPages = circleFriendService.commentPages(circleFriends.getCircleId());
            request.getSession().setAttribute("pages", pages);
            request.getSession().setAttribute("commentPage", commentPage);
            request.getSession().setAttribute("circlePages", circlePages);
            request.getSession().setAttribute("commentPages", commentPages);
            response.sendRedirect("jsp/friendCircle.jsp");
        } else if (operation.equals(Constant.ISSUE)) {
            String type = request.getParameter("type");
            if (type.equals(Constant.CIRCLE)) {
                /**
                 * 发布朋友圈操作
                 */
                String circleText = request.getParameter("circleText");
                String photoUrl = request.getParameter("photoUrl");
                CircleFriends circleFriends = new CircleFriends();
                circleFriends.setSendUserName(loginId);
                circleFriends.setPhotoUrl(photoUrl);
                circleFriends.setText(circleText);
                if (circleFriendService.issueCircleFriend(circleFriends)) {
                    out.print("{\"msg\":\"true\"}");
                } else {
                    out.print("{\"msg\":\"false\"}");
                }
            } else {
                /**
                 * 发布评论操作
                 */
                String commentText = request.getParameter("commentText");
                int circleId = Integer.parseInt(request.getParameter("circleId"));
                Comment comment = new Comment();
                comment.setSendUserName(loginId);
                comment.setCircleId(circleId);
                comment.setCommentText(commentText);
                if (circleFriendService.issueComment(comment)) {
                    out.print("{\"msg\":\"true\"}");
                } else {
                    out.print("{\"msg\":\"false\"}");
                }
            }
        } else if (operation.equals(Constant.DOLIKE)) {
            /**
             * 确认点赞
             */
            CircleFriends circleFriends = (CircleFriends) request.getSession().getAttribute("friendCircle");
            if (!circleFriendService.judgeLike(circleFriends.getCircleId(), loginId)) {
                if (circleFriendService.doLike(circleFriends, loginId)) {
                    request.getSession().setAttribute("like", 1);
                    out.print("{\"msg\":\"true\"}");
                } else {
                    out.print("{\"msg\":\"false\"}");
                }
            } else {
                if (circleFriendService.cancelLike(circleFriends, loginId)) {
                    request.getSession().setAttribute("like", 0);
                    out.print("{\"msg\":\"true\"}");
                } else {
                    out.print("{\"msg\":\"false\"}");
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
