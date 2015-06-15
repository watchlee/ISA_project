package com.project.final_project;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.util.Log;

public class listenerService extends Service {
    public listenerService() {
    }

    //接收短信广播
    IntentFilter filter_Phone = new IntentFilter("android.intent.action.PHONE_STATE");
    //创建来电广播接收器
    inComingReceiver iReceiver_Phone = new inComingReceiver();
    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub

        Log.i("Information : ", "開啟打電話功能");
/*
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:0932614079"));
        startActivity(intent);
        */

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        registerReceiver(iReceiver_Phone, filter_Phone);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        //注销广播接收器

        unregisterReceiver(iReceiver_Phone);
        System.out.println("service is stop");

    }
}
