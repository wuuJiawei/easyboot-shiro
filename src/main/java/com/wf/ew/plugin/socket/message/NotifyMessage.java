package com.wf.ew.plugin.socket.message;

import com.wf.ew.plugin.socket.type.MessageType;
import com.wf.ew.plugin.socket.type.TargetType;

import java.util.List;

/**
 *  聊天消息
 */
public class NotifyMessage extends Notify {

    /**
     * 发送类型
     */
    private TargetType targetType;

    /**
     * 消息类型
     */
    private MessageType type;

    /**
     * 发送目标
     */
    private List<String> target;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 消息来源
     */
    private String from;

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public List<String> getTarget() {
        return target;
    }

    public void setTarget(List<String> target) {
        this.target = target;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
