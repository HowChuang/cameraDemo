package com.zcbl.suishoupai.camers2;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

import com.common.util.AppUtils;
import com.common.util.LogAppUtil;
import com.zcbl.suishoupai.camers2.callback.CameraViewTouchCallback;
import com.zcbl.suishoupai.camers2.callback.FingerScrollCallBack;

import androidx.annotation.RequiresApi;

/**
 * A {@link TextureView} that can be adjusted to a specified aspect ratio.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

public class AutoFit2TextureView extends TextureView {
    public float finger_spacing = 0;
    public int zoom_level = 1;


    CameraViewTouchCallback callback;
    FingerScrollCallBack scrollCallBack;
    private float mPosX, mPosY;
    private float mCurPosX, mCurPosY;
    private float scrollXDistance = 255;
    private float scrollYDistance = 225;
    // 录制时缩放 不采取画面 只能关闭
    private boolean noTouch;


    private long fingerTwoTime;
    //关闭切换菜单功能
    private boolean closeLeftRight;
    private int mRatioWidth;
    private int mRatioHeight;
    private View mFouceView;
    private long mClickOn;
    private RelativeLayout mRelativeFloatView;

    private int mWidth;
    private int mHeight;

    public AutoFit2TextureView(Context context) {
        this(context, null);
    }

    public AutoFit2TextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFit2TextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTouchCallback(CameraViewTouchCallback c) {
        this.callback = c;
    }


    public void setTouchCallback(FingerScrollCallBack c) {
        this.scrollCallBack = c;
    }





    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (noTouch) {
            return false;
        }
        if (event.getPointerCount() == 2) {
            fingerTwoTime = System.currentTimeMillis();
        }
        if (callback != null) {
            try {
                CameraCharacteristics characteristics = callback.getCameraCharacteristics();

                float maxzoom = (characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM)) * 10;

                Rect m = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                float current_finger_spacing;

                if (event.getPointerCount() > 1) {
                    current_finger_spacing = getFingerSpacing(event);
                    if (finger_spacing != 0) {
                        if (current_finger_spacing > finger_spacing && maxzoom > zoom_level) {
                            zoom_level++;
                        } else if (current_finger_spacing < finger_spacing && zoom_level > 1) {
                            zoom_level--;
                        }
//                        Log.d("Common", "之前高度：" + m.height());
                        int minW = (int) (m.width() / maxzoom);
                        int minH = (int) (m.height() / maxzoom);
                        int difW = m.width() - minW;
                        int difH = m.height() - minH;
                        int cropW = difW / 100 * (int) zoom_level;
                        int cropH = difH / 100 * (int) zoom_level;
                        //取偶
                        cropW -= cropW & 3;
                        cropH -= cropH & 3;

                        int tempRight = m.width() - cropW;
                        int tempBottom = m.height() - cropH;

                        //超过这个比例 OPPO 小米 部分机型会卡住
                        if (cropW < m.height() / 2) {
//                            Log.d("Common", "缩放的宽高：" + cropW + "_" + cropH + "；限制高度_:" + m.height() / 2 + "；原始宽高：" + m.height() + "_" + m.width() + "等级：" + zoom_level);
                            Rect zoom = new Rect(cropW, cropH, tempRight, tempBottom);
                            callback.setCropRegion(zoom);
                            Log.d("Common", "允许");
                        } else {
                            Log.d("Common", "不允许");
                        }
                    }
                    finger_spacing = current_finger_spacing;
                } else {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            mClickOn = System.currentTimeMillis();

                            mPosX = event.getX();
                            mPosY = event.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            mCurPosX = event.getX();
                            mCurPosY = event.getY();

                            break;
                        case MotionEvent.ACTION_UP:

                            if (!closeLeftRight) {
                                if (System.currentTimeMillis() - fingerTwoTime > 600) {
                                    if (mCurPosX - mPosX > 0 && (Math.abs(mCurPosX - mPosX) > scrollXDistance)) {
                                        LogAppUtil.Show("向右滑动：" + ((mCurPosX - mPosX)));
                                        if (scrollCallBack != null) {
                                            scrollCallBack.scrollLeft(false);
                                        }
                                    } else if (mCurPosX>0&&mCurPosX - mPosX < 0 && (Math.abs(mCurPosX - mPosX) > scrollXDistance)) {
                                        LogAppUtil.Show("向左滑动：" + ((mCurPosX - mPosX)));
                                        if (scrollCallBack != null) {
                                            scrollCallBack.scrollLeft(true);
                                        }
                                    }
                                }
                            } else {
                                LogAppUtil.Show("关闭左右切换功能");
                            }

                            //执行聚焦动画
                            if ((System.currentTimeMillis() - mClickOn) < 500 && (Math.abs(mCurPosX - mPosX) < scrollXDistance)) {
                                moveFouces((int) event.getX(), (int) event.getY());
                            }

                            break;
                    }
                }
            } catch (CameraAccessException e) {
                throw new RuntimeException("can not access camera.", e);
            }
        }
        return true;
    }

    private float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 录制时不支持缩放 添加的此拦截
     *
     * @param noTouch
     */
    public void setCloseTouch(boolean noTouch) {
        this.noTouch = noTouch;
    }

    public void setCloseLeftRight(boolean closeLeftRight) {
        this.closeLeftRight = closeLeftRight;
    }

    /**
     * 可以点击切换左右菜单
     *
     * @return
     */
    public boolean canChangeNav() {
        return !closeLeftRight;
    }

    /**
     * 设置缩放比例
     * <p>
     * 必须是 1倍，2倍，3倍
     * <p>
     *
     * @return
     */
    public void setScaleLevel(int scale) {


        if (scale >= 3) {
            scale = 3;
        }

        if (scale <= 0) {
            scale = 1;
        }

        try {
            CameraCharacteristics characteristics = callback.getCameraCharacteristics();
            float maxzoom = (characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM)) * 10;
            Rect m = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);

            int minW = (int) (m.width() / maxzoom);
            int minH = (int) (m.height() / maxzoom);
            int difW = m.width() - minW;
            int difH = m.height() - minH;


            int maxWidth = m.height() / (2 * 3) * scale;


            zoom_level = maxWidth * 100 / difW;
            int cropW = maxWidth;
            int cropH = difH / 100 * zoom_level;

            //保持和手势缩放一致
            if (scale == 1) {
                zoom_level = 1;
                cropW = difW / 100 * (int) zoom_level;
                cropH = difH / 100 * (int) zoom_level;
            }


            //取偶
            cropW -= cropW & 3;
            cropH -= cropH & 3;

            int tempRight = m.width() - cropW;
            int tempBottom = m.height() - cropH;
//            Log.d("Common", "动态倍数：" + cropW + "_" + cropH + "；限制高度_:" + m.height() / 2 + "；原始宽高：" + m.height() + "_" + m.width() + "等级：" + zoom_level);

            //超过这个比例 OPPO 小米 部分机型会卡住
            if (cropW < m.height() / 2 + 4) {
                Rect zoom = new Rect(cropW, cropH, tempRight, tempBottom);
                callback.setCropRegion(zoom);
//                Log.d("Common", "允许");
            } else {
//                Log.d("Common", "不允许");
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


    }

    /**
     * ******************************
     * <p>
     * 以下为聚焦动画
     * <p>
     * ******************************
     */
    private FoucesAnimation mFoucesAnimation;

    /**
     * 移动焦点图标
     *
     * @param x x坐标
     * @param y y坐标
     */
    private void moveFouces(int x, int y) {
        if (mFouceView == null) {
            return;
        }
        if (mFoucesAnimation == null) {
            mFoucesAnimation = new FoucesAnimation();
        }

        mFouceView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) mFouceView.getLayoutParams();
        mFouceView.setLayoutParams(layoutParams);
        mFoucesAnimation.setDuration(500);
        mFoucesAnimation.setRepeatCount(0);
        mFoucesAnimation.setOldMargin(x, y);
        mFouceView.startAnimation(mFoucesAnimation);
        //因为是自动对焦所以不需要
//        cameraHelper.requestFocus(x, y);
    }

    public void setFloatView(RelativeLayout area_content) {
        mRelativeFloatView = area_content;
    }

    /**
     * camera 点击对焦动画
     */
    private class FoucesAnimation extends Animation {

        private int width = AppUtils.dip2px(150);
        private int W = AppUtils.dip2px(65);

        private int oldMarginLeft;
        private int oldMarginTop;

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) mFouceView.getLayoutParams();
            int w = (int) (width * (1 - interpolatedTime));
            if (w < W) {
                w = W;
            }
            layoutParams.width = w;
            layoutParams.height = w;
            if (w == W) {
                mFouceView.setLayoutParams(layoutParams);
                return;
            }
            layoutParams.leftMargin = oldMarginLeft - (w / 2);
            layoutParams.topMargin = oldMarginTop + (w / 8);
            mFouceView.setLayoutParams(layoutParams);
        }

        public void setOldMargin(int oldMarginLeft, int oldMarginTop) {
            this.oldMarginLeft = oldMarginLeft;
            this.oldMarginTop = oldMarginTop;
            removeImageFoucesRunnable();
            imageFoucesDelayedHind();
        }
    }

    private Runnable mImageFoucesRunnable = new Runnable() {
        @Override
        public void run() {
            mFouceView.setVisibility(View.GONE);
        }
    };

    /**
     * 移除对焦 消失任务
     */
    private void removeImageFoucesRunnable() {
        mFouceView.removeCallbacks(mImageFoucesRunnable);
    }

    /**
     * 添加 对焦任务
     */
    private void imageFoucesDelayedHind() {
        mFouceView.postDelayed(mImageFoucesRunnable, 500);
    }

    public void setFouceView(View view) {
        mFouceView = view;
    }

}
