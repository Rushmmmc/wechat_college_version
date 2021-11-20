package com.wechat.po;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 功能：朋友圈的实现类
 */
public class CircleFriends {
    /**
     * 朋友圈在数据库中的识别数
     */
    private int circleId;
    /**
     * 朋友圈的发送者
     */
    private int sendUser;
    /**
     * 发送者的名称
     */
    private String sendUserName;
    /**
     * 朋友圈的内容存放
     */
    private String text;
    /**
     * 朋友圈的图片存放
     */
    private String photoUrl;
    /**
     * 朋友圈的发送时间
     */
    private String sendTime;
    /**
     * 朋友圈的点赞数
     */
    private int like;
    /**
     * 评论
     */
    private List<Comment> comments;

    public int getCircleId() {
        return circleId;
    }

    public void setCircleId(int circleId) {
        this.circleId = circleId;
    }

    public int getSendUser() {
        return sendUser;
    }

    public void setSendUser(int sendUser) {
        this.sendUser = sendUser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime() {
        this.sendTime = getCurrentTime();
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    /**
     * 获取当前时间。
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String currentTime = dateFormat.format(date);
        return currentTime;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }
}
