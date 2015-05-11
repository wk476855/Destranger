package com.android.destranger;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.destranger.com.android.destranger.push.SocketService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getApplicationContext(), SocketService.class);
        startService(intent);
    }


    public void send(View view) {
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


    @Override
    protected void onPause() {
//        XGPushManager.unregisterPush(getApplicationContext());
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
