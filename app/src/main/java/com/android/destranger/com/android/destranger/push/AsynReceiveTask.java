package com.android.destranger.com.android.destranger.push;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by wk on 2015/5/11.
 */
public class AsynReceiveTask extends AsyncTask<ClientSocket, String, Void>{

    Context context;
    public AsynReceiveTask(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(ClientSocket... params) {
        return null;
    }
}
