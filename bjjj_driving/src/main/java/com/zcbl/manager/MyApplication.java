package com.zcbl.manager;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.text.TextUtils;

import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;


public class MyApplication extends Application {


    public static MyApplication application;
    // 纬度
    public double Latitude = 0;
    // 經度
    public double Longitude = 0;
    //归属省份
    public String province = "";
    public String city = "";
    public String district = "";//行政区域

    public static MyApplication getApplication() {
        return application;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        String currentProcessName = getCurrentProcessName();
        if (!TextUtils.isEmpty(currentProcessName)) {
            if (getPackageName().equals(currentProcessName)) {
                ToastUtils.init(this);
                iniOkGon();
            } else if (currentProcessName.endsWith(":remote")) {

            }
        }


        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();



    }

    private void iniOkGon() {
        OkGo.getInstance().init(this)//必须调用初始化
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE) //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0);                         //全局统一
        //其他的使用的默认设置，比如链接时常、读取时常、超时断开时常等
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    private String getCurrentProcessName() {

        String currentProcessName = null;
        int pid = android.os.Process.myPid();
        int tid = android.os.Process.myTid();
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                currentProcessName = processInfo.processName;
                break;
            }
        }
        return currentProcessName;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }


}
