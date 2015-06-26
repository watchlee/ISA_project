/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.project.final_project;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class Call_Service extends Service {



    public Call_Service() {
    }

    @Override
    public void onCreate() {
        TelephonyManager manager = (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);
        manager.listen(phoneStateListener,phoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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


    /*需要在此加入取得GPS的function 和 編碼的function*/



    PhoneStateListener phoneStateListener = new PhoneStateListener() {

        int number = 0;
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            ITelephony iTelephony = getITelephony(Call_Service.this);

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
                    if(number<=10) {
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



        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
