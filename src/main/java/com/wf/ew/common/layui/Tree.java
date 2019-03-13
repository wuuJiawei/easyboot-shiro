package com.wf.ew.common.layui;

import java.util.HashMap;
import java.util.List;

/**
 * \* Tree
 * \* @name: Tree
 * \* @author: 武佳伟丶
 * \* @date: 2018/8/4 0004
 * \* @time: 19:38
 * \
 */
public class Tree extends HashMap<String, Object> {

    public Tree() {
        super();
    }

    private String name;
    private boolean spread;
    private String href;
    private List<Tree> children;

    public String getName() {
        return (String) super.get(name);
    }

    public void setName(String name) {
        super.put("name", name);
    }

    public boolean isSpread() {
        return (Boolean) super.get(spread);
    }

    public void setSpread(boolean spread) {
        super.put("spread", spread);
    }

    public String getHref() {
        return (String) super.get(href);
    }

    public void setHref(String href) {
        super.put("href", href);
    }

    public List<Tree> getChildren() {
        return (List<Tree>)super.get(children);
    }

    public void setChildren(List<Tree> children) {
        super.put("children", children);
    }
}