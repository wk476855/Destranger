package com.android.destranger.ui;

import com.android.destranger.data.UserInfo;

import java.util.ArrayList;

/**
 * Created by ximing on 2015/5/12.
 */
public interface IHome extends IRoot {
    void showStrangers(ArrayList<UserInfo> userInfos);
}
