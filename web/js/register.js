/**
 * 检查各项信息的完整性和正确性
 * 不足：本想用数组，结果一直不能替换数组元素，无奈用多个变量
 * @type {number}
 */
var idFlag = 0;
var pswFlag = 0;
var nickFlag = 0;
var mailFlag = 0;
var phoneFlag = 0;
var idCardFlag = 0;
var codeFlag = 0;

function checkId() {
    var span = document.getElementById("loginIdCheck");
    var $loginId = $("#logininput").val();
    if ($loginId == "") {
        idFlag = 0;
        span.innerHTML = "账户不能为空";
    } else if (!$loginId.match("^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}$")) {
        idFlag = 0;
        span.innerHTML = "看这里→";
    } else {
        span.innerHTML = "";
        $.getJSON(
            "RegisterServlet",
            {"loginId": $loginId, "operation": "checkId"},
            function (result) {
                if (result.msg == "true") {
                    idFlag = 1;
                    span.innerHTML = "";
                } else {
                    idFlag = 0;
                    span.innerHTML = "该账户已注册";
                }
            }
        );
    }

}

function register() {
    var $loginId = $("#logininput").val();
    var $password = $("#password").val();
    var $mailbox = $("#mailbox").val();
    var $nickName = $("#nickName").val();
    var $phone = $("#phone").val();
    var $idCard = $("#idCard").val();
    if (idFlag == 0 || pswFlag == 0 || nickFlag == 0 || mailFlag == 0 || phoneFlag == 0 || idCardFlag == 0 || codeFlag == 0) {
        alert("请检查输入的信息！");
        return false;
    }
    $.getJSON(
        "RegisterServlet",
        {
            "loginId": $loginId, "password": $password, "mailbox": $mailbox, "nickName": $nickName,
            "phone": $phone, "idCard": $idCard, "operation": "add"
        },
        function (result) {
            if (result.msg == "true") {
                alert("注册成功！");
                location.href = "login.jsp";
            } else {
                alert("注册失败！");

            }
        }
    );
    return true;
}

function change() {
    var img = document.getElementById("imgCode");
    img.src = "VerifyCodeServlet" + "?" + Math.random();
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

function checkNick() {
    var span = document.getElementById("nickNameCheck");
    var $nickName = $("#nickName").val();
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

function checkMail() {
    var span = document.getElementById("mailCheck");
    var $mailbox = $("#mailbox").val();
    if ($mailbox == "") {
        mailFlag = 0;
        span.innerHTML = "邮箱不能为空";
    } else if (!$mailbox.match("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$")) {
        mailFlag = 0;
        span.innerHTML = "请输入正确的邮箱";
    } else if ($mailbox.match("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$")) {
        $.getJSON(
            "RegisterServlet",
            {"mailbox": $mailbox, "operation": "checkMail"},
            function (result) {
                if (result.msg == "true") {
                    mailFlag = 1;
                    span.innerHTML = "";
                } else {
                    mailFlag = 0;
                    span.innerHTML = "该邮箱已注册";
                }
            }
        );
    }
}

function checkPhone() {
    var span = document.getElementById("phoneCheck");
    var $phone = $("#phone").val();
    if ($phone == "") {
        phoneFlag = 0;
        span.innerHTML = "手机不能为空";
    } else if (!$phone.match("^[1][3,4,5,7,8,9][0-9]{9}$")) {
        phoneFlag = 0;
        span.innerHTML = "请输入正确的手机号码";
    } else if ($phone.match("^[1][3,4,5,7,8,9][0-9]{9}$")) {
        $.getJSON(
            "RegisterServlet",
            {"phone": $phone, "operation": "checkPhone"},
            function (result) {
                if (result.msg == "true") {
                    phoneFlag = 1;
                    span.innerHTML = "";
                } else {
                    phoneFlag = 0;
                    span.innerHTML = "该手机已注册";
                }
            }
        );
    }
}

function checkIdCard() {

    var span = document.getElementById("idCardCheck");
    var $idCard = $("#idCard").val();
    if ($idCard == "") {
        idCardFlag = 0;
        span.innerHTML = "身份证不能为空";
    } else if (!$idCard.match("^\\d{8,18}|[0-9x]{8,18}|[0-9X]{8,18}?$")) {
        idCardFlag = 0;
        span.innerHTML = "请输入正确的身份证";
    } else if ($idCard.match("^\\d{8,18}|[0-9x]{8,18}|[0-9X]{8,18}?$")) {
        $.getJSON(
            "RegisterServlet",
            {"idCard": $idCard, "operation": "checkIdCard"},
            function (result) {
                if (result.msg == "true") {
                    idCardFlag = 1;
                    span.innerHTML = "";
                } else {
                    idCardFlag = 0;
                    span.innerHTML = "该身份证已注册";
                }
            }
        );
    }
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

