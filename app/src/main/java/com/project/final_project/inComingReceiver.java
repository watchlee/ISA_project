/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.project.final_project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class inComingReceiver extends BroadcastReceiver {
    public inComingReceiver() {
    }

    private static final String TAG = null;
    private static String phoneNumber = null;
    private static String contact_num = null;
    private static String contact_name = null;
    private static String info = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.






        //监听电话服务
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        CustomPhoneStateListener customPhoneStateListener = new CustomPhoneStateListener();
        telephony.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


        //接收intent对象
        Bundle bundle = intent.getExtras();
        //获取来电号码


        //获取来电号码
        phoneNumber = bundle.getString("incoming_number");


        contact_num = phoneNumber;


                info = "主人，来电话啦！";
        try {
            if (contact_num.toString().equals("0932614079")) {
                //更新TextView
                MainActivity.setTextView("注意:\t" + info + "\n" + "号码:\t" + contact_num + "\n");

            }
        } catch (java.lang.NullPointerException e) {


            MainActivity.setTextView("注意: 來電中斷\t");
        }
    }
    private  ITelephony getITelephony(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELECOM_SERVICE);

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




    public class CustomPhoneStateListener extends PhoneStateListener {



        private static final String TAG = "CustomPhoneStateListener";

        @Override
        public void onCallStateChanged(int state, String incomingNumber){

            Log.v(TAG, "WE ARE INSIDE!!!!!!!!!!!");
            Log.v(TAG, incomingNumber);
            //检测来电的状态
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:

                    Log.d(TAG, "RINGING");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG, "IDLE");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "OFFHOOK");
                    break;
            }


        }



    }





}
