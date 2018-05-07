package com.symantec.startmobile.hookbinder.deprecated;

import android.app.Application;
import android.content.Context;
import android.os.IBinder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created by TommyD on 2018/5/3.
 */

public class MainApp extends Application {

    private static final String TAG = "TOMMY";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void HookClipBoardBinder() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        final String CLIPBOARD_SERVICE = "clipboard";

        // 下面这一段的意思实际就是: ServiceManager.getService("clipboard");
        // 只不过 ServiceManager这个类是@hide的
        Class<?> serviceManager = Class.forName("android.os.ServiceManager");
        Method getService = serviceManager.getDeclaredMethod("getService", String.class);
        // ServiceManager里面管理的原始的Clipboard Binder对象
        // 一般来说这是一个Binder代理对象
        IBinder rawBinder = (IBinder) getService.invoke(null, CLIPBOARD_SERVICE);
        // Hook 掉这个Binder代理对象的 queryLocalInterface 方法
        // 然后在 queryLocalInterface 返回一个IInterface对象, hook掉我们感兴趣的方法即可.
        IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(serviceManager.getClassLoader(),
                new Class<?>[] { IBinder.class },
                new ClipboardBinderProxyHookHandler(rawBinder));

        // 把这个hook过的Binder代理对象放进ServiceManager的cache里面
        // 以后查询的时候 会优先查询缓存里面的Binder, 这样就会使用被我们修改过的Binder了
        Field cacheField = serviceManager.getDeclaredField("sCache");
        cacheField.setAccessible(true);
        Map<String, IBinder> cache = (Map) cacheField.get(null);
        cache.put(CLIPBOARD_SERVICE, hookedBinder);
    }

    public void HookSmsBinder() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        final String SMS_SERVICE = "isms";
        // 下面这一段的意思实际就是: ServiceManager.getService("isms");
        // 只不过 ServiceManager这个类是@hide的
        Class<?> serviceManager = Class.forName("android.os.ServiceManager");
        Method getService = serviceManager.getDeclaredMethod("getService", String.class);
        // ServiceManager里面管理的原始的Clipboard Binder对象
        // 一般来说这是一个Binder代理对象
        IBinder rawBinder = (IBinder) getService.invoke(null, SMS_SERVICE);

        IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(serviceManager.getClassLoader(),
                new Class<?>[] { IBinder.class },
                new SmsBinderProxyHookHandler(rawBinder));

        // 把这个hook过的Binder代理对象放进ServiceManager的cache里面
        // 以后查询的时候 会优先查询缓存里面的Binder, 这样就会使用被我们修改过的Binder了
        Field cacheField = serviceManager.getDeclaredField("sCache");
        cacheField.setAccessible(true);
        Map<String, IBinder> cache = (Map) cacheField.get(null);
        cache.put(SMS_SERVICE, hookedBinder);
    }

    public void HookPhoneSubInfoBinder() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        final String IPHONESUBINFO_SERVICE = "iphonesubinfo";
        Class<?> serviceManager = Class.forName("android.os.ServiceManager");
        Method getService = serviceManager.getDeclaredMethod("getService", String.class);
        IBinder rawBinder = (IBinder) getService.invoke(null, IPHONESUBINFO_SERVICE);

        IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(serviceManager.getClassLoader(),
                new Class<?>[] { IBinder.class },
                new PhoneSubInfoBinderProxyHookHandler(rawBinder));

        Field cacheField = serviceManager.getDeclaredField("sCache");
        cacheField.setAccessible(true);
        Map<String, IBinder> cache = (Map) cacheField.get(null);
        cache.put(IPHONESUBINFO_SERVICE, hookedBinder);
    }

    @Override
    protected void attachBaseContext(Context base) {
        try {
            HookClipBoardBinder();
            HookSmsBinder();
            HookPhoneSubInfoBinder();
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
        super.attachBaseContext(base);
    }
}
