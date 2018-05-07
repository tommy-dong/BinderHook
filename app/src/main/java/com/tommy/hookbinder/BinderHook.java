package com.symantec.startmobile.hookbinder;

import android.os.IBinder;

import com.symantec.startmobile.hookbinder.hooks.HookInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created by TommyD on 2018/5/4.
 */

public class BinderHook {
    public static void HookBinder(HookInfo hook) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Class<?> serviceManager = Class.forName("android.os.ServiceManager");
        Method getService = serviceManager.getDeclaredMethod("getService", String.class);
        IBinder rawBinder = (IBinder) getService.invoke(null, hook.serviceName);

        IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(serviceManager.getClassLoader(),
                new Class<?>[] { IBinder.class },
                new BinderProxyHookHandler(rawBinder, hook));

        Field cacheField = serviceManager.getDeclaredField("sCache");
        cacheField.setAccessible(true);
        Map<String, IBinder> cache = (Map) cacheField.get(null);
        cache.put(hook.serviceName, hookedBinder);
    }
}
