package com.android.destranger.network;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;


/**
 * Created by ximing on 2015/5/10.
 */
public class Communication2 {
    private Context context;
    private MessageHandler handler;
    private int code;
    private String url;
    private Map<String,String> params;

    public Communication2(Context context, MessageHandler handler)
    {
        this.context = context;
        this.handler = handler;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setParams(Map<String,String> params) {
        this.params = params;
    }

    public void sendPostRequest()
    {
        JsonObjectPostRequest JORequest = new JsonObjectPostRequest(url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Message msg = new Message();
                msg.what = code;
                Bundle data = new Bundle();
                data.putString("result",jsonObject.toString());
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volley",volleyError.toString());
            }
        });
        NetworkSingleton.getInstance(context.getApplicationContext()).addToRequestQueue(JORequest);
    }

}
