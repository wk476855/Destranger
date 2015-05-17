package com.android.destranger.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by ximing on 2015/5/14.
 */
public class JsonObjectPostRequest extends Request<JSONObject>{
    private Map<String,String> mMap;
    private Response.Listener<JSONObject> mListener;
    public String cookieFromResponse;
    private String mHeader;
    private Map<String,String> sendHeader = new HashMap<>(1);

    public JsonObjectPostRequest(String url,Map<String,String> map,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener)
    {
        super(Method.POST,url,errorListener);
        mListener = listener;
        mMap = map;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String json = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            JSONObject jsonObject = new JSONObject(json);
            mHeader = networkResponse.headers.toString();
            Log.e("destrangers", "headers" + mHeader);
            Pattern pattern = Pattern.compile("Set-Cookie.*?;");
            Matcher matcher = pattern.matcher(mHeader);
            if(matcher.find())
            {
                cookieFromResponse = matcher.group();
                Log.e("destrangers","cookie from server" + cookieFromResponse);
                cookieFromResponse = cookieFromResponse.substring(11,cookieFromResponse.length() - 1);
                Log.e("destrangers", "cookie substring" + cookieFromResponse);
                jsonObject.put("Cookie", cookieFromResponse);
            }
            return Response.success(jsonObject,HttpHeaderParser.parseCacheHeaders(networkResponse));
        }catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }

    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        mListener.onResponse(jsonObject);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return sendHeader;
    }

    public void setSendCookie(String cookie)
    {
        sendHeader.put("Cookie",cookie);
    }
}
