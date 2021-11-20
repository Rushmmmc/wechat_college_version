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

    var obj;
    if (value == "") {
        alert("请输入信息！");
        return false;
    }
    obj = {
        sendMsg: value,
        fromId: fromId,
        type: toId,
        toId: toId
    }
    var str = JSON.stringify(obj);
    ws.send(str);
    $("#sendText").val("");
}

function logout() {
    if (confirm("确认注销吗？")) {
        return true;
    }
    return false;
}

function deleteMem() {
    if (confirm("确定将其移出群聊吗？")) {
        return true;
    } else {
        return false;
    }
}

function out() {
    if (confirm("确定退出群聊吗？")) {
        return true;
    } else {
        return false;
    }
}

function showAdd() {
    $("#addMem").show();
}

function add() {
    var id = $("#addMem :checked");
    var ids = id[0].value;
    for (var i = 1; i < id.size(); i++) {
        ids += "and";
        ids += id[i].value;
    }
    if (id.size() == 0) {
        alert("请选择至少一位好友！");
    } else {
        location.href = "../GroupServlet?operation=add&groupId=" + toId + "&users=" + ids;
    }
}

function showAddGroup() {
    $("#addGroup").show();
}

function addGroup() {
    var id = $("#addGroup :checked");
    var name = $("#groupName").val();
    if (id == undefined) {
        alert(123);
    }
    var ids = id[0].value;
    for (var i = 1; i < id.size(); i++) {
        ids += "and";
        ids += id[i].value;
    }
    if (id.size() < 3) {
        alert("请至少选择三位好友！");
    } else if (name == "") {
        alert("请输入群名！");
    } else {
        location.href = "../GroupServlet?operation=doAdd&users=" + ids + "&groupName=" + name;
    }
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