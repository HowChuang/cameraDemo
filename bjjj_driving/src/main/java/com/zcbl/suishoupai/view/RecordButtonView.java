package com.zcbl.suishoupai.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecordButtonView extends View {
    //最大录制时间
    public static final int MAX_TIME = 20000;
    public static long VIDEO_RECORD_MINE_TIME = 5000;
    public static long VIDEO_RECORD_START_TIME;
    CallBackTime mBack;

    public RecordButtonView(Context context) {
        super(context);
        init(context);
    }

    public RecordButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecordButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private OnTimeOverListener onTimeOverListener;


    private Paint defPaint, shanArcPaint;

    private int width, height;

    private float sweepAngle = 0;


    //单位是dp
    private int borderWidth = 12;

    private void init(Context context) {


        //组件背景颜色绘制
        defPaint = new Paint();
        defPaint.setColor(Color.parseColor("#24ffffff"));
        defPaint.setAntiAlias(true);

        //进度条画笔
        shanArcPaint = new Paint();
        shanArcPaint.setAntiAlias(true);
        shanArcPaint.setStyle(Paint.Style.STROKE);
        shanArcPaint.setStrokeWidth(borderWidth);
        //添加渐变
        int[] colors = new int[]{Color.parseColor("#FF1DE8FC"), Color.parseColor("#FF0080FF"),Color.parseColor("#FF0080FF"),Color.parseColor("#FF1DE8FC")};
        float[] positionArray = new float[]{0f, 0.25f, 0.75f, 1.0f};
//        int[] colors = new int[]{Color.RED, Color.RED,Color.BLUE,Color.BLUE};
//        int[] colors = new int[]{Color.parseColor("#ffffff"), Color.parseColor("#000000")};
//        LinearGradient linearGradient = new LinearGradient(0, 0, getMeasuredHeight() / 2, getMeasuredHeight() / 2, colors, null, LinearGradient.TileMode.MIRROR);
        SweepGradient sweepGradient = new SweepGradient(getMeasuredHeight() / 2, getMeasuredHeight() / 2, colors, positionArray);
        shanArcPaint.setShader(sweepGradient);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0 || height == 0) {
            width = getWidth();
            height = getHeight();
        }
        //不加上圆弧 四个边界交界处会被销掉
        int padding=borderWidth/2;
        canvas.drawCircle(width / 2, height / 2, width / 2, defPaint);
//        //圆角矩形
        RectF rectF = new RectF(padding ,padding, width-padding, height-padding);
//        //进度条
        canvas.drawArc(rectF, -90, sweepAngle, false, shanArcPaint);


//        canvas.drawArc(rectF, -90, sweepAngle, true, shanArcPaint);
        //覆盖上面一个 小的圆圈用户看到的就是圆弧了
//        floatPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        floatPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
//        canvas.drawCircle(width / 2, height / 2, getCircleWidth(getContext()), floatPaint);
        //透明效果 不加上不显示，显示呈成黑色
//        setLayerType(LAYER_TYPE_HARDWARE,null);

    }


    private boolean addTimeAnim() {
        int stepCount = MAX_TIME / 10;
        float stepAngle = 360f / stepCount;
        sweepAngle += stepAngle;
        if (sweepAngle >= 360) {
            sweepAngle = 360;
            return true;
        }
        return false;
    }


    public boolean enoughRecordTime() {
        return System.currentTimeMillis() - VIDEO_RECORD_START_TIME > VIDEO_RECORD_MINE_TIME;
    }

    public void doStartAnim() {
        VIDEO_RECORD_START_TIME = System.currentTimeMillis();
        doAnim(true);
    }

    public void doStopAnim() {
        doAnim(false);
    }

    private boolean isAnimStart = false;

    private long lastTime = 0;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                if (isAnimStart) {
                    if (addTimeAnim()) {
                        isAnimStart = false;//改为false，继续执行关闭动画
                    }
                } else {
                    long nowTime = Calendar.getInstance().getTimeInMillis();
                    if ((nowTime - lastTime) > 1000) {//超过1秒才能再次反馈,防止出现两次反馈
                        if (onTimeOverListener != null && enoughRecordTime()) {
                            onTimeOverListener.onTimeOver();
                        }
                        lastTime = nowTime;
                    }
                    break;
                }
                handler.sendEmptyMessage(1);
            }
        }
    };
    private ExecutorService pool = Executors.newSingleThreadExecutor();

    private void doAnim(final boolean isStart) {
        this.isAnimStart = isStart;
        if (isStart) {
            sweepAngle = 0;
            pool.execute(runnable);
        }
    }


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
            if (mBack != null) {
                mBack.callBakc();
            }
        }
    };


    public interface OnTimeOverListener {
        void onTimeOver();
    }


    public interface CallBackTime {
        void callBakc();
    }


    public void setOnTimeOverListener(OnTimeOverListener timeOverListener) {
        this.onTimeOverListener = timeOverListener;
    }

}

