window.onload = function (ev) {
    if (type == 0 || toId == 0) {
        var span = document.getElementById("header");
        span.innerHTML = "没有选中任何聊天";
    }
}

function connect() {
    if ('WebSocket' in window) {
        ws = new WebSocket(url);
    } else if ('MozWebSocket' in window) {
        ws = new MozWebSocket(url);
    } else {
        alert('WebSocket is not supported by this browser.');
        return;
    }
    ws.onmessage = function (event) {
        eval("var result=" + event.data);
        if (result.alert != undefined) {
            $("#text").append("<div id='alert'>" + result.alert + "</div>" + "<br/>");
        }

        if (result.names != undefined) {
            $("#userList").html("");
            $(result.names).each(function () {
                $("#userList").append("<input type=button class='userList' value='" + this + "'/><br/>");
            });
        }

        if (result.from != undefined) {
            if (result.from == fromLoginId) {
                $("#text").append("<div id='sendMsg'>" + result.date + " " + result.from + "</div>");
                $("#text").append("<div class='right'>" + "<p>" + result.sendMsg + "</p>" + "</div>" + "<br/>" + "<br/>" + "<p style='height: 16px';></p>");
            } else {
                $("#text").append("<div id='sendMsg'>" + result.from + " " + result.date + "</div>");
                $("#text").append("<div class='left'>" + "<p>" + result.sendMsg + "</p>" + "</div>" + "<br/>" + "<br/>");
            }
        }
        if (result.toId == fromId && result.status == 0) {
            var span = document.getElementById(result.fromId);
            span.style.display = "block";
        }
    };
}

function send() {
    var value = $("#sendText").val();
    var obj = null;
    if (value == "") {
        alert("请输入信息！");
        return false;
    }
    obj = {
        sendMsg: value,
        fromId: fromId,
        type: 2,
        toId: toId
    }
    var str = JSON.stringify(obj);
    ws.send(str);
    $("#sendText").val("");
}

function AntiSqlValid(oField) {
    re = /select|update|delete|exec|count|'|"|=|;|>|<|%/i;
    if (re.test(oField.value)) {
        alert("请您不要在参数中输入特殊字符和SQL关键字！"); //注意中文乱码
        oField.value = "";
        oField.className = "errInfo";
        oField.focus();
        return false;
    }
}