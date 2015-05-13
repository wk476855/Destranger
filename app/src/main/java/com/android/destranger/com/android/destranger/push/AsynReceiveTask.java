package com.android.destranger.com.android.destranger.push;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

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
        Toast.makeText(context, values[0], Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(ClientSocket... params) {
        ClientSocket clientSocket = params[0];
        if(clientSocket != null) {
            try {
                while (true) {
                    ProtocolPair pair = clientSocket.receive();
                    ConcurrentQueue.Receive_Queue.put(pair);
                }
            } catch (IOException e) {
//                e.printStackTrace();
                publishProgress(e.getMessage());
            } catch (InterruptedException e) {
//                e.printStackTrace();
                publishProgress(e.getMessage());
            }
        }
        return null;
    }
}
