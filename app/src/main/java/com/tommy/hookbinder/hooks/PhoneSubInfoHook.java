package com.symantec.startmobile.hookbinder.hooks;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by TommyD on 2018/5/4.
 */

public class PhoneSubInfoHook extends HookInfo implements ICallback {
    private static final String TAG = "TOMMY";

    public PhoneSubInfoHook() {
        this.stub = "com.android.internal.telephony.IPhoneSubInfo$Stub";
        this.iinterface =  "com.android.internal.telephony.IPhoneSubInfo";
        this.serviceName = "iphonesubinfo";
        this.callback = this;
    }
    @Override
    public Object invoke(Object base, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Log.d(TAG, "PhoneSubInfoHook");
        if ("getDeviceId".equals(method.getName())) {
            Log.d(TAG, "hook getDeviceId");
            return "do not joke me";
        }
        return method.invoke(base, args);
    }
}
