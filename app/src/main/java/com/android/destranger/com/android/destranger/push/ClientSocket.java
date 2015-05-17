package com.android.destranger.com.android.destranger.push;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

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

    private ClientSocket(String host, int port) throws IOException {
//        socket = new Socket(host, port);
        InetSocketAddress address = new InetSocketAddress(host, port);
        socket = new Socket();
        if (socket != null) {
            socket.connect(address, 5000);
            in = socket.getInputStream();
            bis = new BufferedInputStream(in);
            out = socket.getOutputStream();
            bos = new BufferedOutputStream(out);
        }
    }


    public synchronized static ClientSocket getInstance(String host, int port) throws IOException {
        if (clientSocket == null)
            clientSocket = new ClientSocket(host, port);
        return clientSocket;
    }

    private byte[] generateHeader(int contentLen, int protocol){
        int totalLen = contentLen + 6;
        byte[] data = new byte[totalLen];
        byte[] tmp = DataToolkit.intToByteArray(contentLen, 4);
        System.arraycopy(tmp, 0, data, 0, 4);
        tmp = DataToolkit.intToByteArray(protocol, 2);
        System.arraycopy(tmp, 0, data, 4, 2);
        return data;
    }

    public void send(ProtocolPair pair) throws IOException {
        byte[] content = pair.content.getBytes();
        byte[] data = generateHeader(content.length, pair.protocol);   
        System.arraycopy(content, 0, data, 6, content.length);

        if (socket != null && bos != null) {
            bos.write(data);
            bos.flush();
        }
    }

    public ProtocolPair receive() throws IOException {
        ProtocolPair pair = null;
        byte[] header = new byte[8];
        byte[] headerCopy = new byte[8];
        byte[] content = new byte[256];
        int len = 0;
        int cnt = 0;
        int contentLen = -1;
        int protocol = -1;
        int once;
        StringBuilder sb = new StringBuilder();

        while (true) {
            len = bis.read(header, cnt, 4 - cnt);
            if (len <= 0)
                break;
            System.arraycopy(header, cnt, headerCopy, cnt, len);
            cnt += len;
            if (cnt == 4) {
                contentLen = DataToolkit.byteArrayToInt(headerCopy, 4);
                break;
            }
        }

        if (contentLen != -1) {
            cnt = 0;
            while (true) {
                len = bis.read(header, cnt, 2 - cnt);
                System.arraycopy(header, cnt, headerCopy, cnt, len);
                cnt += len;
                if (cnt == 2) {
                    protocol = DataToolkit.byteArrayToInt(headerCopy, 2);
                    break;
                }
            }
        }

        if (protocol != -1) {
            cnt = 0;
            while (true) {
                once = Math.min(256, contentLen - cnt);
                if ((len = bis.read(content, 0, once)) > 0) {
                    cnt += len;
                    sb.append(new String(content, 0, len));
                    System.err.println(new String(content, 0, len));
                    if (cnt >= contentLen) {
                        pair = new ProtocolPair(protocol, sb.toString());
                        break;
                    }
                }
            }
        }
        return pair;
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
