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
    <script src="js/app.js" type="application/javascript"></script>
    <script type="application/javascript">

        var chatMsgObj = {
            senderId: "",
            receiverId: "",
            msg: "",
            msgId: ""
        };

        var dataContent = {
            action: 1,
            chatMsgObj: chatMsgObj
        };

        // 定义一个对象
        window.CHAT = {
            // 若干变量与方法
            socket: null,
            init: function () {
                if (window.WebSocket) {
                    // 判断当前socket是否已经连接，如果已经连接，则不需要重复初始化
                    if (CHAT.socket != null && CHAT.socket !== undefined && CHAT.socket.readyState === WebSocket.OPEN) {
                        return false;
                    }

                    CHAT.socket = new WebSocket(App.nettyWebSocketUrl);

                    CHAT.socket.onopen = CHAT.wsopen,
                    CHAT.socket.onclose = CHAT.wsclose,
                    CHAT.socket.onerror = CHAT.wserror,
                    CHAT.socket.onmessage = CHAT.wsmessage;
                } else {
                    alert("手机设备过久，请升级手机设备...");
                }
            },
            chat: function (msg) {
                // 如果当前WebSocket的状态是已打开，则直接发送，否则重连
                if (CHAT.socket != null && CHAT.socket !== undefined && CHAT.socket.readyState === WebSocket.OPEN) {
                    CHAT.socket.send(msg);
                } else {
                    // 重连WebSocket
                    CHAT.init();
                    setTimeout("CHAT.reChat('" + msg + "')", "1000");
                }
            },
            reChat: function (msg) {
                console.log("消息重新发送...");
                CHAT.socket.send(msg);
            },
            wsopen: function () {
                console.log("建立连接...");
            },
            wsclose: function () {
                console.log("连接关闭...");
            },
            wserror: function () {
                console.log("发生错误...");
            },
            wsmessage: function (e) {
                var content = document.getElementById("content");
                var contentAppend = content.innerHTML;
                content.innerHTML = contentAppend + e.data + "<br />";
                console.log(e);
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
<input type="text" id="message"/>
<input type="button" value="点击发送" onclick="sendMessage();"/>
<br/>
<br/>
接收消息框：
<div id="content" style="background-color: gainsboro;">

</div>
</body>
</html>