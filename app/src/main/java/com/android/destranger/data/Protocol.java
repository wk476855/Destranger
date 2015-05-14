package com.android.destranger.data;

/**
 * Created by ximing on 2015/5/10.
 */
public class Protocol {
    private static final String IP = "192.168.1.209:8080";
    private static final String prefix = "http://" + IP + "/DestrangerServer";
    public static final String REGISTER_URL = prefix + "/register";
    public static final String LOGIN_URL = prefix + "/login";
    public static final String UPDATE_LOC_URL = prefix + "/strangers";
}
