package com.symantec.startmobile.hookbinder.hooks;

import android.content.ClipData;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by TommyD on 2018/5/4.
 */

public class ClipboardHook extends HookInfo implements ICallback {
    private static final String TAG = "TOMMY";

    public ClipboardHook() {
        this.stub = "android.content.IClipboard$Stub";
        this.iinterface = "android.content.IClipboard";
        this.serviceName = "clipboard";
        this.callback = this;
    }

    @Override
    public Object invoke(Object base, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if ("getPrimaryClip".equals(method.getName())) {
            Log.d(TAG, "hook getPrimaryClip");
            return ClipData.newPlainText(null, "you are hooked");
        }

        if ("hasPrimaryClip".equals(method.getName())) {
            Log.d(TAG, "hook hasPrimaryClip");
            return true;
        }

        return method.invoke(base, args);
    }
}
