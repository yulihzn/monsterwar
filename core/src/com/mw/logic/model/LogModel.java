package com.mw.logic.model;

/**
 * Created by BanditCat on 2016/8/8.
 */
public class LogModel {
    private String msg;
    private int id;
    private int type;

    public LogModel(String msg,int type,int id) {
        this.id = id;
        this.msg = msg;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
