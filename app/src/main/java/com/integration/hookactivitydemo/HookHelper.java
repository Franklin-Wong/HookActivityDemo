package com.integration.hookactivitydemo;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Wang
 * @version 1.0
 * @date 2021/8/26 - 13:05
 * @Description com.integration.hookactivitydemo
 */
public class HookHelper {

    private static final String TAG = "HookHelper";
    public static final String EXTRA_TARGET_INTENT = "extra_target_intent";

    public static void hookIActivityManager() {

        try {
            Field singletonField = null;

            //
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Class<?> activityManager = Class.forName("android.app.ActivityManager");
                singletonField = activityManager.getDeclaredField("IActivityManagerSingleton");

            } else {
                Class<?> activityManager = Class.forName("android.app.ActivityManagerNative");
                singletonField = activityManager.getDeclaredField("gDefault");
            }

            singletonField.setAccessible(true);
            Object singleton = singletonField.get(null);
            //拿IActivityManager对象
            Class<?> activityManager = Class.forName("android.util.Singleton");
            Field mInstanceField = activityManager.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            //原始的IActivityManager
            Object rawIActivityManager = mInstanceField.get(singleton);
            //创建一个IActivityManager代理对象
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{Class.forName("android.app.IActivityManager")},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            Log.i(TAG, "invoke: " + method.getName());
                            //偷梁换柱
                            //真正要启动的activity目标
                            Intent raw = null;
                            int index = -1;
                            //找到startActivity方法
                            if ("startActivity".equals(method.getName())) {
                                Log.i(TAG, "invoke: startActivity 启动准备");
                                for (int i = 0; i < args.length; i++) {
                                    if (args[i] instanceof Intent) {
                                        raw = (Intent) args[i];
                                        index = i;
                                    }
                                }
                            }
                            Log.i(TAG, "invoke: raw :" + raw);
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("", StubActivity.class.getName()));
                            intent.putExtra(EXTRA_TARGET_INTENT, raw);
                            args[index] = intent;

                            return method.invoke(rawIActivityManager, args);
                        }
                    });
            //7. IActivityManagerProxy 融入到framework
            mInstanceField.set(singleton,proxy);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 适配Android 10
     */
    public static void hookIActivityTaskManager() {


    }




}
