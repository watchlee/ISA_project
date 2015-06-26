package com.project.final_project;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;


public class MainActivity extends Activity {

    public static String IP = null;
    private Button start_button = null;
    private Button stop_button = null;



    private Button phone_button = null;

    private static TextView info_text = null;
    private static EditText info_edit = null;
    private static ContentResolver resolver = null;
    private static int number = 0;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        start_button = (Button)findViewById(R.id.start_button);
        stop_button = (Button)findViewById(R.id.stop_button);

        info_text = (TextView)findViewById(R.id.textView);

        info_text.setText("Weclome!\n");

        start_button.setOnClickListener(new ServiceListener());
        stop_button.setOnClickListener(new ServiceListener());

        phone_button = (Button)findViewById(R.id.button);
        phone_button.setText("撥打電話");
        phone_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:0932614079"));
                startActivity(intent);

                ITelephony iTelephony = getITelephony(MainActivity.this);

                if (iTelephony != null) {
                    try {
                        iTelephony.endCall(); // 挂断电话

                    } catch (RemoteException e) {

                    }
                }
            }
        });

        resolver = this.getContentResolver();



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


        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            ITelephony iTelephony = getITelephony(MainActivity.this);
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



                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    if(number<=3) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
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



    class ServiceListener implements OnClickListener
    {


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this,listenerService.class);
            switch (v.getId())
            {
                case R.id.start_button:
                    Log.i("Service","Start Service!!\n");

                    startService(intent);
                    break;
                case R.id.stop_button:
                    Log.i("Service","Stop Service!!\n");
                    stopService(intent);
                    break;

                default:
                    break;
            }

        }
    }




    public static ContentResolver sendContent()
    {
        return  resolver;
    }




    public static void setTextView(String string)
    {
        info_text.setText(string);

    }


}
