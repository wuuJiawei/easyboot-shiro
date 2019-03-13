package com.wf.ew.common.layui;



import java.io.Serializable;
import java.util.List;

/**
 * \*
 * \* @name: NavBar
 * \* @author: 武佳伟丶
 * \* @date: 2018/4/8 0008
 * \* @time: 22:46
 * \* @description: To change this template use File | Settings | File Templates.
 * \
 */
public class NavBar implements Serializable {

    private String id;
    private String title;
    private String icon;

    /**
     * 是否展开
     */
    private boolean spread;

    private String url;
    private List<NavBar> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isSpread() {
        return spread;
    }

    public void setSpread(boolean spread) {
        this.spread = spread;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<NavBar> getChildren() {
        return children;
    }

    public void setChildren(List<NavBar> children) {
        this.children = children;
    }
}