package com.android.destranger.data;

import java.io.Serializable;

/**
 * Created by ximing on 2015/5/10.
 */
public class UserInfo implements Serializable{
    private int uid;
    private String username;
    private String password;
    private int gender;
    private String head;
    private String session;
    private UserLoc userLoc;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public UserLoc getUserLoc() {
        return userLoc;
    }

    public void setUserLoc(UserLoc userLoc) {
        this.userLoc = userLoc;
    }


}
