package com.symantec.startmobile.hookbinder.hooks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by TommyD on 2018/5/4.
 */

public interface ICallback {
    Object invoke(Object base, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException;
}
