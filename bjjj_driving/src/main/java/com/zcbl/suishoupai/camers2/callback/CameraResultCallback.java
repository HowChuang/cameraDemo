package com.zcbl.suishoupai.camers2.callback;


public interface CameraResultCallback {
    int TAKE_PICTURE = 1;
    int TAKE_VIDEO = 2;
    int TAKE_REPAIR = 3;

    //0 照片 1 视频 2翻拍
    void getMediaData(int mediatype, String mediaPath);

    void getVideoData(String mediaPath);

}
