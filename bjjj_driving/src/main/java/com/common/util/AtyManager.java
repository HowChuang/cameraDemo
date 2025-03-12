package com.common.util;

import android.app.Activity;
import android.content.Intent;

import java.util.HashSet;
import java.util.Stack;

/**
 * Created by Rendy
 * 16/7/6
 * Todo 界面管理类
 */
public class AtyManager {
    private static AtyManager ourInstance = new AtyManager();
    private static HashSet<String> atyName = new HashSet<String>();

    public static AtyManager getInstance() {
        return ourInstance;
    }

    private static Stack<Activity> activityStack = new Stack<Activity>();

    private AtyManager() {
    }

    /**
     * 退出App
     */
    public void exitApp() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
        System.exit(0);
    }


    public Activity currentActivity() {
        Activity activity = null;
        if (!activityStack.empty())
            activity = activityStack.lastElement();
        return activity;
    }

    /**
     * @param context
     * @param nameClass 之前已经包含需要启动的aty展现之前的 aty; 否则开启新的 aty
     */
    public void showTargetAty(Activity context, Class nameClass) {
        if (isContentAty(nameClass)) {
            showTargetAty(nameClass);
        } else {
            context.startActivity(new Intent(context, nameClass));
        }
    }

    private void showTargetAty(Class nameClass) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().getName().equals(nameClass.getName())) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * @param nameClass
     * @return
     */
    public static boolean isContentAty(Class nameClass) {
        return atyName.contains(nameClass.getName());
    }

    /**
     * @param activity 推出栈
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            // 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
            activity.finish();
            activityStack.remove(activity);
            atyName.remove(activity.getClass().getName());
            activity = null;
        }
    }


    /**
     * @param activity 添加到栈中
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        atyName.add(activity.getClass().getName());
        activityStack.add(activity);
    }
}
