package com.mywechat.model;

import java.util.List;

/**
 * 群组实现类
 */
public class Group {
    private int groupId;
    private int userId;
    private String userName;
    private String groupName;
    private String groupIcon;
    private String notice;
    private List<User> groupMember;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<User> getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(List<User> groupMember) {
        this.groupMember = groupMember;
    }
}
