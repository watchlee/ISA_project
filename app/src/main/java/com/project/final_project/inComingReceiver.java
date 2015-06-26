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
import android.net.Uri;
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


    private static String phoneNumber = null;
    private static Boolean flag = false;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.


        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            MainActivity.setTextView("來電號碼 : " + phoneNumber + "\n");
            /*當媽媽來電則開始啟動Service*/
            if(phoneNumber.equals("0932614079")||phoneNumber.equals("0939738271")||phoneNumber.equals("0929520922")) {
            /*啟動打電話 function*/
                flag = true;

            }


        }
        else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE))
        {
            MainActivity.setTextView(phoneNumber+"中斷通話!");
            if(flag== true)
            {
                Intent call_intent = new Intent(context, Call_Service.class);
                context.startService(call_intent);
                flag = false;
            }

        }











    }









}
