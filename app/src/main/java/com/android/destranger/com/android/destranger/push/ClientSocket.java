package com.android.destranger.com.android.destranger.push;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by wk on 2015/5/10.
 */
public class ClientSocket {

    private static ClientSocket clientSocket;
    private Socket socket;
    private OutputStream out = null;
    private InputStream in = null;
    private BufferedInputStream bis = null;
    private BufferedOutputStream bos = null;


    private ClientSocket() {
    }

    private ClientSocket(String IP, int port) throws IOException {
        socket = new Socket(IP, port);
        if (socket != null) {
            in = socket.getInputStream();
            bis = new BufferedInputStream(in);
            out = socket.getOutputStream();
            bos = new BufferedOutputStream(out);
        }
    }


    public synchronized static ClientSocket getInstance(String IP, int port) throws IOException {
        if (clientSocket == null)
            clientSocket = new ClientSocket(IP, port);
        return clientSocket;
    }

    public void send(Message msg) throws IOException {
        JSONObject json = new JSONObject();
        try {
            json.put("message", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (socket != null && bos != null) {
            bos.write(json.toString().getBytes());
            bos.flush();
        }
    }

    public void close() throws IOException {
        if (bis != null) {
            bis.close();
            in.close();
        }
        if (bos != null) {
            bos.close();
            out.close();
        }
        if (socket != null)
            socket.close();
    }
}
