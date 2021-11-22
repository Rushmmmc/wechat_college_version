package com.mywechat.controller;


import com.mywechat.model.Constant;
import com.mywechat.model.User;
import com.mywechat.service.IndexService;
import com.mywechat.service.impl.IndexImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 功能：登录
 */
@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");
        String loginId = request.getParameter("loginId");
        String password = request.getParameter("password");
        String code = request.getParameter("code");
        String operation = request.getParameter("operation");
        PrintWriter out = response.getWriter();
        IndexService indexService = new IndexImpl();
        /**
         * 判断账户密码是否正确
         * 正确：从数据库中获取该名用户的资料并存储在session中
         * 错误：无
         */
        if (Constant.CHECKID.equals(operation)) {
            if (indexService.queryMessage(loginId, operation)) {
                User user;
                user = indexService.getUserMsg(loginId);
                request.getSession().setAttribute("user", user);
                out.print("{\"msg\":\"true\"}");
            } else {
                out.print("{\"msg\":\"false\"}");
            }
        } else if (Constant.CHECKPSW.equals(operation)) {
            request.getSession().setAttribute("password", password);
            if (indexService.confirmPassword(loginId, password)) {
                request.getSession().setAttribute("loginId", loginId);
                out.print("{\"msg\":\"true\"}");
            } else {
                out.print("{\"msg\":\"false\"}");
            }

        } else if (Constant.CHECKCODE.equals(operation)) {
            /**
             * 检查验证码
             */
            if (code.equals(request.getSession().getAttribute("code"))) {
                out.print("{\"msg\":\"true\"}");
            } else {
                out.print("{\"msg\":\"false\"}");
            }
        }
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
