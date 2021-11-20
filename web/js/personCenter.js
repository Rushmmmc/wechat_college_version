/**
 * 检查密码输入的完整性，正确性
 * @type {number}
 */
var oldPswFlag = 0;
var newPswFlag = 0;
var nickFlag = 0;
/**
 * 账号需多次用到，所以做全局变量
 * @type {*|jQuery|*|*}
 */
var $loginId = $("#loginId").val();

function checkOld() {
    var span = document.getElementById("oldPsw");
    var $password = $("#oldInput").val();
    if ($password == oldPsw) {
        oldPswFlag = 1;
        span.innerHTML = "";
    } else {
        oldPswFlag = 0;
        span.innerHTML = "密码与原密码不匹配";
    }
}

function checkPsw() {
    var $newPsw1 = $("#newPsw1").val();
    var $newPsw2 = $("#newPsw2").val();
    var span = document.getElementById("pswSpan");
    if ($newPsw1 == "" || $newPsw2 == "") {
        newPswFlag = 0;
        span.innerHTML = "密码不能为空";
    } else if (!$newPsw2.match("^[0-9]{7,15}$")) {
        newPswFlag = 0;
        span.innerHTML = "请输入正确的密码格式:7-15的纯数字";
    } else if ($newPsw2 != $newPsw1) {
        newPswFlag = 0;
        span.innerHTML = "两次输入的密码不同";
    } else if ($newPsw2.match("^[0-9]{7,15}$") && $newPsw1 == $newPsw2) {
        newPswFlag = 1;
        span.innerHTML = "";
    }
}

function submitMsg() {
    var $password = $("#oldInput").val();
    var $newPassword = $("#newPsw2").val();
    if (oldPswFlag == 0) {
        alert("请输入正确的原密码！");
        return false;
    }
    if (newPswFlag == 0) {
        alert("请检查输入的新密码！");
        return false;
    }
    $.getJSON(
        "../ModifyServlet",
        {"loginId": $loginId, "password": $password, "newPassword": $newPassword, "operation": "modifyPsw"},
        function (result) {
            if (result.msg == "true") {
                alert("修改成功！请重新登录！");
                location.href = "../login.jsp";
            } else {
                alert("修改失败！");
            }
        }
    );
    return true;
}

function checkNick() {
    var span = document.getElementById("modifySpan");
    var $nickName = $("#nickInput").val();
    if ($nickName == "") {
        nickFlag = 0;
        span.innerHTML = "昵称不能为空";
    } else if (!$nickName.match("^[-_a-zA-Z0-9]{4,16}$")) {
        nickFlag = 0;
        span.innerHTML = "账户名应该在4-16个字符之间，包括下划线，减号";
    } else if ($nickName.match("^[-_a-zA-Z0-9]{4,16}$")) {
        nickFlag = 1;
        span.innerHTML = "";
    }
}

function updateMsg() {
    var $nickInput = $("#nickInput").val();
    var $signature = $("#signature").val();
    if (nickFlag == 0) {
        alert("请检查输入的新用户名!");
        return false;
    }
    $.getJSON(
        "../ModifyServlet",
        {"loginId": $loginId, "nickName": $nickInput, "signature": $signature, "operation": "update"},
        function (result) {
            if (result.msg == "true") {
                alert("修改成功！");
                location.href = "/mywechat_war_exploded/jsp/personCenter.jsp?message=2";
            } else {
                alert("修改失败！");
            }
        }
    );
}

function checkImg() {
    var fileInput = document.getElementById("test-image-file");
    if (!fileInput.value) {
        alert("没有选择文件！");
        return false;
    }
    if (fileInput.files[0].size > 2 * 1024 * 1024) {
        alert("文件大于2M！");
        return false;
    }
    if (fileInput.files[0].type !== 'image/jpeg' && fileInput.files[0].type !== 'image/jpg' && fileInput.files[0].type !== 'image/gif' && fileInput.files[0].type !== 'image/png') {
        alert("不是有效的图片格式！");
        return false;
    }
    if (fileInput.value && fileInput.files[0].size <= 2 * 1024 * 1024) {
        return true;
    }
}

function doImg() {
    var span = document.getElementById("uploadSpan");
    span.innerHTML = "请点击更换头像完成最后操作";
}

function changeBg() {
    if (confirm("确定将这张图片设为背景吗？")) {
        return true;
    } else {
        return false;
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

