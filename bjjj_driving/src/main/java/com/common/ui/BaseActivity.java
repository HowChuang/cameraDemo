package com.common.ui;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.util.AppUtils;
import com.common.util.AtyManager;
import com.common.util.HttpUtils;
import com.common.util.ImageUrlUtils;
import com.common.util.InsideUpdate;
import com.common.util.LogAppUtil;
import com.common.util.NetWorkListener;
import com.zcbl.bjjj_driving.R;

import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;


/**
 * @author Rendy
 * 界面 base
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener, InsideUpdate.UpdateNotify, NetWorkListener {
    public boolean mShow;
    LoadingDialog mLoadDlg;


    @Override
    public Resources getResources() {
        //防止用户调整手机字体大小，app界面错乱
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        noActionBar();
        AtyManager.getInstance().pushActivity(this);
        InsideUpdate.addClientNotify(this);
        setContentView();
        findViewById();

    }

    public void setBgWhite() {
        TextView tvTitle = getView(R.id.tv_title_right);
        tvTitle.setTextColor(getResources().getColor(R.color.color_333333));
        TextView textView = getView(R.id.tv_title);
        textView.setTextColor(getResources().getColor(R.color.black));
        getView(R.id.title).setBackgroundColor(getResources().getColor(R.color.transparence));
        ImageView imageView = getView(R.id.iv_aty_head_back);
        ImageUrlUtils.setLocalImgData(imageView, R.mipmap.ssp_home_button_return);
    }

    public void setBgWhiteSSP() {
        TextView textView = getView(R.id.tv_title);
        textView.setTextColor(getResources().getColor(R.color.black));
        getView(R.id.title).setBackgroundResource(R.drawable.common_bg_ssp);
        ImageView imageView = getView(R.id.iv_aty_head_back);
        ImageUrlUtils.setLocalImgData(imageView, R.mipmap.ssp_home_button_return);
    }

    public void setBgNoneTitleWhite() {
        TextView textView = getView(R.id.tv_title);
        textView.setTextColor(getResources().getColor(R.color.white));
        getView(R.id.title).setBackgroundColor(getResources().getColor(R.color.transparence));
    }

    public ImageView setImageTopRight(int drawableId) {
        ImageView imageView = getView(R.id.iv_aty_head_back);
        ImageUrlUtils.setLocalImgData(imageView, drawableId);
        return imageView;
    }

    public void setHeadBgColor(int color) {
        getView(R.id.title).setBackgroundColor(getResources().getColor(color));
    }

    public abstract void setContentView();

    public abstract void findViewById();

    /**
     * 设置全屏模式 隐藏掉手机信息
     */
    private void noActionBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置透明手机状态栏
     */
    public void alphaActionBar() {
//         透明状态栏
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    View decorView = getWindow().getDecorView();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    getWindow().setStatusBarColor(Color.parseColor("#00000000"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                noActionBar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected void setColorStatus(String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (TextUtils.isEmpty(color)) {
                    //默认颜色值
                    window.setStatusBarColor(Color.parseColor("#245FED"));
                } else {
                    window.setStatusBarColor(Color.parseColor(color));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void setColorStatus(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(color));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param action
     * @param url
     * @param values 请求参数键值 对出现 key~value
     */
    @Override
    public void getURL(int action, String url, String... values) {
    }

    /**
     * @param action
     * @param url
     * @param values 请求参数键值 对出现 key~value
     */
    @Override
    public void postURL(int action, String url, String... values) {

    }


    public void postSSP(int action, String url, String... values) {
        JSONObject object = HttpUtils.getSuiShouPaiJsons(values);
    }

    public void postJjzDk(int action, String url, String... values) {
        JSONObject object = HttpUtils.getJjzDkJsons(values);
    }

    public void postJjzDk(int action, String url, JSONObject object) {
    }

    public void getSSP(int action, String url, String... values) {
        JSONObject object = HttpUtils.getSuiShouPaiJsons(values);
    }

    public void postCGS(int action, String url, String... values) {
        JSONObject object = HttpUtils.getCGSParams(values);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_head_aty_top_back:
                onBackPressed();
                break;
        }
    }

    public TextView settTitle(String value) {
        return iniTextView(R.id.tv_title, value);
    }


    @Override
    public void updateUi(int action, Object... value) {

    }


    /**
     * 展示loading弹窗
     */
    public void showLoadingDialog() {
        if (mLoadDlg == null) {
            mLoadDlg = new LoadingDialog(this, R.style.loading_net, "请等待……");
            mLoadDlg.setCanceledOnTouchOutside(true);
            mLoadDlg.setCancelable(true);
        }
        if (!mLoadDlg.isShowing()) {
            mLoadDlg.show();
        }
    }

    /**
     * 隐藏加载信息 dialog
     */
    public void hideLodingDialog() {
        if (mLoadDlg != null) {
            mLoadDlg.dismiss();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mShow = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mShow = false;
    }

    @Override
    protected void onDestroy() {
        hideLodingDialog();
        super.onDestroy();
        InsideUpdate.removeClientNotify(this);
        AtyManager.getInstance().popActivity(this);

    }


    public <T extends View> T getView(int viewId) {
        View view = findViewById(viewId);
        return (T) view;
    }

    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }

    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    public TextView getEditView(int viewId) {
        return (EditText) getView(viewId);
    }

    public <T extends View> T iniClickView(int viewId) {
        View view = getView(viewId);
        view.setOnClickListener(this);
        return (T) view;
    }


    public void iniClickView(int... viewId) {
        if (viewId != null) {
            for (int i = 0; i < viewId.length; i++) {
                View view = findViewById(viewId[i]);
                view.setOnClickListener(this);
            }
        }
    }

    public TextView iniTextView(int viewId, String value) {
        TextView view = (TextView) findViewById(viewId);
        view.setText(value);
        return view;
    }

    public EditText iniEditView(int viewId, String value) {
        EditText view = (EditText) findViewById(viewId);
        view.setText(value);
        return view;
    }

    public TextView iniTextView(int viewId, String value, int viewTag) {
        TextView view = (TextView) findViewById(viewId);
        //内容空隐藏
        View viewTemp = getView(viewTag);
        if (TextUtils.isEmpty(value)) {
            viewTemp.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        } else {
            view.setText(value);
            viewTemp.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }
        return view;
    }

    /**
     * 界面销毁，拦截线程异步更新界面,主要针对弱网情况
     *
     * @param action
     * @param object
     */
    @Override
    public void onSuccessIntercept(int action, JSONObject object) {
        if (AppUtils.canNextAty(this)) {
            onSuccess(action, object);
        } else {
            LogAppUtil.Show("Activity:界面销毁被拦截");
        }
    }

    /**
     * @param action
     * @param object 请求成功
     */
    public void onSuccess(int action, JSONObject object) {
    }

    /**
     * @param action    网络成功，数据异常
     * @param errorCode
     * @param errorMsg
     */
    @Override
    public void onFailed(int action, String errorCode, String errorMsg) {
        //防止界面销毁依然弹出异常信息
        if (AppUtils.canNextAty(this)) {
            AppUtils.showToast(this, errorMsg);
        }
    }

    /**
     * @param action 网络链接失败
     */
    @Override
    public void onNetError(int action) {
        //防止界面销毁依然弹出异常信息
        if (AppUtils.canNextAty(this)) {
            if (AppUtils.judgeNetIsConnected(this)) {
                try {
                    AppUtils.showToast(getString(R.string.network_week));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    AppUtils.showToast(getString(R.string.net_err));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param action 请求结束
     */
    @Override
    public void onFinish(int action) {
        hideLodingDialog();
    }

}
