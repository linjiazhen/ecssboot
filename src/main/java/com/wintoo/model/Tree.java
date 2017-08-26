package com.wintoo.model;

/**
 * Created by jason on 2015/3/5.
 */
public class Tree {
    private String id;
    private String parent;
    private String text;

    public void setId(String id) {
        this.id = id;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getParent() {
        return parent;
    }

    public String getText() {
        return text;
    }
}
