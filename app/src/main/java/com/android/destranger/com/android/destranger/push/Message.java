package com.android.destranger.com.android.destranger.push;

import org.json.JSONObject;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by wk on 2015/5/10.
 */
public class Message{

    private byte[] content;
    private String from;
    private String to;
    private Date time;
    private int type;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
