package com.mywechat.controller;

import com.mywechat.model.Constant;
import com.mywechat.model.User;
import com.mywechat.service.IndexService;
import com.mywechat.service.impl.IndexImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * 上传图片类
 */
@WebServlet(name = "PhotoServlet")
public class PhotoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IndexService indexService = new IndexImpl();
        String operation = request.getParameter("operation");
        User user = (User) request.getSession().getAttribute("user");
        boolean nullFile = false;
        String path = Constant.PATH;
        if (operation.equals(Constant.PORTRAIT)) {
            path += "\\headPortrait";
        } else {
            path += "\\background";
        }
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");
        // 上传
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setSizeMax(2 * 1024 * 1024);
                List<FileItem> items = upload.parseRequest(request);
                Iterator<FileItem> iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = iter.next();
                    String fileName = item.getName();
                    if (fileName.equals("") || !(fileName.endsWith(".jpg") || fileName.endsWith(".png"))) {
                        nullFile = true;
                        continue;
                    }
                    if (operation.equals(Constant.PORTRAIT)) {
                        user.setPortraitName(fileName);
                        user.setHeadPortrait(Constant.DEFAULTPOR + fileName);
                        indexService.changePortrait(user.getLoginId(), user.getHeadPortrait());
                    } else {
                        user.setBgName(fileName);
                        user.setBgName(Constant.DEFAULTBG + fileName);
                    }
                    File file = new File(path, fileName);
                    item.write(file);
                }
            }
        } catch (FileUploadBase.SizeLimitExceededException e) {
            System.out.println("上传文件大小超过限制！最大2MB");
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (nullFile) {
        } else {
            if (operation.equals(Constant.PORTRAIT)) {
                response.sendRedirect("/mywechat_war_exploded/jsp/personCenter.jsp?message=2");
            } else {
                response.sendRedirect("/mywechat_war_exploded/jsp/personCenter.jsp?message=3");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
