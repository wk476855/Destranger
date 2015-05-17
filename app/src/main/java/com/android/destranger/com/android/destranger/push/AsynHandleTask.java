package com.android.destranger.com.android.destranger.push;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.os.Looper;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wk on 2015/5/14.
 */
public class AsynHandleTask extends AsyncTask<LinkedBlockingQueue<ProtocolPair>, Message, Void>{

    public Context context;

    public AsynHandleTask(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected Void doInBackground(LinkedBlockingQueue<ProtocolPair>... params) {
        Looper.prepare();
        LinkedBlockingQueue<ProtocolPair> queue = params[0];
        if(queue != null) {
            while (true) {
                try {
                    if (!queue.isEmpty()) {
                        ProtocolPair pair = queue.take();
                        switch (pair.protocol) {
                            case Protocol.MESSAGE_RECEIVE:
                                Message msg = new Gson().fromJson(pair.content, Message.class);
                                publishProgress(msg);
                            case Protocol.MESSAGE_SEND_COMFIRM:
                                JSONObject json = new JSONObject(pair.content);
                                if(json.has("ret_code")) {
                                    //TODO
                                }
                        }
                    }
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Message... values) {
        Message msg = values[0];
        if(msg != null) {
            if(msg.getTo().equals(CurrentChatUser.username)) {
                Intent intent = new Intent("com.android.destranger.push");
                intent.putExtra("message", msg);
                context.sendBroadcast(intent);
            }else {

            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
