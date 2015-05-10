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
        Toast.makeText(this, "oncreate", Toast.LENGTH_SHORT).show();
        System.out.println("asdf");
        new Thread() {
            @Override
            public void run() {
                Socket socket = null;

                OutputStream out =null;
                BufferedOutputStream bos = null;
                try {
                    socket = new Socket("192.168.1.160", 12345);
                    out = socket.getOutputStream();
                    bos = new BufferedOutputStream(out);
                    JSONObject json = new JSONObject();
                    try {
                        json.put("msg", "sdfs");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bos.write(json.toString().getBytes());
                    bos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if(out != null)
                            out.close();
                        if(bos != null)
                            bos.close();
                        if(bos != null)
                            socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
