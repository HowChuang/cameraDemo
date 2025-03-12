package com.common.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.zcbl.bjjj_driving.R;

/**
 * 创建日期：2021/11/5 下午3:36
 * 描述: 网络加载等待
 * 作者: 赵伟闯
 */
public class LoadingDialog extends Dialog {

    private ImageView loadingIv;
    private TextView loadingTipTv;
    private Context context;
    private String message;
    Animation hyperspaceJumpAnimation;

    public LoadingDialog(Context context, String message) {
        super(context);
        this.context = context;
        this.message = message;
    }

    public LoadingDialog(Context context, int theme, String message) {
        super(context, theme);
        this.context = context;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        loadingIv = (ImageView) findViewById(R.id.dialog_loading_iv);
        loadingTipTv = (TextView) findViewById(R.id.dialog_loading_tv);
        loadingTipTv.setText(message);
         hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.anim_loding);
        this.setCanceledOnTouchOutside(false);//Dialog外边的View点击不可取消;
//        this.setCancelable(false);//不可用返回键取消
    }

    /**
     * Start the dialog and display it on screen.  The window is placed in the
     * application layer and opaque.  Note that you should not override this
     * method to do initialization when the dialog is shown, instead implement
     * that in {@link #onStart}.
     */
    @Override
    public void show() {
        super.show();
        loadingIv.startAnimation(hyperspaceJumpAnimation);
    }

    /**
     * Dismiss this dialog, removing it from the screen. This method can be
     * invoked safely from any thread.  Note that you should not override this
     * method to do cleanup when the dialog is dismissed, instead implement
     * that in {@link #onStop}.
     */
    @Override
    public void dismiss() {
        super.dismiss();
        loadingIv.clearAnimation();
    }
}
