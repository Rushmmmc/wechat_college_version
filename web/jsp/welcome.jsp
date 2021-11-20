<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>-ERROR-</title>
    <style>
        #a1:hover {
            border-bottom-style: solid;
            border-bottom-color: snow;
        }

        #skill {
            list-style: none;
            font: 12px "Helvetica Neue", Arial, Helvetica, Geneva, sans-serif;
            width: 296px;
            margin: 50px auto 0;
            position: relative;
            line-height: 2em;
            padding: 30px 0;
            top: 197px;
        }

        #skill li {
            margin-bottom: 50px;
            background: #e9e5e2;
            background-image: -webkit-gradient(linear, left top, left bottom, from(#e1ddd9), to(#e9e5e2));
            background-image: -webkit-linear-gradient(top, #e1ddd9, #e9e5e2);
            background-image: -moz-linear-gradient(top, #e1ddd9, #e9e5e2);
            background-image: -ms-linear-gradient(top, #e1ddd9, #e9e5e2);
            background-image: -o-linear-gradient(top, #e1ddd9, #e9e5e2);
            background-image: linear-gradient(top, #e1ddd9, #e9e5e2);
            height: 20px;
            border-radius: 10px;
            -moz-box-shadow: 0 1px 0px #bebbb9 inset, 0 1px 0 #fcfcfc;
            -webkit-box-shadow: 0 1px 0px #bebbb9 inset, 0 1px 0 #fcfcfc;
            box-shadow: 0 1px 0px #bebbb9 inset, 0 1px 0 #fcfcfc;
        }

        #skill li h3 {
            position: relative;
            top: 23px;
            left: 6px;
            color: white;
        }

        .bar {
            height: 18px;
            margin: 1px 2px;
            position: absolute;
            border-radius: 10px;
            -moz-box-shadow: 0 1px 0px #fcfcfc inset, 0 1px 0 #bebbb9;
            -webkit-box-shadow: 0 1px 0px #fcfcfc inset, 0 1px 0 #bebbb9;
            box-shadow: 0 1px 0px #fcfcfc inset, 0 1px 0 #bebbb9;
        }

        .graphic-design {
            width: 100%;
            -moz-animation: graphic-design 4s ease-out;
            -webkit-animation: graphic-design 4s ease-out;
            background-color: #03A9F4;
            background-image: -webkit-gradient(linear, left top, left bottom, from(#03A9F4), to(#00BCD4));
            background-image: -webkit-linear-gradient(top, #03A9F4, #00BCD4);
            background-image: -moz-linear-gradient(top, #03A9F4, #00BCD4);
            background-image: -ms-linear-gradient(top, #03A9F4, #00BCD4);
            background-image: -o-linear-gradient(top, #03A9F4, #00BCD4);
            background-image: linear-gradient(top, #03A9F4, #00BCD4);

        }

        @-moz-keyframes graphic-design {
            0% {
                width: 0px;
            }
            100% {
                width: 100%;
            }
        }

        @-webkit-keyframes graphic-design {
            0% {
                width: 0px;
            }
            100% {
                width: 100%;
            }
        }
    </style>
    <script type="text/javascript">
        function countDown(secs, surl) {
            if (--secs > 0) {
                setTimeout("countDown(" + secs + ",'" + surl + "')", 1000);
            } else {
                location.href = surl;
            }
        }
    </script>
</head>
<br>
<body>
<body style="background:url(../image/background/login.jpg) ;background-size:cover; "></body>
<div id="div1">
    <h1 style="position: absolute;top: 31%;left: 37%;color: white;"><br>欢迎用户${sessionScope.user.nickName}使用微信</h1>
</div>
<ul id="skill">
    <li><span class="bar graphic-design"></span>
        <h3>Login...Wechat网页版</h3></li>
</ul>
<script type="text/javascript">
    countDown(5, "/mywechat_war_exploded/ChatServlet?operation=query");
</script>
</body>
</html>
