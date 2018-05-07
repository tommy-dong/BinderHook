package com.symantec.startmobile.hookbinder;

import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import com.symantec.startmobile.hookbinder.hooks.HookInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by TommyD on 2018/5/4.
 */

public class BinderProxyHookHandler implements InvocationHandler {
    private static final String TAG = "TOMMY";
    IBinder base;
    Class<?> stub;
    Class<?> iinterface;
    HookInfo hook;

    public BinderProxyHookHandler(IBinder base, HookInfo hook) {
        this.base = base;
        this.hook = hook;
        try {
            this.stub = Class.forName(hook.stub);
            this.iinterface = Class.forName(hook.iinterface);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("queryLocalInterface".equals(method.getName())) {
            Log.d(TAG, "hook queryLocalInterface");

            // 这里直接返回真正被Hook掉的Service接口
            // 这里的 queryLocalInterface 就不是原本的意思了
            // 我们肯定不会真的返回一个本地接口, 因为我们接管了 asInterface方法的作用
            // 因此必须是一个完整的 asInterface 过的 IInterface对象, 既要处理本地对象,也要处理代理对象
            // 这只是一个Hook点而已, 它原始的含义已经被我们重定义了; 因为我们会永远确保这个方法不返回null
            // 让 IClipboard.Stub.asInterface 永远走到if语句的else分支里面
            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),
                    // asInterface 的时候会检测是否是特定类型的接口然后进行强制转换
                    // 因此这里的动态代理生成的类型信息的类型必须是正确的
                    new Class[] { IBinder.class, IInterface.class, this.iinterface },
                    new BinderHookHandler(base, stub, hook.callback));
        }

        Log.d(TAG, "method:" + method.getName());
        return method.invoke(base, args);
    }
}
