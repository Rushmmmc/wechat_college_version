package com.wechat.controller;


import com.wechat.constant.Constant;
import com.wechat.po.User;
import com.wechat.service.IndexService;
import com.wechat.service.impl.IndexImpl;
import com.wechat.util.Md5Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 注册类
 */
@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String operation = request.getParameter("operation");
        String loginId = request.getParameter("loginId");
        String password = request.getParameter("password");
        String nickName = request.getParameter("nickName");
        String mailbox = request.getParameter("mailbox");
        String phone = request.getParameter("phone");
        String idCard = request.getParameter("idCard");
        PrintWriter out = response.getWriter();
        IndexService indexService = new IndexImpl();
        if (Constant.CHECKID.equals(operation)) {
            /**
             * 检查loginId是否已被使用
             */
            outPrint(response, indexService.queryMessage(loginId, operation));
        } else if (Constant.CHECKMAIL.equals(operation)) {
            /**
             * 检查邮箱是否已被使用
             */
            outPrint(response, indexService.queryMessage(mailbox, operation));
        } else if (Constant.CHECKPHONE.equals(operation)) {
            /**
             * 检查手机是否已被使用
             */
            outPrint(response, indexService.queryMessage(phone, operation));
        } else if (Constant.CHECKIDCARD.equals(operation)) {
            /**
             * 检查验证码是否正确
             */
            outPrint(response, indexService.queryMessage(idCard, operation));
        } else if (Constant.ADD.equals(operation)) {
            User user = new User();
            user.setLoginId(loginId);
            user.setPassword(Md5Util.getMD5String(password));
            user.setIdCard(idCard);
            user.setMailBox(mailbox);
            user.setNickName(nickName);
            user.setPhone(phone);
            user.setSignature("这位用户太懒,没留下什么动态");
            user.setHeadPortrait(Constant.DEFAULTPOR + "portrait.png");
            user.setBackground(Constant.DEFAULTBG + "login.jpg");
            Constant.user = user;
            indexService.addUser(user);
            out.print("{\"msg\":\"true\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void outPrint(HttpServletResponse response, boolean bool) throws IOException {
        PrintWriter out = response.getWriter();
        if (!bool) {
            out.print("{\"msg\":\"true\"}");
        } else {
            out.print("{\"msg\":\"false\"}");
        }
    }
}
