/**
 * 检查用户信息填入的正确性和完整性
 * @type {number}
 */
var codeFlag = 0;
var pswFlag = 0;

function login() {
    var $loginId = $("#logininput").val();
    var $password = $("#password").val();
    if (codeFlag == 0) {
        alert("请输入正确的验证码！");
        return false;
    }
    if (pswFlag == 0) {
        alert("请输入正确的密码！");
        return false;
    }
    $.getJSON(
        "LoginServlet",
        {"loginId": $loginId, "password": $password, "operation": "checkPsw"},
        function (result) {
            if (result.msg == "true") {
                window.location = "/mywechat_war_exploded/jsp/welcome.jsp";
            } else {
                alert("账号或密码错误！");
            }
        }
    );
    return true;
}

function check() {
    var span = document.getElementById("inputCheck");
    var $loginId = $("#logininput").val();
    $.getJSON(
        "LoginServlet",
        {"loginId": $loginId, "operation": "checkId"},
        function (result) {
            if (result.msg == "true") {
                span.innerHTML = "";
            } else {
                span.innerHTML = "该账户未注册";
            }
        }
    );
}

function checkPsw() {
    var span = document.getElementById("pswCheck");
    var $password = $("#password").val();
    if ($password == "") {
        pswFlag = 0;
        span.innerHTML = "密码不能为空";
    } else if (!$password.match("^[0-9]{7,15}$")) {
        pswFlag = 0;
        span.innerHTML = "密码应为7-15位的数字组合";
    } else if ($password.match("^[0-9]{7,15}$")) {
        pswFlag = 1;
        span.innerHTML = "";
    }
}

function change() {
    var img = document.getElementById("imgCode");
    img.src = "VerifyCodeServlet" + "?" + Math.random();
}

function checkCode() {
    var span = document.getElementById("codeCheck");
    var $code = $("#code").val();
    $.getJSON(
        "LoginServlet",
        {"code": $code, "operation": "checkCode"},
        function (result) {
            if (result.msg == "true") {
                codeFlag = 1;
                span.innerHTML = "";
            } else {
                codeFlag = 0;
                span.innerHTML = "验证码输入错误";
            }
        }
    )
}

