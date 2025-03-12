package com.zcbl.suishoupai;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.ui.BaseActivity;
import com.common.util.AppUtils;
import com.common.util.ButtionFastUtils;
import com.common.util.CompressImage;
import com.common.util.ImageUrlUtils;
import com.common.util.LogAppUtil;
import com.zcbl.bjjj_driving.R;
import com.zcbl.suishoupai.camers2.AutoFit2TextureView;
import com.zcbl.suishoupai.camers2.Camera2Tools;
import com.zcbl.suishoupai.camers2.callback.CameraResultCallback;
import com.zcbl.suishoupai.camers2.callback.FingerScrollCallBack;
import com.zcbl.suishoupai.view.RecordButtonView;
import com.zcbl.suishoupai.view.SspLogic;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * 拍照 视频
 * <p>
 * 录制视频 拍照 自动对焦功能
 * <p>
 */
public class HomeSSPActivity extends BaseActivity implements
        CameraResultCallback {
    AutoFit2TextureView mainTexture;
    RecordButtonView record_button;
    private boolean supportCamera = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? true : false;
    private String mServerPicTime;
    private String mLessTime = "视频录制时间不能少于5秒";
    private String mTakePicTips = "投诉违法停车拍照间隔需10秒以上，其他违法无需等待";
    private int NOW_MODE;
    private Disposable mDisposable;

    private View currentNavView;

    private Camera2Tools mCamera2Tools;
    /**
     * 请求服务端时间是否成功
     */
    private boolean mRequestTimeSuccess = true;

    private boolean openFlash;
    private View videoFouces;

    private TextView currentScaleTv;
    private RelativeLayout area_content;

    @Override
    public void setContentView() {
        setContentView(R.layout.ssp_aty_camera_video);
    }

    @Override
    public void findViewById() {
        alphaActionBar();
        setBgNoneTitleWhite();
        ImageView imageView = getView(R.id.iv_aty_head_back);
        ImageUrlUtils.setLocalImgData(imageView, R.drawable.icon_back_bai);
        settTitle("随手拍").getPaint().setFakeBoldText(true);
        iniClickView(R.id.v_head_right_img).setVisibility(View.VISIBLE);
        iniClickView(R.id.iv_head_right).setVisibility(View.VISIBLE);
        area_content = getView(R.id.area_content);
        mainTexture = getView(R.id.video_texture);
        //设置焦点动画
        videoFouces = getView(R.id.video_fouces);
        mainTexture.setFouceView(videoFouces);
        record_button = getView(R.id.record_button);

        record_button.setOnTimeOverListener(new RecordButtonView.OnTimeOverListener() {
            @Override
            public void onTimeOver() {
                getView(R.id.tv_index_dot).post(new Runnable() {
                    @Override
                    public void run() {
                        getView(R.id.tv_index_dot).setVisibility(View.VISIBLE);
                        mCamera2Tools.stopRecord(true);
                    }
                });
            }
        });
        mainTexture.setFloatView(area_content);
        //初始化相机
        mCamera2Tools = new Camera2Tools();
        mCamera2Tools.init(this, mainTexture, this);
        if (supportCamera) {
            mainTexture.setVisibility(View.VISIBLE);
            mainTexture.setTouchCallback(new FingerScrollCallBack() {
                @Override
                public void scrollLeft(boolean directionLeft) {
                    showView(null, directionLeft);
                }
            });
        }
        //查询版本更新 执行完之后会展示 草稿箱功能
        showView(getView(R.id.tv_video), true);
        currentScaleTv = getTextView(R.id.tv_1_scal);
        //是否在黑名单


    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.area_flash:
                if (!openFlash) {
                    openFlash = true;
                    ImageUrlUtils.setImageResource(getImageView(R.id.iv_flash), R.mipmap.ssp_icon_sgd_open);
                    mCamera2Tools.openCameraFlash();
                } else {
                    openFlash = false;
                    ImageUrlUtils.setImageResource(getImageView(R.id.iv_flash), R.mipmap.ssp_icon_sgd_close);
                    mCamera2Tools.closeCameraFlash();
                }
                break;
            case R.id.tv_1_scal:
                if (currentScaleTv != null) {
                    currentScaleTv.setTextColor(getResources().getColor(R.color.white));
                }
                mainTexture.setScaleLevel(1);
                currentScaleTv = (TextView) v;
                break;
            case R.id.tv_2_scal:
                if (currentScaleTv != null) {
                    currentScaleTv.setTextColor(getResources().getColor(R.color.white));
                }
                mainTexture.setScaleLevel(2);
                currentScaleTv = (TextView) v;

                break;
            case R.id.tv_3_scal:
                if (currentScaleTv != null) {
                    currentScaleTv.setTextColor(getResources().getColor(R.color.white));
                }
                mainTexture.setScaleLevel(3);
                currentScaleTv = (TextView) v;
                break;
            //个人中心
            case R.id.v_head_right_img:
            case R.id.iv_head_right:
                break;
            //取消录制
            case R.id.area_cancle:
                mainTexture.setCloseTouch(false);
                if (ButtionFastUtils.canNext()) {
                    //结束倒计时
                    if (mDisposable != null && !mDisposable.isDisposed()) {
                        mDisposable.dispose();
                        mDisposable = null;
                    }
                    record_button.doStopAnim();
                    if (record_button.enoughRecordTime()) {
                        mCamera2Tools.stopRecord(true);
                    } else {
                        AppUtils.showToast(mLessTime);
                        mCamera2Tools.stopRecord(false);
                        getView(R.id.area_video_def).setVisibility(View.VISIBLE);
                        getView(R.id.area_video_process).setVisibility(View.GONE);
                        updateOtherView(View.VISIBLE);
                        mainTexture.setCloseLeftRight(false);
                    }
                }
                break;
            //执行拍照点击 ～ 拍照完成点击
            case R.id.area_take_pic_anim:
            case R.id.iv_finish:
                if (ButtionFastUtils.canNext()) {
                    startPicturePermission(CameraResultCallback.TAKE_PICTURE);
                }
                break;
            //设施报修 执行拍照点击 ～ 拍照完成点击
            case R.id.area_take_repair_anim:
                if (ButtionFastUtils.canNext()) {
                    startPicturePermission(CameraResultCallback.TAKE_REPAIR);
                }
                break;
            //开始录制视频 ～ 违法信息
            case R.id.area_starVideo:
                if (ButtionFastUtils.canNext()) {
                    startVideoPermission();
                }
                break;
            //重新选择拍摄照片
            case R.id.iv_pic1_close:
                if (getView(R.id.iv_pic2_close).getVisibility() == View.VISIBLE) {
                    delPic(this);
                } else {
                    mainTexture.setCloseLeftRight(false);
                    getTextView(R.id.tv_startPic).setText("需拍摄2张违法照片");
                    getTextView(R.id.tv_timer).setVisibility(View.GONE);
                    updateOtherView(View.VISIBLE);
                    defPicture();
                    cancleTakePictureTimer();
                    getImageView(R.id.iv_pic1).setOnClickListener(null);

                }
                break;
            //重新选择拍摄照片
            case R.id.iv_pic2_close:
                getImageView(R.id.iv_pic2).setOnClickListener(null);
                getView(R.id.iv_pic2_close).setTag(null);
                getView(R.id.iv_pic2_close).setVisibility(View.GONE);
                ImageUrlUtils.setRoundImageUrl(null, getImageView(R.id.iv_pic2), R.mipmap.ssp_img_bg2_select);
                getView(R.id.iv_pic2_bg).setVisibility(View.GONE);
                getView(R.id.iv_finish).setVisibility(View.GONE);
                break;
            //违法拍照一
            case R.id.iv_pic1:
                if (getImageView(R.id.iv_pic1_close).getVisibility() == View.VISIBLE) {
                    View picture2Close = getView(R.id.iv_pic2_close);
//                    PhotoPreviewActivity.launch(this, 0, (String) getImageView(R.id.iv_pic1_close).getTag(), picture2Close.getTag() == null ? null : (String) picture2Close.getTag());
                }
                break;
            //违法拍照二
            case R.id.iv_pic2:
                if (getImageView(R.id.iv_pic2_close).getVisibility() == View.VISIBLE) {
//                    PhotoPreviewActivity.launch(this, 1, (String) getImageView(R.id.iv_pic1_close).getTag(), (String) getImageView(R.id.iv_pic2_close).getTag());
                }
                break;
            //没有权限
            case R.id.area_no_camera_permission:
                startActivity(new Intent(this, SspPermissionActivity.class));
                break;
            //展示视频
            case R.id.tv_video:
                //展示拍照
            case R.id.tv_photo:
                //选择设施报修
            case R.id.tv_repair:
                if (mainTexture.canChangeNav()) {
                    showView(v, false);
                }
                break;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //设置默认没有使用缓存
        SspLogic.USE_CACHE = false;
        //相机权限判断
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            getView(R.id.area_no_camera_permission).setVisibility(View.VISIBLE);
        } else {
            getView(R.id.area_no_camera_permission).setVisibility(View.GONE);
        }
        mCamera2Tools.resume();
        //获取用户消息

    }

    @Override
    protected void onStop() {
        super.onStop();
        //录制视频时锁屏 强制关闭
        if (mCamera2Tools.isStartRecord()) {
            getView(R.id.area_cancle).performClick();
        }
        mCamera2Tools.stop();

    }

    /**
     * 拍照结束
     *
     * @param mediatype
     * @param mediaPath
     */
    @Override
    public void getMediaData(int mediatype, final String mediaPath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updatePicView(mediaPath);
                CompressImage.compress(mediaPath, 1080, 1920, 100);
            }
        });

    }

    /**
     * 录像结束
     *
     * @param mediaPath
     */
    @Override
    public void getVideoData(final String mediaPath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.d("Common", "录制结束");
                PlayVideoSspActivity.launch(HomeSSPActivity.this, mediaPath, getTextView(R.id.tv_time).getText().toString());
                finish();
            }
        });
    }

    /**
     * 拍照倒计时
     */
    private void recordCount() {
        final int count = RecordButtonView.MAX_TIME / 1000;
        mDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) {
                        return count - aLong;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        long time = count - aLong;
                        if (time < 0) {
                            time = 1;
                        }
                        if (time < 10) {
                            getTextView(R.id.tv_time).setText("0:0" + time);
                        } else {
                            getTextView(R.id.tv_time).setText("0:" + time);
                        }
                        if (getView(R.id.tv_index_dot).getVisibility() == View.VISIBLE) {
                            getView(R.id.tv_index_dot).setVisibility(View.INVISIBLE);
                        } else {
                            getView(R.id.tv_index_dot).setVisibility(View.VISIBLE);
                        }
                        updateVideo(time);
                        if (time >= count) {
                            getView(R.id.tv_index_dot).post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mDisposable != null && !mDisposable.isDisposed()) {
                                        mDisposable.dispose();
                                        mDisposable = null;
                                    }

                                }
                            });
                        }
                    }
                });
    }

    /**
     * 获取服务器时间
     */
    private void getServerTime() {
        mRequestTimeSuccess = false;

        //执行网络请求关闭其他的业务操作
        updateOtherView(View.GONE);
        getView(R.id.v_head_aty_top_back).setVisibility(View.VISIBLE);
        mainTexture.setCloseLeftRight(true);

        mainTexture.postDelayed(new Runnable() {
            @Override
            public void run() {
                onSuccess(Action_1, null);
                onFinish(Action_1);
            }
        }, 2000);
    }


    /**
     * 获取服务端时间 同时开启动画
     */
    private void startAnimationAndTime() {
        if (!AppUtils.judgeNetIsConnected(this)) {
            onNetError(Action_1);
        } else {
            if (NOW_MODE == CameraResultCallback.TAKE_PICTURE) {
                AppUtils.getRoteAnimation(getView(R.id.iv_loading_pic));
                getServerTime();
            } else if (NOW_MODE == CameraResultCallback.TAKE_VIDEO) {
                AppUtils.getRoteAnimation(getView(R.id.iv_loading_video));
                getServerTime();
            } else if (NOW_MODE == CameraResultCallback.TAKE_REPAIR) {
                AppUtils.getRoteAnimation(getView(R.id.iv_loading_repair));
                getServerTime();
            }
        }

    }


    /**
     * 获取服务端的时间之后执行的操作
     */
    private void getTimeThenTakeMedia(String temServerTime) {
        //当前界面不在显示中
        if (!mShow) {
            return;
        }
        switch (NOW_MODE) {
            //执行拍照举报
            case CameraResultCallback.TAKE_PICTURE:
                mServerPicTime = temServerTime;
                mCamera2Tools.savePhoto();
                break;
            //设备报修
            case CameraResultCallback.TAKE_REPAIR:
                mServerPicTime = temServerTime;
                mCamera2Tools.savePhoto();
                break;
            //获取服务端时间之后录像
            case CameraResultCallback.TAKE_VIDEO:
                try {
                    SspLogic.FASR_REPORT_VIDEO_PAHT_TIME = temServerTime;
                    mCamera2Tools.prepareMediaRecorder();
                    mCamera2Tools.startMediaRecorder();
                    recordCount();
                    record_button.doStartAnim();
//                    mainTexture.setCloseTouch(true);
                    getView(R.id.tv_bototm_tips).setVisibility(View.VISIBLE);
                    getView(R.id.area_video_process).setVisibility(View.VISIBLE);
                    getView(R.id.area_video_def).setVisibility(View.GONE);
                    updateOtherView(View.GONE);
                    getView(R.id.v_head_aty_top_back).setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    LogAppUtil.Show("录像异常信息：" + e.toString());
                    mCamera2Tools.stopRecord(false);
                    notSupportVideo(this);
                }
                break;
        }
    }

    /**
     * 清除动画效果 网络执行完成之后自动调用
     */
    private void clearAnimation() {
//        switch (NOW_MODE) {
//            case CameraResultCallback.TAKE_PICTURE:
        getView(R.id.iv_loading_pic).clearAnimation();
        getView(R.id.iv_loading_pic).setVisibility(View.GONE);
//                break;
//            case CameraResultCallback.TAKE_REPAIR:
        getView(R.id.iv_loading_repair).clearAnimation();
        getView(R.id.iv_loading_repair).setVisibility(View.GONE);
//                break;
//            case CameraResultCallback.TAKE_VIDEO:
//        getView(R.id.iv_video).setVisibility(View.VISIBLE);
        getView(R.id.iv_loading_video).clearAnimation();
        getView(R.id.iv_loading_video).setVisibility(View.GONE);
//                break;
//        }
    }

    @Override
    public void onSuccess(int action, JSONObject object) {
        switch (action) {
            //获取服务端时间
            case Action_1:
                mRequestTimeSuccess = true;
                getTimeThenTakeMedia("2025-01-03 05:12:11");
                break;
            //版本更新
            case Action_4:
                break;
            //用户消息
            case Action_5:

                break;
            //获取黑名单
            case Action_6:

                break;

        }

    }

    @Override
    public void onFinish(int action) {
        super.onFinish(action);
        switch (action) {
            case Action_1:
                mainTexture.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clearAnimation();
                    }
                }, 300);
                //网络没有执行成功开启其他功能
                if (!mRequestTimeSuccess) {
                    //执行网络请求关闭其他的业务操作
                    updateOtherView(View.VISIBLE);
                    getView(R.id.v_head_aty_top_back).setVisibility(View.VISIBLE);
                    mainTexture.setCloseLeftRight(false);
                }
                break;

        }
    }

    private void updateVideo(long time) {
        if (time * 1000 > RecordButtonView.VIDEO_RECORD_MINE_TIME) {
            getView(R.id.iv_cancle_top).setVisibility(View.GONE);
            getView(R.id.iv_finish_top).setVisibility(View.VISIBLE);
        } else {
            getView(R.id.iv_cancle_top).setVisibility(View.VISIBLE);
            getView(R.id.iv_finish_top).setVisibility(View.GONE);
        }
    }

    /**
     * 暂不支持定位
     *
     * @param activity
     */
    private void notSupportVideo(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        window.setContentView(R.layout.expose_hint_dialog);
        //加粗 标题
        TextView tv_title = (TextView) window.findViewById(R.id.tv_expose_hint_dialog);
        tv_title.setText("温馨提示");
        tv_title.getPaint().setFakeBoldText(true);
        //内容
        TextView tv_content = (TextView) window.findViewById(R.id.tv_content);
        tv_content.setText("该机型暂不支持视频拍摄,请选择拍照投诉");
        tv_content.setVisibility(View.VISIBLE);
        //
        TextView left_cancel = (TextView) window.findViewById(R.id.but_expose_hint_cancel);
        TextView right_cancle = (TextView) window.findViewById(R.id.but_expose_hint_confirm);
        right_cancle.setTextColor(getResources().getColor(R.color.ssp_blue));


        left_cancel.setTextColor(getResources().getColor(R.color.ssp_blue));
        left_cancel.setVisibility(View.GONE);
        window.findViewById(R.id.line_bottom).setVisibility(View.GONE);


        right_cancle.setText("确定");
        right_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showView(getView(R.id.tv_photo), false);
            }
        });
        dialog.show();
    }
    /**
     * 删除拍摄的照片
     *
     * @param activity
     */
    private void delPic(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        window.setContentView(R.layout.expose_hint_dialog);
        //加粗 标题
        TextView tv_title = (TextView) window.findViewById(R.id.tv_expose_hint_dialog);
        tv_title.setText("提示");
        tv_title.getPaint().setFakeBoldText(true);
        //内容
        TextView tv_content = (TextView) window.findViewById(R.id.tv_content);
        tv_content.setText("根据照片拍摄要求，删除该照片需要同时删除其他照片，是否删除");
        tv_content.setVisibility(View.VISIBLE);
        //
        TextView left_cancel = (TextView) window.findViewById(R.id.but_expose_hint_cancel);
        left_cancel.setText("取消");
        TextView right_cancle = (TextView) window.findViewById(R.id.but_expose_hint_confirm);
        right_cancle.setText("确定");
        right_cancle.setTextColor(getResources().getColor(R.color.color_333333));
        left_cancel.setTextColor(getResources().getColor(R.color.ssp_blue));
        left_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        right_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainTexture.setCloseLeftRight(false);
                dialog.dismiss();
                getView(R.id.iv_finish).setVisibility(View.GONE);
                getTextView(R.id.tv_startPic).setText("需拍摄2张照片");
                getTextView(R.id.tv_timer).setVisibility(View.GONE);
                updateOtherView(View.VISIBLE);
                defPicture();
                cancleTakePictureTimer();
            }
        });
        dialog.show();
        return;
    }

    private void defPicture() {
        getView(R.id.iv_pic1_close).setTag(null);
        getView(R.id.iv_pic1_close).setVisibility(View.GONE);
        getView(R.id.iv_pic2_close).setTag(null);
        getView(R.id.iv_pic2_close).setVisibility(View.GONE);
        ImageUrlUtils.setRoundImageUrl(null, getImageView(R.id.iv_pic1), R.mipmap.ssp_img_bg1_select);
        ImageUrlUtils.setRoundImageUrl(null, getImageView(R.id.iv_pic2), R.mipmap.ssp_img_bg2_default);
        getImageView(R.id.iv_pic1).setOnClickListener(null);
        getImageView(R.id.iv_pic2).setOnClickListener(null);
        getView(R.id.iv_pic2_bg).setVisibility(View.GONE);
        getView(R.id.iv_pic1_bg).setVisibility(View.GONE);

    }

    private void updateOtherView(int visibility) {
        getView(R.id.v_head_aty_top_back).setVisibility(visibility);
        getView(R.id.tv_title).setVisibility(visibility);
        getView(R.id.v_head_right_img).setVisibility(visibility);
        getView(R.id.area_bottom_nav).setVisibility(visibility);
        getView(R.id.tv_nav_index).setVisibility(visibility);

    }

    private Timer timer;
    private int mBottonIndex;

    /**
     * 录制时间统计
     */
    private void iniTimer() {
        mBottonIndex = 10;
        getTextView(R.id.tv_startPic).setText(mTakePicTips);
        getTextView(R.id.tv_startPic).setVisibility(View.VISIBLE);
        getView(R.id.tv_startPic_sanjiao).setVisibility(View.VISIBLE);

        getTextView(R.id.tv_timer).setText(mBottonIndex + "秒");
        getTextView(R.id.tv_timer).setVisibility(View.VISIBLE);
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 1000, 1000);
        }
    }

    private void cancleTakePictureTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mBottonIndex--;
            if (mBottonIndex <= 0) {
                getTextView(R.id.tv_startPic).setVisibility(View.GONE);
                getView(R.id.tv_startPic_sanjiao).setVisibility(View.GONE);
                getTextView(R.id.tv_timer).setVisibility(View.GONE);
                cancleTakePictureTimer();
            } else {
                getTextView(R.id.tv_startPic).setVisibility(View.VISIBLE);
                getView(R.id.tv_startPic_sanjiao).setVisibility(View.VISIBLE);

                getTextView(R.id.tv_startPic).setText(mTakePicTips);

                getTextView(R.id.tv_timer).setText(mBottonIndex + "秒");
                getTextView(R.id.tv_timer).setVisibility(View.VISIBLE);

            }
        }
    };

    /**
     * 填充拍照图片
     *
     * @param pathPic
     */
    public void updatePicView(String pathPic) {
        if (NOW_MODE == CameraResultCallback.TAKE_PICTURE) {
            mainTexture.setCloseLeftRight(true);
            if (getView(R.id.iv_pic1_close).getVisibility() == View.GONE) {
                iniTimer();
//                ImageUrlUtils.setRoundBorderImageUrl(pathPic, getImageView(R.id.iv_pic1));
                ImageUrlUtils.setRoundImageUrl(pathPic, getImageView(R.id.iv_pic1), R.mipmap.ssp_img_bg1_select);
                getView(R.id.iv_pic1_bg).setVisibility(View.VISIBLE);
                //临时储存数据
                getView(R.id.iv_pic1_close).setTag(pathPic);
                getView(R.id.iv_pic1_close).setVisibility(View.VISIBLE);
                ImageUrlUtils.setImageUrl(null, getImageView(R.id.iv_pic2), R.mipmap.ssp_img_bg2_select);
                SspLogic.LOCAL_PIC_1 = pathPic;
                SspLogic.LOCAL_PIC_1_TIME = mServerPicTime;
                updateOtherView(View.GONE);
                getView(R.id.v_head_aty_top_back).setVisibility(View.VISIBLE);
                getImageView(R.id.iv_pic1).setOnClickListener(this);
            } else {
                //临时储存数据
//                ImageUrlUtils.setRoundBorderImageUrl(pathPic, getImageView(R.id.iv_pic2));
                ImageUrlUtils.setRoundImageUrl(pathPic, getImageView(R.id.iv_pic2), R.mipmap.ssp_img_bg2_select);
                getView(R.id.iv_pic2_bg).setVisibility(View.VISIBLE);
                getView(R.id.iv_pic2_close).setTag(pathPic);
                getView(R.id.iv_pic2_close).setVisibility(View.VISIBLE);
                SspLogic.LOCAL_PIC_2 = pathPic;
                SspLogic.LOCAL_PIC_2_TIME = mServerPicTime;
                getImageView(R.id.iv_pic2).setOnClickListener(this);
                getView(R.id.iv_finish).setVisibility(View.VISIBLE);
            }
        } else if (NOW_MODE == CameraResultCallback.TAKE_REPAIR) {
            mainTexture.setCloseTouch(false);
            PlayPictureSspActivity.launch(HomeSSPActivity.this, pathPic, mServerPicTime);
            finish();
        }


    }

    /**
     * 拍照执行的权限
     */
    private void startPicturePermission(int type) {
        SspLogic.FASR_REPORT_VIDEO_PAHT = null;
        //照片已经拍摄完成
        if (getView(R.id.iv_pic1_close).getVisibility() == View.VISIBLE && getView(R.id.iv_pic2_close).getVisibility() == View.VISIBLE) {
            AppUtils.showToast("已经拍摄完成");
            return;
        }


//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            startActivity(new Intent(this, SspPermissionActivity.class));
//            return;
//        }
//        new LocationBaiduUtil().startLocation();
//        if (MyApplication.application.getLatitude() == 0) {
//            //没有定位服务
//            if (!AppUtils.isLocServiceEnable(this)) {
//                notAllowGbs(this);
//                return;
//            } else {
//                if (MyApplication.application.getLatitude() == 0) {
//                    AppUtils.showToast("正在获取定位信息，请稍等");
//                    return;
//                }
//            }
//        }
//
////        不在北京地区
//        if (!MyApplication.application.getCity().contains("北京")) {
//            dialogNotAllow(this);
//            return;
//        }
        NOW_MODE = type;
        startAnimationAndTime();
    }

    /**
     * 录像执行的权限
     */
    private void startVideoPermission() {
        //检测权限
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            startActivity(new Intent(this, SspPermissionActivity.class));
//            return;
//        }
//        //没有定位服务
//        new LocationBaiduUtil().startLocation();
//        if (MyApplication.application.getLatitude() == 0) {
//            //没有定位服务
//            if (!AppUtils.isLocServiceEnable(this)) {
//                notAllowGbs(this);
//                return;
//            } else {
//                if (MyApplication.application.getLatitude() == 0) {
//                    AppUtils.showToast("正在获取定位信息，请稍等");
//                    return;
//                }
//            }
//        }
//
//        //地理拦截
//        if (!MyApplication.application.getCity().contains("北京")) {
//            dialogNotAllow(this);
//            return;
//        }
        NOW_MODE = CameraResultCallback.TAKE_VIDEO;
        //需要检测当区域是否已开通
        startAnimationAndTime();
    }

    /**
     * 不在北京弹窗提醒
     *
     * @param activity
     */
    private void dialogNotAllow(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        window.setContentView(R.layout.expose_hint_dialog);
        //加粗 标题
        TextView tv_title = (TextView) window.findViewById(R.id.tv_expose_hint_dialog);
        tv_title.setText("温馨提示");
        tv_title.getPaint().setFakeBoldText(true);
        //内容
        TextView tv_content = (TextView) window.findViewById(R.id.tv_content);
        tv_content.setText("当前区域暂未开通举报，感谢\n您的支持");
        tv_content.setVisibility(View.VISIBLE);
        //
        TextView left_cancel = (TextView) window.findViewById(R.id.but_expose_hint_cancel);
        left_cancel.setText("返回");
        TextView right_cancle = (TextView) window.findViewById(R.id.but_expose_hint_confirm);
        right_cancle.setText("确定");
        right_cancle.setTextColor(getResources().getColor(R.color.ssp_blue));
        left_cancel.setTextColor(getResources().getColor(R.color.ssp_blue));

        left_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        right_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 左右手势切换 触发
     * 点击事件触发
     */
    private void showView(View view, boolean toLeft) {
        if (view != null) {
            setNavView(view);
        } else if (toLeft) {
            if (currentNavView == getView(R.id.tv_photo)) {
                setNavView(getView(R.id.tv_video));
            } else if (currentNavView == getView(R.id.tv_video)) {
                setNavView(getView(R.id.tv_repair));
            }
//往右
        } else {
            if (currentNavView == getView(R.id.tv_repair)) {
                setNavView(getView(R.id.tv_video));
            } else if (currentNavView == getView(R.id.tv_video)) {
                setNavView(getView(R.id.tv_photo));
            }
        }

    }

    /**
     * 展示拍摄的状态
     *
     * @param view
     */
    private void setNavView(View view) {
        if (currentNavView == view) {
            return;
        }
        getTextView(R.id.tv_video).setTextColor(getResources().getColor(R.color.color_999999));
        getTextView(R.id.tv_video).getPaint().setFakeBoldText(false);
        getTextView(R.id.tv_photo).setTextColor(getResources().getColor(R.color.color_999999));
        getTextView(R.id.tv_photo).getPaint().setFakeBoldText(false);
        getTextView(R.id.tv_repair).setTextColor(getResources().getColor(R.color.color_999999));
        getTextView(R.id.tv_repair).getPaint().setFakeBoldText(false);
        getTextView(R.id.tv_photo).setTextSize(15);
        getTextView(R.id.tv_repair).setTextSize(15);
        getTextView(R.id.tv_video).setTextSize(15);
        HorizontalScrollView.LayoutParams params = (HorizontalScrollView.LayoutParams) getView(R.id.area_bottom_content).getLayoutParams();
        switch (view.getId()) {
            //拍照举报
            case R.id.tv_photo:
                params.setMargins(AppUtils.dip2px(16 * 4) + AppUtils.dip2px(5), 0, 0, 0);
                getTextView(R.id.tv_photo).setTextColor(getResources().getColor(R.color.white));
                getTextView(R.id.tv_photo).getPaint().setFakeBoldText(true);
                getTextView(R.id.tv_photo).setTextSize(16);
                NOW_MODE = CameraResultCallback.TAKE_PICTURE;
                getView(R.id.area_take_pic).setVisibility(View.VISIBLE);
                getView(R.id.area_take_repair).setVisibility(View.GONE);
                getView(R.id.area_video_def).setVisibility(View.GONE);
                AppUtils.showInAnimation(getView(R.id.area_take_pic_anim));
                break;
            //视频拍摄
            case R.id.tv_video:
                params.setMargins(0, 0, 0, 0);
                getTextView(R.id.tv_video).setTextColor(getResources().getColor(R.color.white));
                getTextView(R.id.tv_video).setTextSize(16);
                getTextView(R.id.tv_video).getPaint().setFakeBoldText(true);
                //
                NOW_MODE = CameraResultCallback.TAKE_VIDEO;
                getView(R.id.area_take_pic).setVisibility(View.GONE);
                getView(R.id.area_take_repair).setVisibility(View.GONE);
                getView(R.id.area_video_def).setVisibility(View.VISIBLE);
                AppUtils.showInAnimation(getView(R.id.area_starVideo));
                break;
            //设备报修
            case R.id.tv_repair:
                params.setMargins(0, 0, AppUtils.dip2px(16 * 4) + AppUtils.dip2px(5), 0);
                getTextView(R.id.tv_repair).setTextColor(getResources().getColor(R.color.white));
                getTextView(R.id.tv_repair).getPaint().setFakeBoldText(true);
                getTextView(R.id.tv_repair).setTextSize(16);
                //
                NOW_MODE = CameraResultCallback.TAKE_REPAIR;
                getView(R.id.area_take_pic).setVisibility(View.GONE);
                getView(R.id.area_take_repair).setVisibility(View.VISIBLE);
                getView(R.id.area_video_def).setVisibility(View.GONE);
                AppUtils.showInAnimation(getView(R.id.area_take_repair_anim));
                break;
        }
        getView(R.id.area_bottom_content).setLayoutParams(params);
        currentNavView = view;
    }


}
