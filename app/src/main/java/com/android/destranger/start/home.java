package com.android.destranger.start;

import android.content.Intent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.destranger.R;
import com.android.destranger.data.Protocol;
import com.android.destranger.data.UserInfo;
import com.android.destranger.network.Communication;
import com.android.destranger.network.MessageHandler;
import com.android.destranger.ui.IHome;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class home extends ActionBarActivity implements SensorListener ,BDLocationListener, BaiduMap.OnMarkerClickListener,IHome{

    public SensorManager sm = null;

    private MapView mapView = null;
    private BaiduMap baiduMap = null;
    private LocationClient locationClient;
    private LatLng loc = null;
    private int mSpeed=3000;
    private int mInterval=50;
    private long LastTime;
    private float LastX,LastY,LastZ;

    private UserInfo userInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_home);

        sm = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        initMap();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        userInfo = (UserInfo) bundle.getSerializable("userInfo");
    }

    public void initMap()
    {
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        MyLocationConfiguration.LocationMode locationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(locationMode,true,null));
        baiduMap.setOnMarkerClickListener(this);

        locationClient = new LocationClient(this);
        locationClient.registerLocationListener(this);
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setOpenGps(true);
        locationClientOption.setCoorType("bd09ll");
        locationClient.setLocOption(locationClientOption);
        locationClient.start();

    }

    public void updateLoc(LatLng location)
    {
        Communication com = new Communication(this,new MessageHandler(this));
        com.setUrl(Protocol.UPDATE_LOC_URL);
        com.setCode(0x003);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", userInfo.getUid());
            jsonObject.put("latitude",location.latitude);
            jsonObject.put("longitude",location.longitude);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        com.setParams(jsonObject);
        com.sendGetRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    public void shake(View view)
    {
        sm.registerListener(this,SensorManager.SENSOR_ACCELEROMETER);
    }

    public void stop(View view)
    {
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(int sensor, float[] values)
    {
        if(sensor == SensorManager.SENSOR_ACCELEROMETER)
        {
            long NowTime = System.currentTimeMillis();
            if ((NowTime - LastTime) < mInterval)
                return;
            LastTime = NowTime;
            float NowX = values[0];
            float NowY = values[1];
            float NowZ = values[2];
            float DeltaX = NowX - LastX;
            float DeltaY = NowY - LastY;
            float DeltaZ = NowZ - LastZ;
            LastX = NowX;
            LastY = NowY;
            LastZ = NowZ;
            double NowSpeed = Math.sqrt(DeltaX * DeltaX + DeltaY * DeltaY + DeltaZ * DeltaZ) / mInterval * 10000;
            if (NowSpeed >= mSpeed)
            {
                System.out.println("shake");
                if(loc != null)
                    updateLoc(loc);
            }
        }

    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if(bdLocation == null || mapView == null)
            return;
        MyLocationData locData = new MyLocationData.Builder().direction(2.0f)
                .accuracy(0).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        baiduMap.setMyLocationData(locData);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomBy(5);
        baiduMap.setMapStatus(msu);
        loc = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Bundle data = marker.getExtraInfo();
        UserInfo userInfo = (UserInfo) data.getSerializable("userInfo");
        hint(userInfo.getUsername());
        return false;
    }

    @Override
    public void hint(String str) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showStrangers(ArrayList<UserInfo> userInfos)
    {
        mapView.getOverlay().clear();
        for(UserInfo userInfo : userInfos)
        {
            Bundle data = new Bundle();
            data.putSerializable("userInfo",userInfo);
            LatLng latLng = new LatLng(userInfo.getUserLoc().getLatitude(),userInfo.getUserLoc().getLongitude());
            OverlayOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)).position(latLng).title(userInfo.getUsername());

            Marker marker = (Marker) baiduMap.addOverlay(markerOption);
            marker.setExtraInfo(data);
            /*OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(24).fontColor(0xFFFF00FF).text(userInfo.getUsername())
                    .position(latLng).extraInfo(data);
            baiduMap.addOverlay(textOption);*/
        }
    }
}
