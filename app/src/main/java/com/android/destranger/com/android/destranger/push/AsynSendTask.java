package com.android.destranger.com.android.destranger.push;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by wk on 2015/5/10.
 */
public class AsynSendTask extends AsyncTask<ClientSocket, String, Void>{

    Context context = null;

    public AsynSendTask(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected Void doInBackground(ClientSocket... params) {
        ClientSocket client = params[0];
        while (true) {
            if(client == null)  continue;
            if(!MessageQueue.Wait_Queue.isEmpty()) {
                Message msg = MessageQueue.Wait_Queue.poll();
                if(msg != null) {
                    MessageQueue.Send_Queue.push(msg);
                    try {
                        client.send(msg);
                    } catch (IOException e) {
                        publishProgress(e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Toast.makeText(context, values[0], Toast.LENGTH_SHORT).show();
    }
}
