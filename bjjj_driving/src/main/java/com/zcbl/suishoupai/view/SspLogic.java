package com.zcbl.suishoupai.view;

import android.app.Activity;
import android.content.Intent;

import com.zcbl.suishoupai.HomeSSPActivity;

/**
 * 创建日期：2020/2/17 下午6:23
 * 描述: 随手拍动态逻辑处理
 * 作者: 赵伟闯
 */
public class SspLogic {
    //快速举报视频地址
    public static String FASR_REPORT_VIDEO_PAHT = null;
    public static String FASR_REPORT_VIDEO_PAHT_TIME = null;
    //设施报修
    public static String REPAIR_PIC_1 = null;
    public static String REPAIR_PIC_1_TIME = null;
    //本地第一张图片
    public static String LOCAL_PIC_1 = null;
    public static String LOCAL_PIC_1_TIME = null;
    //本地第二张图片
    public static String LOCAL_PIC_2 = null;
    public static String LOCAL_PIC_2_TIME = null;
    //是否使用缓存
    public static boolean USE_CACHE;


    public static void showHomeView(Activity activity) {
        Intent intent = new Intent(activity, HomeSSPActivity.class);
        activity.startActivity(intent);
    }

}
