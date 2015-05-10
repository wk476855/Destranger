package com.android.destranger.com.android.destranger.push;

import android.os.AsyncTask;

/**
 * Created by wk on 2015/5/10.
 */
public class AsynSendTask extends AsyncTask<Void, Message, Void>{
    @Override
    protected Void doInBackground(Void... params) {
        while (true) {
            if(!MessageQueue.isEmpty()) {
                Message msg = MessageQueue.poll();
                if(msg != null) {

                }
            }
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
    }

    @Override
    protected void onProgressUpdate(Message... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
