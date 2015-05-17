package com.android.destranger.com.android.destranger.push;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
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
        Looper.prepare();
        ClientSocket client = params[0];
        while (true) {
            if(client == null)  continue;
            try {
                if (!ConcurrentQueue.Wait_Queue.isEmpty()) {
                    ProtocolPair pair = ConcurrentQueue.Wait_Queue.take();
                    client.send(pair);
                }
            }catch (InterruptedException e) {
//                e.printStackTrace();
                publishProgress(e.getMessage());
            } catch (IOException e) {
//                e.printStackTrace();
                publishProgress(e.getMessage());
            }
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Toast.makeText(context, values[0], Toast.LENGTH_SHORT).show();
    }
}
