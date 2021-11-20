<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WeChat网页版</title>
    <link rel="stylesheet" type="text/css" href="../css/message.css"/>
</head>
<body>
<div style=" display: grid; width: 70px;  position: absolute;">
    <a href="chatGroup.jsp?type=0&groupId=0" class="back">返回主页</a>
    <script>
        if (${sessionScope.produceMsg == null}) {

        } else if (${sessionScope.produceMsg == true}) {
            alert("导出成功！生成文件在项目根目录的messages文件夹中！");
        } else if (${sessionScope.produceMsg == false}) {
            alert("导出失败！");
        }
        ${sessionScope.remove("produceMsg")};
    </script>
    <c:if test="${param.type == 1}">
        <a href="../ChatServlet?operation=delete&friendId=${param.friendId}" class="back">删除记录</a>
        <a href="../ChatServlet?operation=issue&friendId=${param.friendId}" class="back">导出记录</a>
    </c:if>
    <c:if test="${param.type == 2}">
        <a href="../ChatServlet?operation=delete&groupId=${param.groupId}" class="back">删除记录</a>
        <a href="../ChatServlet?operation=issue&groupId=${param.groupId}" class="back">导出记录</a>
    </c:if>


</div>
<div id="text">
    <c:if test="${param.type == 1}">
        <c:forEach items="${sessionScope.friendMsg}" var="chat">
            <div id="sendMsg">
                    ${chat.from}
                    ${chat.date}
            </div>
            <c:if test="${chat.fromId == sessionScope.user.userId}">
                <div class="right">
                    <p>${chat.sendMsg}</p>
                </div>
                <br/><br/>
            </c:if>
            <c:if test="${chat.fromId != sessionScope.user.userId}">
                <div class="left">
                    <p>${chat.sendMsg}</p>
                </div>
                <br/><br/>
            </c:if>
        </c:forEach>
    </c:if>
    <c:if test="${param.type == 2}">
        <c:forEach items="${sessionScope.groupMsg}" var="chat">
            <div id="sendMsg">
                    ${chat.from}
                    ${chat.date}
            </div>
            <c:if test="${chat.fromId == sessionScope.user.userId}">
                <div class="right">
                    <p>${chat.sendMsg}</p>
                </div>
                <br/><br/>
            </c:if>
            <c:if test="${chat.fromId != sessionScope.user.userId}">
                <div class="left">
                    <p>${chat.sendMsg}</p>
                </div>
                <br/><br/>
            </c:if>
        </c:forEach>
    </c:if>
</div>
</body>
</html>
