package com.symantec.startmobile.hookbinder.deprecated;

import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by TommyD on 2018/5/3.
 */

public class SmsBinderHookHandler implements InvocationHandler {
    private static final String TAG = "TOMMY";

    // 原始的Service对象 (IInterface)
    Object base;

    public SmsBinderHookHandler(IBinder base, Class<?> stubClass) {
        try {
            Method asInterfaceMethod = stubClass.getDeclaredMethod("asInterface", IBinder.class);
            // ISms.Stub.asInterface(base);
            this.base = asInterfaceMethod.invoke(null, base);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

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
