package com.symantec.startmobile.hookbinder.deprecated;

import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by TommyD on 2018/5/4.
 */

public class PhoneSubInfoBinderProxyHookHandler implements InvocationHandler {

    private static final String TAG = "TOMMY";
    IBinder base;
    Class<?> stub;
    Class<?> iinterface;

    public PhoneSubInfoBinderProxyHookHandler(IBinder base) {
        this.base = base;
        try {
            this.stub = Class.forName("com.android.internal.telephony.IPhoneSubInfo$Stub");
            this.iinterface = Class.forName("com.android.internal.telephony.IPhoneSubInfo");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("queryLocalInterface".equals(method.getName())) {
            Log.d(TAG, "hook queryLocalInterface");

            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),
                    new Class[] { IBinder.class, IInterface.class, this.iinterface },
                    new PhoneSubInfoBinderHookHandler(base, stub));
        }

        Log.d(TAG, "method:" + method.getName());
        return method.invoke(base, args);
    }
}
