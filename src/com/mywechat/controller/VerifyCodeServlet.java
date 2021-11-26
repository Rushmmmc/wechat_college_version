package com.mywechat.controller;

import com.mywechat.util.VerifyCode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码实现类
 */
@WebServlet(name = "VerifyCodeServlet")
public class VerifyCodeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerifyCode verifyCode = new VerifyCode();
        BufferedImage bufferedImage = verifyCode.getImage();
        request.getSession().setAttribute("code", verifyCode.getText());
        VerifyCode.output(bufferedImage, response.getOutputStream());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
