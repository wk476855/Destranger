package com.android.destranger.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ximing on 2015/5/10.
 */
public class NetworkSingleton {
    private static NetworkSingleton networkSingleton;
    private RequestQueue requestQueue;
    private static Context ctx;
    private NetworkSingleton(Context context)
    {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized NetworkSingleton getInstance(Context context)
    {
        if(networkSingleton == null)
            networkSingleton = new NetworkSingleton(context);
        return networkSingleton;
    }

    public RequestQueue getRequestQueue()
    {
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        requestQueue.add(req);
    }
}
