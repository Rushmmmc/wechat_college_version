package com.wechat.constant;

import com.wechat.po.User;

public class Constant {
    /**
     * 验证码的长度为4
     */
    public static final int CODESIZE = 4;
    /**
     * 检查用户loginId的操作名称
     */
    public static final String CHECKID = "checkId";
    /**
     * 检查用户密码password的操作名称
     */
    public static final String CHECKPSW = "checkPsw";
    /**
     * 检查用户邮箱mailbox的操作名称
     */
    public static final String CHECKMAIL = "checkMail";
    /**
     * 检查用户手机号码phone的操作名称
     */
    public static final String CHECKPHONE = "checkPhone";
    /**
     * 检查用户身份证idCard的操作名称
     */
    public static final String CHECKIDCARD = "checkIdCard";
    /**
     * 检查用户登录验证码的操作名称
     */
    public static final String CHECKCODE = "checkCode";
    /**
     * 增加（插入数据库数据）的操作名称
     */
    public static final String ADD = "add";
    /**
     * 用于更新，如个人资料
     */
    public static final String UPDATE = "update";
    /**
     * 用于修改密码
     */
    public static final String MODIFYPSW = "modifyPsw";
    /**
     * 用于修改用户聊天背景
     */
    public static final String MODIFYBG = "modifyBg";
    /**
     * 默认头像的路径
     */
    public static final String DEFAULTPOR = "../image/headPortrait/";
    /**
     * 默认背景的路径
     */
    public static final String DEFAULTBG = "../image/background/";
    /**
     * 用户的肖像
     */
    public static final String PORTRAIT = "portrait";
    /**
     * 用户的聊天背景
     */
    public static final String BACKGROUND = "background";
    /**
     * 图片的根路径
     */
    public static final String PATH = "E:\\study\\java\\java-code\\mywechat\\web\\image";
    /**
     * 展示图片的最大数
     */
    public static final Integer SIZES = 4;
    /**
     * 朋友圈评论的展示数量
     */
    public static final Integer FRIENDLIST = 4;
    /**
     * 用户查询操作
     */
    public static final String QUERYALL = "queryAll";
    /**
     * 发布朋友圈操作
     */
    public static final String ISSUE = "issue";
    /**
     * 发布朋友圈的标识
     */
    public static final String CIRCLE = "circle";
    /**
     * 进行点赞操作
     */
    public static final String DOLIKE = "doLike";
    /**
     * 通讯录展示好友的数量
     */
    public static final Integer FRIENDS = 11;
    /**
     * 查看好友信息操作
     */
    public static final String QUERY = "query";
    /**
     * 查询好友操作（查询后用于添加）
     */
    public static final String SEARCH = "search";
    /**
     * 确认添加好友操作
     */
    public static final String DOADD = "doAdd";
    /**
     * 确认添加好友操作
     */
    public static final String CONFIRMAdd = "confirm";
    /**
     * 查看添加请求操作
     */
    public static final String ADDREQUEST = "addRequest";
    /**
     * 通讯录右侧查看的显示数
     */
    public static final Integer ADDRESSSIZE = 9;
    /**
     * 删除操作
     */
    public static final String DELETE = "delete";
    /**
     * 单聊操作
     */
    public static final String CHATTYPEONLY = "chatOnly";

    /**
     * 群聊查询操作
     */
    public static String CHATMULTI = "chatMulti";
    /**
     * 群聊操作
     */
    public static String CHATGROUP = "chatGroup";
    /**
     * 群图标位置
     */
    public static String GROUPICON = "../image/icon/group.png";
    /**
     * 静态变量user
     */
    public static User user;
}
