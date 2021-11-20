<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>-WeChat网页版-</title>
    <script type="text/javascript" src="../js/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="../js/user.js"></script>
    <link rel="stylesheet" type="text/css" href="../css/user.css"/>
    <script type="text/javascript">
        var ws;
        var url = "ws://localhost:8080/mywechat_war_exploded/chatSocket?username=${sessionScope.user.loginId}?friendId=${param.friendId}";

        function logout() {
            if (confirm("确认注销吗？")) {
                return true;
            }
            return false;
        }

        var type = ${param.type};
        var toId = ${param.friendId};
        var fromId = ${sessionScope.user.userId};
        var fromLoginId = "${sessionScope.user.loginId}";
        if (type == 2) {
            connect();
        }
    </script>
</head>
<body style="background: url(../image/background/login.jpg); background-size: cover">
<ul>
    <li>
        <img src="${sessionScope.user.headPortrait}"
             style="width: 60px;height: 60px;position: absolute;top: 3px;left: 31px;border-radius: 45px;"
             title="这是你的头像">
        <span style="position: absolute;top: 22px;left: 102px;">${sessionScope.user.nickName}</span>
    </li>
    <div style="position: relative;left: 36%;">
        <li><a class="active" href="#home">聊天</a></li>
        <li><a href="../AddressBookServlet?operation=queryAll&pages=1">通讯录</a></li>
        <li><a href="../CircleFriendServlet?operation=queryAll&pages=1&commentPage=1">朋友圈</a></li>
        <li>
            <div class="dropdown">
                <button class="dropbtn">个人中心</button>
                <div class="dropdown-content">
                    <a href="personCenter.jsp?message=1">账户与安全</a>
                    <a href="personCenter.jsp?message=2">修改资料</a>
                    <a href="../ModifyServlet?operation=modifyBg&pages=1">聊天设置</a>
                </div>
            </div>
        </li>
        <li>
            <a href="../login.jsp" onclick="return logout();">注销</a>
        </li>
        <li>
            <a href="feedback.jsp">反馈</a>
        </li>
    </div>
</ul>
<div id="main">
    <div id="header">${sessionScope.friendNickName}
        <a href="../GroupServlet?operation=query&receiverId=${param.friendId}"
           style="text-decoration: none;font-size: 13px;position: absolute;left: 475px; top: 40px; color: unset;">聊天记录</a>
    </div>
    <div id="text" style="background: url('${sessionScope.user.background}'); background-size: cover">
        <c:if test="${param.type!=0}">
            <c:forEach items="${sessionScope.chatUnAccept}" var="chat">
                <div id="sendMsg">
                        ${chat.from}
                        ${chat.date}
                </div>
                <div class="left">
                    <p>${chat.sendMsg}</p>
                </div>
                <br/><br/>
            </c:forEach>
        </c:if>
    </div>
    <div id="userList"></div>
    <div id="send">
        <textarea id="sendText" placeholder="输入你想发送的内容" maxlength="200" onblur="AntiSqlValid(this)"></textarea>
        <button id="sendBtn" onclick="send()">发送</button>
    </div>
    <div class="chat">
        <div style=" width: 291px;position: absolute;top: 43px;">
            <a href="###" class="header" style="border-bottom-style: solid">最近聊天(仅私聊)</a>
            <a href="../ChatServlet?operation=chatMulti" class="header">群聊</a>
            <div>
                <button>
                    <a href="../traverse.jsp">
                        <img src="../image/headPortrait/聊天选中.png"
                             style="width: 30px;max-width: 30px;height: 30px;max-height: 30px;position: relative;left: -15%;top: 55%;background: white;border-radius: 5px;">
                        <span style="color: white;position: relative;left: -22px;top: -10px;">聊天总群</span>
                    </a>
                </button>
            </div>
            <c:forEach items="${sessionScope.chatList}" var="chat">
                <div class="userList">
                    <button onclick="location='../ChatServlet?operation=chatOnly&friendId=${chat.userId}'">
                        <img src="${chat.headPortrait}" class="img">
                        <img src="../image/icon/notice.png" id="${chat.userId}" class="spanBtn"/>
                        <span class="span">${chat.nickName}</span>
                    </button>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>