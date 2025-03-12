package com.common.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.common.util.AppUtils;
import com.common.util.HttpUtils;
import com.common.util.InsideUpdate;
import com.common.util.LogAppUtil;
import com.common.util.NetWorkListener;
import com.zcbl.bjjj_driving.R;

import org.json.JSONObject;

import androidx.fragment.app.Fragment;

/**
 * Created by Rendy
 * 16/7/11
 * Todo
 * Fragment 界面基类
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener, NetWorkListener, InsideUpdate.UpdateNotify {

    protected View mContentView;
    public boolean mVisible, mInitView;
    protected Activity mActivity;
    protected LayoutInflater mInflater;
    private LoadingDialog mLoadDlg;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        InsideUpdate.addClientNotify(this);
        mInflater = inflater;
        mContentView = inflater.inflate(getLayoutId(), null);
        initView(mContentView);
        mInitView = true;
        lazyLoad();
        return mContentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mVisible = true;
            lazyLoad();
        } else {
            mVisible = false;
        }
    }

    /**
     * 想实现懒加载 重写该方法并判断条件
     * <p>
     * mVisiable==true&&mInitView==true 并判断过之前有没有执行过
     */
    protected void lazyLoad() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        InsideUpdate.removeClientNotify(this);
    }

    public abstract int getLayoutId();

    public abstract void initView(View rootView);

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
    public void postCGS(int action, String url, String... values) {
        JSONObject object = HttpUtils.getCGSParams(values);

    }
    public void postJjzDk(int action, String url, String... values) {
        JSONObject object = HttpUtils.getJjzDkJsons(values);
    }

    public <T extends View> T getView(int viewId) {
        View view = mContentView.findViewById(viewId);
        return (T) view;
    }
    public TextView getTextView(int viewId) {
        View view = mContentView.findViewById(viewId);
        return (TextView) view;
    }
    public EditText getEditView(int viewId) {
        View view = mContentView.findViewById(viewId);
        return (EditText) view;
    }

    public <T extends View> T iniClickView(int viewId) {
        View view = mContentView.findViewById(viewId);
        view.setOnClickListener(this);
        return (T) view;
    }

    public TextView iniTextView(int viewId, String value) {
        TextView view = (TextView) mContentView.findViewById(viewId);
        view.setText(value);
        return view;
    }


    /**
     * 展示loading弹窗
     */
    public void showLoadingDialog() {
        if (mLoadDlg == null) {
            mLoadDlg = new LoadingDialog(getContext(), R.style.loading_net, "请等待……");
            mLoadDlg.setCanceledOnTouchOutside(true);
            mLoadDlg.setCancelable(true);
        }
        mLoadDlg.show();
    }

    /**
     * 隐藏加载信息 dialog
     */
    public void hideLodingDialog() {
        if (mLoadDlg != null)
            mLoadDlg.dismiss();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void updateUi(int action, Object... value) {

    }

    /**
     * 界面销毁，拦截数据异步更新
     *
     * @param action
     * @param object
     */
    @Override
    public void onSuccessIntercept(int action, JSONObject object) {
        if (AppUtils.canNextAty(getActivity())) {
            onSuccess(action, object);
        } else {
            LogAppUtil.Show("Fragment:界面销毁被拦截");
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


    }

    /**
     * @param action 网络链接失败
     */
    @Override
    public void onNetError(int action) {
        //防止界面销毁依然弹出异常信息、或者PopupWindow 展示时界面已经销毁
        if (AppUtils.canNextAty(getActivity())) {
            if (AppUtils.judgeNetIsConnected(getActivity())) {
                AppUtils.showToast(getContext(), getString(R.string.network_week));
            } else {
                AppUtils.showToast(getContext(), getString(R.string.net_err));
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
