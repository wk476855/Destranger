package com.android.destranger.com.android.destranger.push;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by wk on 2015/5/11.
 */
public class AsynConnectTask extends AsyncTask<String, String, ClientSocket> {

    Context context;
    ClientSocket clientSocket = null;

    public AsynConnectTask(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Toast.makeText(context, values[0], Toast.LENGTH_SHORT).show();
    }

    @Override
    protected ClientSocket doInBackground(String... params) {
        Looper.prepare();
        if (params.length <= 2) {
            publishProgress("AsyConnectTask paramters error");
            return null;
        }
        String host = params[0];
        int port = Integer.parseInt(params[1]);
        String session = params[2];
        while (clientSocket == null) {
            try {
                clientSocket = ClientSocket.getInstance(host, port);
            } catch (IOException e) {
//                e.printStackTrace();
                publishProgress(e.getMessage());
            }
        }

        //session confirm
        try {
            JSONObject json = new JSONObject();
            json.put("session", session);
            ConcurrentQueue.Wait_Queue.put(new ProtocolPair(Protocol.SESSION_COMFIRM, json.toString()));
        } catch (InterruptedException e) {
//            e.printStackTrace();
            publishProgress(e.getMessage());
        } catch (JSONException e) {
//            e.printStackTrace();
            publishProgress(e.getMessage());
        }
        return clientSocket;
    }

    @Override
    protected void onPostExecute(ClientSocket clientSocket) {
        super.onPostExecute(clientSocket);
        //start send task
        AsynSendTask asynSendTask = new AsynSendTask(context);
        asynSendTask.execute(clientSocket);

        //start receive task
        AsynReceiveTask asynReceiveTask = new AsynReceiveTask(context);
        asynReceiveTask.execute(clientSocket);
    }
}
