package com.symantec.startmobile.hookbinder.hooks;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by TommyD on 2018/5/4.
 */

public class SmsHook extends HookInfo implements ICallback {
    private static final String TAG = "TOMMY";

    public SmsHook() {
        this.stub = "com.android.internal.telephony.ISms$Stub";
        this.iinterface =  "com.android.internal.telephony.ISms";
        this.serviceName = "isms";
        this.callback = this;
    }
    @Override
    public Object invoke(Object base, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if ("sendText".equals(method.getName())) {
            Log.d(TAG, "hook sendText");
            return null;
        } else if ("sendTextForSubscriber".equalsIgnoreCase(method.getName())){
            Log.d(TAG, "hook sendTextForSubscriber");
            return null;
        }

        return method.invoke(base, args);
    }
}
