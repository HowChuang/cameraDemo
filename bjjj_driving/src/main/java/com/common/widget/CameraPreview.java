package com.common.widget;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * 创建日期：2020/6/19 下午12:28
 * 描述:
 * 作者: 赵伟闯
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "main";
    private SurfaceHolder mHolder;
    @SuppressWarnings("deprecation")
    private Camera mCamera;

    @SuppressWarnings("deprecation")
    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        // 通过SurfaceView获得SurfaceHolder
        mHolder = getHolder();
        // 为SurfaceHolder指定回调
        mHolder.addCallback(this);
        mCamera.setDisplayOrientation(90);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // 当Surface被创建之后，开始Camera的预览
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "预览失败");
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Surface发生改变的时候将被调用，第一次显示到界面的时候也会被调用
        if (mHolder.getSurface() == null) {
            // 如果Surface为空，不继续操作
            return;
        }

        // 停止Camera的预览
        try {
            mCamera.stopPreview();

        } catch (Exception e) {
            Log.d(TAG, "当Surface改变后，停止预览出错");
        }

        // 重新开始预览
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "预览Camera出错");
        }
    }
}

