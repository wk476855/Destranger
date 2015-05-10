package com.android.destranger.com.android.destranger.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SocketService extends Service {

    private ClientSocket clientSocket;

    public SocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
       super.onCreate();
        try {
            clientSocket = ClientSocket.getInstance("192.168.1.160", 12345);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AsynSendTask asynSendTask = new AsynSendTask(this);
        asynSendTask.execute(clientSocket);
    }
}
