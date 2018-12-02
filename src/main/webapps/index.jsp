<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/2
  Time: 10:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>websocket聊天室</title>
    <script type="application/javascript">
        // 定义一个对象
        window.CHAT = {
            // 若干变量与方法
            socket: null,
            init: function() {
                if(window.WebSocket) {
                    CHAT.socket = new WebSocket("ws://192.168.1.101:9999/ws");

                    CHAT.socket.onopen = function () {
                        console.log("建立连接成功...");
                    },

                    CHAT.socket.onmessage = function(e) {
                        var content = document.getElementById("content");
                        var contentAppend = content.innerHTML;
                        content.innerHTML = contentAppend + e.data  + "<br />";
                        console.log(e);
                    },
                    CHAT.socket.onclose = function() {
                        console.log("连接关闭...");
                    },
                    CHAT.socket.onerror = function() {
                        console.log("发生错误...");
                    }
                } else {
                    alert("您使用的浏览器不支持WebSocket");
                }
            }
        };

        CHAT.init();

        function sendMessage() {
            var message = document.getElementById("message");
            CHAT.socket.send(message.value);
        }
    </script>
</head>
<body>
    <div>发送消息输入框：</div>
    <input type="text" id="message" />
    <input type="button" value="点击发送" onclick="sendMessage();" />
    <br />
    <br />
    接收消息框：
    <div id="content" style="background-color: gainsboro;">

    </div>
</body>
</html>