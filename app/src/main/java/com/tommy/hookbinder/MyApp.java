package com.symantec.startmobile.hookbinder;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.symantec.startmobile.hookbinder.hooks.ClipboardHook;
import com.symantec.startmobile.hookbinder.hooks.HookInfo;
import com.symantec.startmobile.hookbinder.hooks.PhoneSubInfoHook;
import com.symantec.startmobile.hookbinder.hooks.SmsHook;
import com.symantec.startmobile.hookbinder.hooks.TelephonyHook;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static com.symantec.startmobile.hookbinder.BinderHook.HookBinder;

/**
 * Created by TommyD on 2018/5/4.
 */

public class MyApp extends Application {
    private static final String TAG = "TOMMY";
    @Override
    public void onCreate() {
        super.onCreate();
    }

    private ArrayList<HookInfo> hookInfos = new ArrayList<>();
    private void initHook() {
        hookInfos.add(new ClipboardHook());
        hookInfos.add(new SmsHook());
        if(Build.VERSION.SDK_INT < 23)
            hookInfos.add(new PhoneSubInfoHook());
        else
            hookInfos.add(new TelephonyHook());
    }

    @Override
    protected void attachBaseContext(Context base) {
        initHook();
        for(HookInfo hook : hookInfos) {
            try {
                HookBinder(hook);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        super.attachBaseContext(base);
    }
}
