package com.android.destranger.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.destranger.data.UserInfo;
import com.android.destranger.data.UserLoc;
import com.android.destranger.ui.IHome;
import com.android.destranger.ui.ILogin;
import com.android.destranger.ui.IRoot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ximing on 2015/5/10.
 */
public class MessageHandler extends Handler {
    public IRoot ui;
    public MessageHandler(IRoot ui)
    {
        this.ui = ui;
    }

    @Override
    public void handleMessage(Message msg) {
        int code = msg.what;
        Bundle data = msg.getData();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data.getString("result"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch (code)
        {
            /**
             * for register
             */
            case 0x001:
                try {
                    ui.hint(jsonObject.getString("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            /**
             * for login
             */
            case 0x002:
                try {
                    int result = jsonObject.getInt("result");
                    if(result == 1) {
                        ui.hint("登陆成功");
                        UserInfo userInfo = new UserInfo();
                        userInfo.setUid(jsonObject.getInt("uid"));
                        userInfo.setUsername(jsonObject.getString("username"));
                        userInfo.setPassword(jsonObject.getString("password"));
                        userInfo.setHead(jsonObject.getString("head"));
                        userInfo.setGender(jsonObject.getInt("gender"));
                        userInfo.setSession(jsonObject.getString("session"));
                        ((ILogin) ui).jump(userInfo);
                    }
                    else
                        ui.hint("登陆失败");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            /**
             * shake for users
             */
            case 0x003:
                try {
                    int result = jsonObject.getInt("result");
                    if(result == 0)
                        ui.hint("附近没人");
                    else
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("userInfos");
                        JSONObject userObject;
                        ArrayList<UserInfo> userInfos = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            userObject = jsonArray.getJSONObject(i);
                            UserInfo userInfo = new UserInfo();
                            userInfo.setUid(userObject.getInt("uid"));
                            userInfo.setUsername(userObject.getString("username"));
                            userInfo.setPassword(userObject.getString("password"));
                            userInfo.setHead(userObject.getString("head"));
                            userInfo.setGender(userObject.getInt("gender"));
                            JSONObject loc = userObject.getJSONObject("userloc");
                            UserLoc userLoc = new UserLoc();
                            userLoc.setUid(loc.getInt("uid"));
                            userLoc.setLatitude(loc.getDouble("latitude"));
                            userLoc.setLongitude(loc.getDouble("longitude"));
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            userLoc.setTimestamp(df.parse(loc.getString("timestamp")));
                            userInfo.setUserLoc(userLoc);
                            userInfos.add(userInfo);
                        }
                        ((IHome)ui).showStrangers(userInfos);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
