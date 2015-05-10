package com.android.destranger.com.android.destranger.push;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by wk on 2015/5/10.
 */
public class Message implements Comparator<Message>{

    private byte[] content;
    private String from;
    private String to;
    private long time;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    @Override
    public int compare(Message lhs, Message rhs) {
        if(lhs.getTime() > rhs.getTime())
            return 1;
        if(lhs.getTime() == rhs.getTime())
            return 0;
        if(lhs.getTime() < rhs.getTime())
            return -1;
        return 0;
    }

}
