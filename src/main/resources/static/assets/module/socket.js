/**
 * Created by Administrator on 2019/3/14.
 */
layui.define(['layer'], function (exports) {
    "use strict";

    var Socket = function () {
        this.v = '1.0';
    }, $ = layui.jquery,
        layer = layui.layer;

    /**
     * 初始化
     * @param options
     *          | url: 地址
     *          | user: 登录用户
     *          | onOpen: function() 连接成功
     *          | onError: function() 出现错误
     *          | onClose: function() 关闭
     *          | onMessage: function() 收到消息
     */
    Socket.prototype.init = function (options) {
        var opts = options,
            socket,
            socketInstance = new Socket();

        // 检查浏览器兼容性
        if(typeof(WebSocket) == "undefined") {
            console.log("您的浏览器不支持WebSocket");
            return;
        }

        // 初始化
        var socketUrl = opts.url + "/" + opts.user;
        socketUrl = socketUrl.replace("https", "ws").replace("http", "ws");
        socket = new WebSocket(socketUrl);
        //打开
        socket.onopen = function() {
            opts.onOpen();
        };
        //获得消息
        socket.onmessage = function(res) {
            var data = JSON.parse(res.data);
            if (data.code === 200) {
                opts.onMessage(data.data);
            }
        };
        //关闭事件
        socket.onclose = function() {
            opts.onClose();
        };
        //发生了错误事件
        socket.onerror = function() {
            opts.onError();
        };
        socketInstance.WebSocket = socket;

        // 发送消息
        socketInstance.send = function (data) {
            try {
                socket.send(JSON.stringify(data));
            } catch (e) {
                setTimeout(function () {
                    socket.send(JSON.stringify(data));
                }, 600);
            }
        };
        
        return socketInstance;
    };

    exports("socket", new Socket());
});