package com.wf.ew.plugin.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * 系统日志
 * </p>
 *
 * @author 武佳伟
 * @since 2019-01-28
 */
@TableName("sys_log")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;


    /**
     * 生成时间
     */
    private LocalDateTime ctime;


    /**
     * 结束时间
     */
    private LocalDateTime endtime;


    /**
     * 执行时间(秒)
     */
    private Float exctime;


    /**
     * 级别
     */
    private String level;


    /**
     * 日志名
     */
    private String loggerName;


    /**
     * 文件名
     */
    private String fileName;


    /**
     * 主机
     */
    private String host;

    /**
     * 操作系统
     */
    private String osName;


    /**
     * 设备
     */
    private String device;


    /**
     * 浏览器
     */
    private String browserType;


    /**
     * 线程
     */
    private String thread;


    /**
     * 类名
     */
    private String clasz;


    /**
     * 方法名
     */
    private String method;


    /**
     * 参数
     */
    private String params;


    /**
     * 行号
     */
    private String lineNumber;


    /**
     * 消息
     */
    private String message;


    /**
     * 异常堆栈
     */
    private String throwables;


    /**
     * 请求地址
     */
    private String uri;

    @TableField(exist = false)
    private String username;  // 用户账号

    @TableField(exist = false)
    private String nickName;  // 用户昵称

    private Integer userId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }
    public LocalDateTime getCtime() {
        return ctime;
    }

    public void setCtime(LocalDateTime ctime) {
        this.ctime = ctime;
    }
    public LocalDateTime getEndtime() {
        return endtime;
    }

    public void setEndtime(LocalDateTime endtime) {
        this.endtime = endtime;
    }
    public Float getExctime() {
        return exctime;
    }

    public void setExctime(Float exctime) {
        this.exctime = exctime;
    }
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }
    public String getClasz() {
        return clasz;
    }

    public void setClasz(String clasz) {
        this.clasz = clasz;
    }
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getThrowables() {
        return throwables;
    }

    public void setThrowables(String throwables) {
        this.throwables = throwables;
    }
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Log{" +
        "logId=" + logId +
        ", ctime=" + ctime +
        ", endtime=" + endtime +
        ", exctime=" + exctime +
        ", level=" + level +
        ", loggerName=" + loggerName +
        ", fileName=" + fileName +
        ", host=" + host +
        ", thread=" + thread +
        ", clasz=" + clasz +
        ", method=" + method +
        ", params=" + params +
        ", lineNumber=" + lineNumber +
        ", message=" + message +
        ", throwables=" + throwables +
        ", uri=" + uri +
        ", userId=" + userId +
        "}";
    }
}
