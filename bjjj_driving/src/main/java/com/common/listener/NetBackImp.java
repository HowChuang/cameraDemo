package com.common.listener;

import com.common.util.AppUtils;
import com.lzy.okgo.model.Progress;

import org.json.JSONObject;

/**
 * @author Rendy
 * 界面 网络信息状态统一监听
 */
public class NetBackImp implements NetBack {
    @Override
    public void onSuccess(JSONObject object) {

    }

    /**
     * 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程
     *
     * @param response
     */
    @Override
    public void onError(String response) {
        AppUtils.showToast(response);
    }

    /**
     * 请求网络结束后，UI线程
     */
    @Override
    public void onFinish() {

    }

    /**
     * 上传过程中的进度回调，get请求不回调，UI线程
     *
     * @param progress
     */
    @Override
    public void uploadProgress(Progress progress) {

    }

    /**
     * 下载过程中的进度回调，UI线程
     *
     * @param progress
     */
    @Override
    public void downloadProgress(Progress progress) {

    }
}
