package com.wf.ew.common.layui;

import cn.hutool.core.util.StrUtil;

import java.io.Serializable;

/**
 * LayTableArg
 *
 * @name: LayTableArg
 * @author: 12093
 * @date: 2019/1/9
 * @time: 14:16
 */
public class LayTableArg implements Serializable {

    private Integer limit;
    private Integer page;
    private String param;
    private String field;
    private String type;
    private boolean isAsc;

    public Integer getLimit() {
        return limit == null ? 12 : limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page == null ? 1 : page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAsc() {
        return StrUtil.equals(type, "asc");
    }

    public void setAsc(boolean asc) {
        isAsc = asc;
    }
}