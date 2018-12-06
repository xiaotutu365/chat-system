window.App = {
    nettyWebSocketUrl: "ws://172.16.0.112:9999/ws",
    ChatMsgObj: function (senderId, receiverId, msg, msgId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.msg = msg;
        this.msgId = msgId;
    },
    DataContent: function (action, chatMsgObj, extend) {
        this.action = action;
        this.chatMsgObj = chatMsgObj;
        this.extend = extend;
    },
    setUserGlobalInfo: function (userId) {
        localStorage.setItem("userId", userId);
    },
    getUserGlobalInfo: function () {
        return localStorage.getItem("userId");
    },
    CONNECT: 1,
    CHAT: 2,
    SIGNED: 3,
    KEEPALIVE: 4,
    PULL_FRIEND: 5,
    /**
     * 保存用户的聊天记录
     * @param myId
     * @param friendId
     * @param msg
     * @param flag 判断本条消息是我发送的,还是朋友发送的,1:我, 2:朋友
     */
    saveUserChatHistory: function(myId, friendId, msg, flag) {
        var chatKey = "chat-" + myId + "-" + friendId;
        // 从本地缓存获取聊天记录是否存在
        var chatHistoryListStr = localStorage.getItem(chatKey);
        // if() {
        //
        // }
    },
    /**
     * 获取用户的聊天记录
     * @param myId
     * @param friendId
     */
    getUserChatHistory: function(myId, friendId) {

    },
    ChatHistory: function (myId, friendId, msg, flag) {
        this.myId = myId;
        this.friendId = friendId;
        this.msg = msg;
        this.flag = flag;
    }
};