window.onload = function () {
    var arr = document.getElementsByClassName('buttonShow');
    for (var i = 0; i < arr.length; i++) {
        arr[i].onclick = function () {
            $.getJSON(
                "../AddressBookServlet",
                {"operation": "query", "userId": this.id},
                function () {
                    location.href = "../jsp/addressBook.jsp?message=1";
                }
            )
        }
    }
    var arr2 = document.getElementsByClassName("aAdd");
    for (var i = 0; i < arr2.length; i++) {
        arr2[i].onclick = function (ev) {
            $.getJSON(
                "../AddressBookServlet",
                {"operation": "doAdd", "friendId": this.name},
                function (result) {
                    if (result.msg == "true") {
                        alert("成功发送请求！");
                    } else {
                        alert("已请求过一次，请不要多次请求！");
                    }
                }
            )
        }
    }
}

function searchFriend() {
    var msg = $("#likeSearch").val();
    $.getJSON(
        "../AddressBookServlet",
        {"operation": "search", "likeLoginId": msg, "nickName": msg, "page": 1},
        function () {
            location.href = "../jsp/addressBook.jsp?message=2";
        }
    )
}


function addSearch() {
    $.getJSON(
        "../AddressBookServlet",
        {"operation": "addRequest", "pages": 1},
        function () {
            location.href = "../jsp/addressBook.jsp?message=3";
        }
    )
}

function agree() {
    var arr = document.getElementsByClassName("agree").item(0);
    $.getJSON(
        "../AddressBookServlet",
        {"operation": "confirm", "option": "add", "friendId": arr.id},
        function (result) {
            if (result.msg == "true") {
                alert("添加成功！");
                location.href = "../jsp/addressBook.jsp?message=3";
            } else {
                alert("添加失败！");
            }
        }
    )
}

function refuse() {
    var arr = document.getElementsByClassName("agree").item(1);
    $.getJSON(
        "../AddressBookServlet",
        {"operation": "confirm", "option": "refuse", "friendId": arr.id},
        function () {
            location.href = "../jsp/addressBook.jsp?message=3";
        }
    )
}

function changeUnAddPage1() {
    var msg = $("#likeSearch").val();
    $.getJSON(
        "../AddressBookServlet",
        {"operation": "search", "likeLoginId": msg, "nickName": msg, "page": unAddPage - 1},
        function () {
            location.href = "../jsp/addressBook.jsp?message=2";
        }
    )
}

function changeUnAddPage2() {
    var msg = $("#likeSearch").val();
    $.getJSON(
        "../AddressBookServlet",
        {"operation": "search", "likeLoginId": msg, "nickName": msg, "page": unAddPage + 1},
        function () {
            location.href = "../jsp/addressBook.jsp?message=2";
        }
    )
}

function changeRequest1() {
    $.getJSON(
        "../AddressBookServlet",
        {"operation": "addRequest", "pages": requestPage - 1},
        function () {
            location.href = "../jsp/addressBook.jsp?message=3";
        }
    )
}


function changeRequest2() {
    $.getJSON(
        "../AddressBookServlet",
        {"operation": "addRequest", "pages": requestPage + 1},
        function () {
            location.href = "../jsp/addressBook.jsp?message=3";
        }
    )
}

function chatBtn() {
    var friend = document.getElementsByClassName("chatBtn").item(0);
    $.getJSON(
        "../ChatServlet",
        {"operation": "chatOnly", "friendId": friend.id},
        function (result) {

        }
    )
    location.href = "../jsp/user.jsp?type=2&friendId=" + friend.id;
}

function chatA() {
    var friendId = document.getElementsByClassName("chatA").item(0);
    if (confirm("确定删除这位好友吗")) {
        $.getJSON(
            "../AddressBookServlet",
            {"operation": "delete", "friendId": friendId.id},
            function (result) {
                if (result.msg == "true") {
                    alert("删除成功！");
                    location.href = "../AddressBookServlet?operation=queryAll&pages=1"
                } else {
                    alert("删除失败！");
                }
            }
        )
    } else {
        return false;
    }
}