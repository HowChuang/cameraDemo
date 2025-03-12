package com.common.util;

import org.json.JSONObject;

/**
 * 创建日期：2019/4/18 上午10:12
 * 描述: 网络请求监听器
 * 使用时注意：网络请求返回数据时，界面已经销毁拦截PopupWindow展示。
 * 作者: 赵伟闯
 */
public interface NetWorkListener {
    int Action_1 = 0X1001;
    int Action_2 = 0X1002;
    int Action_3 = 0X1003;
    int Action_4 = 0X1004;
    int Action_5 = 0X1005;
    int Action_6 = 0X1006;
    int Action_7 = 0X1007;
    int Action_8 = 0X1008;
    int Action_9 = 0X1009;

    /**
     * @param url
     * @param values 请求参数键值 对出现 key~value
     */
    void getURL(int action, String url, String... values);

    /**
     * @param url
     * @param values 请求参数键值 对出现 key~value
     */
    void postURL(int action, String url, String... values);

    /**
     * 界面销毁，拦截数据异步更新，
     *
     * @param action
     * @param object
     */
    void onSuccessIntercept(int action, JSONObject object);

    /**
     * @param action 网络成功，数据异常
     */
    void onFailed(int action, String errorCode, String errorMsg);


    /**
     * @param action 网络链接失败
     */
    void onNetError(int action);

    /**
     * @param action 请求结束
     */
    void onFinish(int action);
}
