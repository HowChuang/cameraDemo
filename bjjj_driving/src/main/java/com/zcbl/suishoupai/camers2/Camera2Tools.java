package com.zcbl.suishoupai.camers2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.Toast;

import com.common.util.AppUtils;
import com.common.util.FileUtil;
import com.common.util.LogAppUtil;
import com.zcbl.suishoupai.camers2.callback.CameraResultCallback;
import com.zcbl.suishoupai.camers2.callback.CameraViewTouchCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import static android.os.Build.VERSION_CODES.LOLLIPOP;


/**
 * 相机类
 * 适配camera2sdk
 */
@RequiresApi(LOLLIPOP)
public class Camera2Tools {
    private String TAG = getClass().getSimpleName();
    private Activity context;
    public static int PREVIEW_MINE_HEIGHT = 1000;

    private AutoFit2TextureView mMainTextureView;
    private CaptureRequest.Builder mPreviewBuilder;

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;


    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();

    static {
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }

    private CaptureRequest mPreviewRequest;

    /**
     * A {@link CameraCaptureSession } for camera preview.
     */
    private CameraCaptureSession previewSession;

    private WindowManager windowManager;

    private CameraManager cameraManager;


    private CameraDevice cameraDevice;

    private Handler backgroudHandler;

    private HandlerThread backgroudThread;

    private Size mPreviewSize;

    private ImageReader jpgReader;


    private MediaRecorder mediaRecorder;

    /**
     * 相机标号
     */
    private String cameraId;


    private boolean isRecording = false;

    private String currentVideoPath;

    private CameraResultCallback cameraResultCallback;

    private CameraViewTouchCallback cameraViewTouchCallback;

    private boolean supportCamera = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? true : false;
    private CameraCharacteristics characteristics;


    private Rect mRoom;
    private boolean mShowFlash = false;


    public Camera2Tools() {
    }

    /**
     * 1、camera2 管理工具初始化第一步
     *
     * @param ctx
     * @param textureview
     */
    public void init(Activity ctx, AutoFit2TextureView textureview, CameraResultCallback resultCallback) {
        if (supportCamera) {
            this.context = ctx;
            this.mMainTextureView = textureview;
            mMainTextureView.setSurfaceTextureListener(mTextureListener);
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            this.cameraResultCallback = resultCallback;
            //193是录制覆盖层的高度
//            PREVIEW_MINE_HEIGHT = AppUtils.getScreenHeight(context) - AppUtils.dip2px(193);
            PREVIEW_MINE_HEIGHT = AppUtils.getScreenHeight(context);
            LogAppUtil.ShowE("相机的最小宽度：" + PREVIEW_MINE_HEIGHT);
            LogAppUtil.ShowE("init");
        } else {
            LogAppUtil.ShowE("Camera2Tools.init[]不支持");
        }
    }

    public int mTextureWidth;

    private int mTextureHeight;
    /**
     * 2.初始化监听
     */
    private TextureView.SurfaceTextureListener mTextureListener = new TextureView.SurfaceTextureListener() {


        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
                                              int width, int height) {
            if (mTextureHeight == 0) {
                mTextureWidth = width;
                mTextureHeight = height;
                LogAppUtil.ShowE("预览组建的:" + width + "____" + height);
                setUpCamera(width, height);
                openCamera(cameraId);
            }

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
                                                int width, int height) {
            LogAppUtil.ShowE("改变后预览组件宽高:" + width + "____" + height);
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

    };

    /**
     * 设置相机
     *
     * @param width
     * @param height
     */
    private void setUpCamera(int width, int height) {
        //获取摄像头的管理者CameraManager
        try {
            //遍历所有摄像头
            for (String cameraIdtemp : cameraManager.getCameraIdList()) {
                characteristics = cameraManager.getCameraCharacteristics(cameraIdtemp);
                //默认打开后置摄像头
                if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    cameraId = cameraIdtemp;
                    //获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
                    StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    //注意 注意 注意 根据TextureView的尺寸设置预览尺寸,设置过一次找到最佳尺寸不能再修改了
                    if (mPreviewSize == null) {
                        mPreviewSize = Camera2Util.getMinPreSizeNew(map.getOutputSizes(SurfaceTexture.class), width, height);
                        //Texture 宽高可能已经发生改变 需要修改一下,已经做矩阵适配，不需要硬性修改
//                        if (Camera2Util.needChangeTextSize) {
//                            mMainTextureView.changeWidthAndHeight(mPreviewSize, mTextureWidth, mTextureHeight);
//                        }
                    }
                    LogAppUtil.ShowE("当前使用的相机的宽高1：" + mPreviewSize.getWidth() + "_" + mPreviewSize.getHeight());
                    //动态设置尺寸
                    configureTransform(width, height);
                    //创建数据接受监听
                    jpgReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(), ImageFormat.JPEG, 2);
                    //后期读取照片使用
                    jpgReader.setOnImageAvailableListener(jpegImageLister, backgroudHandler);
                    mediaRecorder = new MediaRecorder();
                    return;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            LogAppUtil.ShowE("Camera2Tools.setUpCamera()" + e.toString());

        }
    }

    private void openCamera(String CameraId) {
        //获取摄像头的管理者CameraManager
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        //检查权限
        try {
            //打开相机，第一个参数指示打开哪个摄像头，第二个参数stateCallback为相机的状态回调接口，第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.openCamera(CameraId, deviceCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 4、监听打开相机的回调
     */
    private CameraDevice.StateCallback deviceCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            startPreview();
            if (null != mMainTextureView) {
                configureTransform(mMainTextureView.getWidth(), mMainTextureView.getHeight());
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            LogAppUtil.ShowE("CameraDevice.onDisconnected");
            camera.close();
            cameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            LogAppUtil.ShowE("CameraDevice.onError：" + error);
            camera.close();
            cameraDevice = null;

        }
    };

    /**
     * ******************************Camera2成功打开，开始预览(startPreview)*************************
     */
    public void startPreview() {

        try {
            //关闭之前的相机应用会话
            closePreviewSession();
            //获取TextureView的SurfaceTexture，作为预览输出载体
            SurfaceTexture mSurfaceTexture = mMainTextureView.getSurfaceTexture();
            //设置TextureView的缓冲区大小
            mSurfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            //创建CaptureRequestBuilder，TEMPLATE_PREVIEW比表示预览请求
            mPreviewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Surface mSurface = new Surface(mSurfaceTexture);//获取Surface显示预览数据
            mPreviewBuilder.addTarget(mSurface);//设置Surface作为预览数据的显示界面

            //拍照自动对焦
//            mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            //预览时需不需要开启闪光灯
//            if (isLightOn) {
//                mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
//            } else {
//                mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
//            }
            //创建相机捕获会话，第一个参数是捕获数据的输出Surface列表，第二个参数是CameraCaptureSession的状态回调接口，当它创建好后会回调onConfigured方法，第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            cameraDevice.createCaptureSession(Arrays.asList(mSurface, jpgReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        //创建捕获请求
                        mPreviewRequest = mPreviewBuilder.build();
                        previewSession = session;
                        LogAppUtil.ShowE("预览创建成功：");

                        //不停的发送获取图像请求，完成连续预览
                        previewSession.setRepeatingRequest(mPreviewRequest, null, backgroudHandler);
                        initTextureTouchEvent();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogAppUtil.ShowE("预览绘话：" + e.toString());
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 6. 执行拍照
     */
    public void savePhoto() {
        try {
            CaptureRequest.Builder mCaptureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            //获取屏幕方向
            int rotation = windowManager.getDefaultDisplay().getRotation();
            mCaptureBuilder.addTarget(jpgReader.getSurface());
            mCaptureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            //锁定焦点
            mCaptureBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            //判断预览的时候是否两指缩放过,是的话需要保持当前的缩放比例
            if (mRoom != null) {
                mCaptureBuilder.set(CaptureRequest.SCALER_CROP_REGION, mRoom);
            }
            //拍照执行闪光灯
            if (mShowFlash) {
                mCaptureBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
            } else {
                mCaptureBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
            }
            CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    //拍完照unLockFocus
//                    unlockFocus();
                    try {
                        previewSession.setRepeatingRequest(mPreviewRequest, null, backgroudHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                }
            };
            if (previewSession != null) {
                previewSession.stopRepeating();
                //咔擦拍照
                previewSession.capture(mCaptureBuilder.build(), captureCallback, null);
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 7、 获取拍照并存储
     */
    private ImageReader.OnImageAvailableListener jpegImageLister = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            LogAppUtil.Show("保存照片");
            Image image = reader.acquireLatestImage();
            File file = new File(FileUtil.getNoFilePath());
            LogAppUtil.Show("拍照文件路径：" + file.getAbsolutePath());
            new Thread(new SavePicRunnable(image, file, 0, cameraResultCallback)).start();
        }
    };


    /**
     * 屏幕方向发生改变时调用转换数据方法
     *
     * 如果你为了相机的清晰度选择了大于TextureView 的宽高，注意要进行变小缩放
     *
     * @param viewWidth  mTextureView 的宽度
     * @param viewHeight mTextureView 的高度
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == mMainTextureView || null == mPreviewSize) {
            return;
        }
        //屏幕方向
        int rotation = windowManager.getDefaultDisplay().getRotation();
        final Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        //相机宽度小于界面高度时，使用矩阵缩放适配
        if(mPreviewSize.getWidth() < viewHeight) {
            LogAppUtil.ShowE(TAG + "矩阵缩放");
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max((float) viewHeight / mPreviewSize.getWidth(), (float) viewWidth / mPreviewSize.getHeight());
            //设置缩放
            matrix.postScale(scale, scale, centerX, centerY);
            //设置旋转角度
            //matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else {
            LogAppUtil.ShowE(TAG + "没有执行");
        }
        mMainTextureView.post(new Runnable() {
            @Override
            public void run() {
                mMainTextureView.setTransform(matrix);
            }
        });
    }

    /**
     *
     * **************************************关闭视频状态************************************************
     */
    /**
     * public boolean getIsRecorde() {
     * return isRecording;
     * }
     * <p>
     * /**
     * stop record
     *
     * @throws CameraAccessException
     * @ save true save to local
     * false delete the short video and restart preview
     */
    public void stopRecord(boolean save) {
        if (supportCamera) {
            try {
                isRecording = false;
                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                    mediaRecorder.reset();
                    mediaRecorder.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mediaRecorder = null;
            }
            if (save) {
                cameraResultCallback.getVideoData(getCurrentVideoPath());
            } else {
                stop();
                resume();
            }
        }


    }


    private void closePreviewSession() {
        try {
            if (previewSession != null) {
                previewSession.close();
                previewSession = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        if (supportCamera) {
            try {
                if (isRecording) {
                    if (mediaRecorder != null) {
                        mediaRecorder.stop();
                        mediaRecorder.reset();
                        mediaRecorder.release();
                    }
                }
                closePreviewSession();
                cameraDevice.close();
                if (jpgReader != null) {
                    jpgReader.close();
                    jpgReader = null;
                }
                LogAppUtil.ShowE("当前相机已经关闭");
            } catch (Exception e) {
                e.printStackTrace();
                LogAppUtil.ShowE("相机关闭异常：" + e.toString());

            }
        }
    }


    /**
     * 获取当前录制视频存储地址
     *
     * @return
     */
    public String getCurrentVideoPath() {
        return currentVideoPath;
    }


    /**
     * ***************录像相关方法*************
     */

    private void setUpMediaRecorder() {
        try {
            mediaRecorder.reset();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            // 录制出来10S的视频，大概1.2M，清晰度不错，而且避免了因为手动设置参数导致无法录制的情况
            // 手机一般都有这个格式CamcorderProfile.QUALITY_480P,因为单单录制480P的视频还是很大的，所以我们在手动根据预览尺寸配置一下videoBitRate,值越高越大
            // QUALITY_QVGA清晰度一般，不过视频很小，一般10S才几百K
            // 判断有没有这个手机有没有这个参数
//            if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P))


            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//            int bitRate = mPreviewSize.getWidth() * mPreviewSize.getHeight();
//            bitRate = mPreviewSize.getWidth() < 1080 ? bitRate * 2 : bitRate;
//            mediaRecorder.setVideoEncodingBitRate(bitRate);

            int tempBitRate = 10000000;
            int bitRate = mPreviewSize.getWidth() * mPreviewSize.getHeight();
            bitRate = bitRate < tempBitRate ? tempBitRate : bitRate;
            mediaRecorder.setVideoEncodingBitRate(bitRate);
            LogAppUtil.ShowE("视频录制码率：" + bitRate);
            LogAppUtil.ShowE("视频宽高：" + mPreviewSize.getWidth() + "___" + mPreviewSize.getHeight());


            mediaRecorder.setVideoFrameRate(30);

            mediaRecorder.setVideoSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
//            设置录制质量
//            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));
//            mediaRecorder.setVideoSize(mTextureHeight, mTextureWidth);

            //判断有没有配置过视频地址了
            currentVideoPath = FileUtil.getNoFilePathMp4();
            mediaRecorder.setOutputFile(currentVideoPath);
            //判断是不是前置摄像头,是的话需要旋转对应的角度
//            if (isCameraFront) {
//                mediaRecorder.setOrientationHint(270);
//            } else {
            mediaRecorder.setOrientationHint(90);
//            }
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //预览录像
    public void prepareMediaRecorder() {

        try {
            closePreviewSession();
            setUpMediaRecorder();

            SurfaceTexture texture = mMainTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();

            // Set up Surface for the camera preview
            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            mPreviewBuilder.addTarget(previewSurface);

            // Set up Surface for the MediaRecorder
            Surface recorderSurface = mediaRecorder.getSurface();
            surfaces.add(recorderSurface);
            mPreviewBuilder.addTarget(recorderSurface);


            //录像自动对焦
            mPreviewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);

            //开启摄像头
            if (mShowFlash) {
                //闪光灯常亮
                mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
            } else {
                mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
            }

            //缩放大小
            if (mRoom != null) {
                mPreviewBuilder.set(CaptureRequest.SCALER_CROP_REGION, mRoom);
            }

            cameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        //创建捕获请求
                        mPreviewRequest = mPreviewBuilder.build();
                        previewSession = session;
                        //设置反复捕获数据的请求，这样预览界面就会一直有数据显示
                        previewSession.setRepeatingRequest(mPreviewRequest, null, backgroudHandler);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    Toast.makeText(context, "onConfigureFailed", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //开始录像
    public void startMediaRecorder() {
        // Start recording
        try {


            mediaRecorder.start();
            isRecording = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        if (mTextureWidth > 0) {
            mMainTextureView.zoom_level = 1;
            setUpCamera(mTextureWidth, mTextureHeight);
            openCamera(cameraId);
        }
    }

    private void initTextureTouchEvent() {
        LogAppUtil.ShowE("初始化手势监听器");
        cameraViewTouchCallback = new CameraViewTouchCallback() {
            @Override
            public CameraCharacteristics getCameraCharacteristics() throws CameraAccessException {
                return cameraManager.getCameraCharacteristics(cameraId);
            }

            @Override
            public void setCropRegion(Rect region) {
                mRoom = region;
                if (mPreviewBuilder != null) {
                    mPreviewBuilder.set(CaptureRequest.SCALER_CROP_REGION, region);
                    try {
                        previewSession.setRepeatingRequest(mPreviewBuilder.build(), null, backgroudHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        mMainTextureView.setTouchCallback(cameraViewTouchCallback);
    }

    public boolean isStartRecord() {
        return isRecording;
    }

    /**
     * 拍照开启闪光灯、录制开启补光灯
     */
    public void openCameraFlash() {
        mShowFlash = true;
        if (isRecording && mPreviewBuilder != null) {
            mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
            try {
                previewSession.setRepeatingRequest(mPreviewBuilder.build(), null, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 拍照开启闪光灯、录制开启补光灯
     */
    public void closeCameraFlash() {
        mShowFlash = false;
        if (isRecording && mPreviewBuilder != null) {
            mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
            try {
                previewSession.setRepeatingRequest(mPreviewBuilder.build(), null, backgroudHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }


}
