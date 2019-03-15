package com.wf.ew.plugin.socket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.wf.ew.common.JsonResult;
import com.wf.ew.plugin.socket.message.NotifyMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Struct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket服务端
 * @author wujiawei0926@yeah.net
 */
@Component
@ServerEndpoint("/ws/{userId}")
public class WebSocketServer {

    Log log = LogFactory.get(WebSocketServer.class);
    // 记录当前在线连接数
    private static int onlineCount = 0;
    // 线程安全hashMap
    private static ConcurrentHashMap<String, WebSocketServer> webSocketList = new ConcurrentHashMap<>();
    // 与客户端的连接会话，通过它来给客户端发送数据
    private Session session;
    // 接收用户id
    private String userId;

    /**
     * 初始化一个系统消息
     */
    @PostConstruct
    public void init(){


    }

    /**
     * 连接建立成功
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId){
        this.session = session;
        webSocketList.put(userId, this);
        // 在线数+1
        addOnlineCount();
        log.info("新窗口开始监听：{}，当前在线人数：{}", userId, getOnlineCount());
        this.userId = userId;
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(){
        if(webSocketList.get(this.userId) != null){
            webSocketList.remove(this.userId);
            subOnlineCount();
            log.info("有一连接关闭，当前在线人数：{}", getOnlineCount());
        }
    }

    /**
     * 收到客户端消息
     * @param message 消息正文
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自客户端{}的消息：{}", message);

        if (StrUtil.isNotBlank(message)) {
            /*NotifyMessage fromMsg = JSONUtil.toBean(message, NotifyMessage.class);
            String fromUser = fromMsg.getFrom();
            String content = fromMsg.getMsg();
            if (StrUtil.isEmpty(content)) {
                return;
            }

            List<String> toUsers = fromMsg.getTarget();
            if (toUsers != null && toUsers.size() > 0) {
                for (String toUser : toUsers) {

                }
            }*/

            JSONArray list = JSONUtil.parseArray(message);
            for (int i = 0; i < list.size(); i++) {
                 try {
                     // 解析报文
                     JSONObject object = list.getJSONObject(i);
                     String toUser = object.getStr("toUser");
                     String content = object.getStr("content");
                     object.put("fromUserId", this.userId);

                     // 传送给对应用户的webSocket服务
                     if (StrUtil.isNotBlank(toUser) && StrUtil.isNotBlank(content)) {
                         WebSocketServer ss = webSocketList.get(toUser);
                         // 转换
                         if (ss != null) {
                             ss.sendMessage(JsonResult.ok(200, "连接成功"));
                             // 业务代码 TODO 消息存库

                         }
                     }


                 } catch (Exception e) {
                     e.printStackTrace();
                 }
            }
        }
    }

    /**
     * 发生错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }


    /**
     * 实现服务器主动推送
     */
    public void sendMessage(JsonResult result) throws IOException {
        String json = JSONUtil.toJsonStr(result);
        session.getBasicRemote().sendText(json);
    }


    /**
     * 群发自定义消息
     * */
    /*public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        log.info("推送消息到窗口"+userId+"，推送内容:"+message);
        for (ImController item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(userId==null) {
                    item.sendMessage(message);
                }else if(item.userId.equals(userId)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }*/

    /**
     * 在线数 + 1
     */
    private synchronized void addOnlineCount(){
        WebSocketServer.onlineCount++;
    }

    /**
     * 获取在线数
     * @return
     */
    private synchronized int getOnlineCount(){
        return onlineCount;
    }

    /**
     * 在线数 - 1
     */
    private synchronized void subOnlineCount(){
        WebSocketServer.onlineCount--;
    }



}
