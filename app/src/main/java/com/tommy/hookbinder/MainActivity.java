package com.symantec.startmobile.hookbinder;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private Button btn1;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Class.forName("com.android.internal.telephony.ISms$Stub");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());
        //TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //tv.setText(telephonyManager.getDeviceId());
        //requestPhoneStatePermission();

        btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, "内容"));  // 将内容set到剪贴板
                if (clipboardManager.hasPrimaryClip()){
                    Log.i("TOMMY", "clipboard content: " + clipboardManager.getPrimaryClip().getItemAt(0).getText());  // 获取内容
                }
            }
        });

        btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSmsPermission();
            }
        });

        btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPhoneStatePermission();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case PERMISSION_PHONE_STATE:
                case PERMISSION_SEND_SMS: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted
                        Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        // permission denied
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }
    }

    private static final int PERMISSION_SEND_SMS = 123;
    private static final int PERMISSION_PHONE_STATE = 124;
    private void requestSmsPermission() {
        // check permission is given
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_SEND_SMS);
        } else {
            // permission already granted run sms send
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("10086", null, "test", null, null);
        }
    }

    private void requestPhoneStatePermission() {
        // check permission is given
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_PHONE_STATE);
        } else {
            TextView tv = (TextView) findViewById(R.id.sample_text);
            //tv.setText(stringFromJNI());
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            tv.setText(telephonyManager.getDeviceId());
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
