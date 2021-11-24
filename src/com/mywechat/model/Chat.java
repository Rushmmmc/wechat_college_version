package com.mywechat.model;

public class Chat {
    private Integer chatId;
    /**
     * 聊天类型，群聊还是单聊
     */
    private int type;
    /**
     * 发送者
     */
    private int sendUser;
    /**
     * 接收者
     */
    private int receiver;
    /**
     * 获取接受者的名字
     */
    private String receiverName;

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSendUser() {
        return sendUser;
    }

    public void setSendUser(int sendUser) {
        this.sendUser = sendUser;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
