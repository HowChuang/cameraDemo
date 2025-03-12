package com.common.listener;

import com.lzy.okgo.model.Progress;

import org.json.JSONObject;

/**
 * 创建日期：2020/8/10 上午11:05
 * 描述:
 * 作者: 赵伟闯
 */
public interface NetBack {

    void onSuccess(JSONObject object);

    /**
     * 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程
     */
    void onError(String response);

    /**
     * 请求网络结束后，UI线程
     */
    void onFinish();

    /**
     * 上传过程中的进度回调，get请求不回调，UI线程
     */
    void uploadProgress(Progress progress);

    /**
     * 下载过程中的进度回调，UI线程
     */
    void downloadProgress(Progress progress);
}
