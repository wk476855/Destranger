package com.android.destranger.com.android.destranger.push;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.IBinder;


public class SocketService extends Service {

    String host = null;
    String port = null;

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
        //get server host and port
        ComponentName service = new ComponentName(this, SocketService.class);
        try {
            ServiceInfo info = getPackageManager().getServiceInfo(service, PackageManager.GET_META_DATA);
            host = info.metaData.getString("server");
            port = String.valueOf(info.metaData.getInt("port"));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //get session
        String session = "123132331";
        //TODO
        //connect server
        AsynConnectTask asynConnectTask = new AsynConnectTask(SocketService.this);
        asynConnectTask.execute(host, port, session);



//        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       return  super.onStartCommand(intent, flags, startId);
    }
}
