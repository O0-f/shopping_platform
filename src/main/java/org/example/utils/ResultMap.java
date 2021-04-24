package org.example.utils;

import java.io.Serializable;

public class ResultMap implements Serializable {

    private static final long serialVersionUID = 491691775166685841L;
    private static volatile ResultMap instance;
    private String status;
    private String msg;
    private Object data;

    private ResultMap() {
        this.status = Status.SUCCESS.getStatus();
        this.msg = "";
        this.data = null;
    }

    public static synchronized ResultMap getInstance() {
        if (instance == null) {
            synchronized (ResultMap.class) {
                if (instance == null) {
                    instance = new ResultMap();
                }
            }
        }
        return instance;
    }

    public static void setInstance(String status, String msg, Object data) {
        getInstance().setStatus(status);
        getInstance().setMsg(msg);
        getInstance().setData(data);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
