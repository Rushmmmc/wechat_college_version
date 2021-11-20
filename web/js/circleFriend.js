// 获取弹窗
var modal = document.getElementById('myModal');
var modal2 = document.getElementById('myModal2');
// 打开弹窗的按钮对象
var btn = document.getElementById("myBtn");
var btn2 = document.getElementById("myBtn2");

// 获取 <span> 元素，用于关闭弹窗 that closes the modal
var span = document.getElementById("close");
var span2 = document.getElementById("close2");

// 点击按钮打开弹窗
btn.onclick = function () {
    modal.style.display = "block";
}
// 点击按钮打开弹窗
btn2.onclick = function () {
    modal2.style.display = "block";
}

// 点击 <span> (x), 关闭弹窗
span.onclick = function () {
    modal.style.display = "none";
}
span2.onclick = function () {
    modal2.style.display = "none";
}


// 在用户点击其他地方时，关闭弹窗
window.onclick = function (event) {
    if (event.target == modal || event.target == modal2) {
        modal.style.display = "none";
        modal2.style.display = "none";
    }
}


function issueCF() {
    var text = $("#issue").val();
    if (text == "") {
        alert("请不要不填写内容！");
        return false;
    } else {
        $.getJSON(
            "../CircleFriendServlet",
            {"operation": "issue", "type": "circle", "circleText": text, "photoUrl": null},
            function (result) {
                if (result.msg == "true") {
                    alert("发布成功！");
                    location.href = "/mywechat_war_exploded/CircleFriendServlet?operation=queryAll&pages=1&commentPage=1";
                } else {
                    alert("修改失败！");
                }
            }
        );
    }
}

function issueCM() {
    var text = $("#issue2").val();
    if (text == "") {
        alert("请不要不填写内容！");
        return false;
    } else {
        $.getJSON(
            "../CircleFriendServlet",
            {"operation": "issue", "type": "comment", "commentText": text, "circleId": circleId},
            function (result) {
                if (result.msg == "true") {
                    alert("发布成功！");
                    location.href = "/mywechat_war_exploded/CircleFriendServlet?operation=queryAll&pages=" + pages + "&commentPage=1";
                } else {
                    alert("修改失败！");
                }
            }
        );
    }
}

function like() {
    if (likes == 0) {
        $.getJSON(
            "../CircleFriendServlet",
            {"operation": "doLike"},
            function (result) {
                if (result.msg == "true") {
                    alert("点赞成功！");
                    location.href = "/mywechat_war_exploded/CircleFriendServlet?operation=queryAll&pages=" + pages + "&commentPage=1";
                } else {
                    alert("点赞失败！");
                }
            }
        );
    } else {
        $.getJSON(
            "../CircleFriendServlet",
            {"operation": "doLike"},
            function (result) {
                if (result.msg == "true") {
                    alert("取消成功！");
                    location.href = "/mywechat_war_exploded/CircleFriendServlet?operation=queryAll&pages=" + pages + "&commentPage=1";
                } else {
                    alert("取消失败！");
                }
            }
        );
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