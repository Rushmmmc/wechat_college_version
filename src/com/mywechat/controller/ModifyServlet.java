package com.mywechat.controller;

import com.mywechat.model.Constant;
import com.mywechat.model.Photo;
import com.mywechat.model.User;
import com.mywechat.service.IndexService;
import com.mywechat.service.PersonService;
import com.mywechat.service.impl.IndexImpl;
import com.mywechat.service.impl.PersonImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * 用户进行修改操作的servlet
 * 修改密码。聊天背景，修改个人资料等
 */
@WebServlet(name = "ModifyServlet")
public class ModifyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IndexService indexService = new IndexImpl();
        PersonService personService = new PersonImpl();
        PrintWriter out = response.getWriter();
        String operation = request.getParameter("operation");
        String loginId = request.getParameter("loginId");
        if (operation.equals(Constant.UPDATE)) {
            /**
             * 更新用户的昵称和简介
             */
            String nickName = request.getParameter("nickName");
            String signature = request.getParameter("signature");
            User user = (User) request.getSession().getAttribute("user");
            user.setNickName(nickName);
            user.setSignature(signature);
            indexService.modifyInfo(nickName, loginId, signature);
            out.print("{\"msg\":\"true\"}");
        } else if (operation.equals(Constant.MODIFYPSW)) {
            /**
             * 更换密码
             */
            String password = request.getParameter("password");
            String newPassword = request.getParameter("newPassword");
            if (indexService.confirmPassword(loginId, password)) {
                request.getSession().setAttribute("password", newPassword);
                indexService.modifyPsw(loginId, newPassword);
                out.print("{\"msg\":\"true\"}");
            } else {
                out.print("{\"msg\":\"false\"}");
            }
        } else if (operation.equals(Constant.MODIFYBG)) {
            /**
             * 查看聊天背景
             */
            int pages = Integer.parseInt(request.getParameter("pages"));
            List<Photo> photos = personService.getPhotos(pages, Constant.SIZES);
            request.getSession().setAttribute("photoList", photos);
            int photoPages = personService.getPhotoSize();
            request.getSession().setAttribute("pages", pages);
            request.getSession().setAttribute("photoPages", photoPages);
            response.sendRedirect("jsp/personCenter.jsp?message=3");
        } else if (operation.equals(Constant.BACKGROUND)) {
            /**
             * 更换聊天背景
             */
            String photoName = request.getParameter("photoName");
            loginId = (String) request.getSession().getAttribute("loginId");
            personService.changeBg(loginId, photoName);
            User user = (User) request.getSession().getAttribute("user");
            user.setBackground(Constant.DEFAULTBG + photoName);
            response.sendRedirect("jsp/user.jsp?type=0&friendId=0");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
