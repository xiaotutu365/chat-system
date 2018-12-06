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
                console.log("开始建立连接...");
                var userId = App.getUserGlobalInfo();
                console.log("当前登陆用户Id为：" + userId);
                var chatMsg = new App.ChatMsgObj(userId, null, null, null);
                var dataContent = new App.DataContent(1, chatMsg, null);
                CHAT.chat(JSON.stringify(dataContent));
                console.log(dataContent);

                // ----------开启连接后，开始向服务端发送心跳包----------
                // 组装心跳包
                var heartChatMsg = new App.ChatMsgObj(null, null, null, null);
                var heartDataContent = new App.DataContent(App.KEEPALIVE, heartChatMsg, null);
                // 发送心跳包
                setInterval(function() {
                    CHAT.chat(JSON.stringify(heartDataContent));
                }, 3000);
            },
            wsclose: function () {
                console.log("连接关闭...");
            },
            wserror: function () {
                console.log("发生错误...");
            },
            wsmessage: function (e) {
                // TODO 增加拉取好友请求类型--实时拉取好友

                // 接收到的消息内容
                var msgContent = e.data;
                console.log("接收到的原始消息：" + msgContent);

                // 解析消息，将接收到的消息解析为JSON
                var charMsgObj = JSON.parse(msgContent);
                var msg = charMsgObj.msg;

                var content = document.getElementById("content");
                content.innerHTML += msg + "<br />";

                // TODO 保存聊天历史记录到本地--朋友发送来的消息
                App.saveUserChatHistory();
            }
        };

        /**
         * 发送消息
         */
        function sendMessage() {
            // 消息内容
            var messageContent = document.getElementById("message").value;
            // 组装消息对象
            var userId = App.getUserGlobalInfo();
            // 测试用，指定接收者Id为"yangyixin"
            var receiveUserId = "yangyixin";
            var chatMsg = new App.ChatMsgObj(userId, receiveUserId, messageContent, null);
            var dataContent = new App.DataContent(2, chatMsg, null);
            // 消息发送
            CHAT.socket.send(JSON.stringify(dataContent));

            // TODO 保存聊天历史记录到本地--我发送出去的消息
            App.saveUserChatHistory();
        }

        /**
         * 初始化连接
         */
        function initConnect() {
            App.setUserGlobalInfo(document.getElementById("userId").value);
            CHAT.init();
        }
    </script>
</head>
<body>
<div>
用户名：<input type="text" id="userId" />&nbsp;&nbsp;<input type="button" value="初始化连接" onclick="initConnect();" />
</div>
<br/>
<div>发送消息输入框：<input type="text" id="message"/>&nbsp;&nbsp;<input type="button" value="点击发送" onclick="sendMessage();"/>
</div>
<br/>
<div>
    接收消息框：
    <div id="content" style="background-color: gainsboro;">

    </div>
</div>
</body>
</html>