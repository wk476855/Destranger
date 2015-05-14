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


/**
 * Created by ximing on 2015/5/10.
 */
public class Communication {
    private Context context;
    private MessageHandler handler;
    private int code;
    private String url;
    private JSONObject params;

    public Communication(Context context,MessageHandler handler)
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

    public void setParams(JSONObject params) {
        this.params = params;
    }

    public void sendGetRequest()
    {
        String trueUrl = getTrueUrl();
        System.out.println(trueUrl);
        JsonObjectRequest JORequest = new JsonObjectRequest(Request.Method.GET, trueUrl, null, new Response.Listener<JSONObject>() {
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

    public void sendPostRequest()
    {
        JsonObjectRequest JORequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
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

    private String getTrueUrl()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.url);
        sb.append("?");
        sb.append("request=");
        sb.append(this.params.toString());
        return sb.toString();
    }


}
