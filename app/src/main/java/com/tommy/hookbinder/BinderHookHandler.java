package com.symantec.startmobile.hookbinder;

import android.os.IBinder;

import com.symantec.startmobile.hookbinder.hooks.ICallback;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by TommyD on 2018/5/4.
 */

public class BinderHookHandler implements InvocationHandler {
    Object base;
    ICallback callback;

    public BinderHookHandler(IBinder base, Class<?> stubClass, ICallback callback) {
        this.callback = callback;
        try {
            Method asInterfaceMethod = stubClass.getDeclaredMethod("asInterface", IBinder.class);
            this.base = asInterfaceMethod.invoke(null, base);
        } catch (Exception e) {
            throw new RuntimeException("hooked failed!");
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return callback.invoke(base, method, args);
    }
}
