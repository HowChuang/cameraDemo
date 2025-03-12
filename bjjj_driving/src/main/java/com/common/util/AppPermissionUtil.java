package com.common.util;

import android.content.Context;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

import androidx.annotation.NonNull;

public class AppPermissionUtil {


    /**
     * activity中获取权限
     */
    public static void requestPermission(final Context mContext, final OnPermissionResult onPermissionResult,
                                         final int code_permission,
                                         final String... permissions) {
        AndPermission.with(mContext)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale()) //rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
//                        LogUtil.e("guoTag", " onSucceed : " + code_permission);
                        onPermissionResult.onGranted(code_permission, permissions);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(mContext, permissions)) {
                            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                            onPermissionResult.onAlwaysDenied(code_permission, permissions);
                        } else {
//                            LogUtil.e("guoTag", " onFailed : " + code_permission);
                            onPermissionResult.onDenied(code_permission, permissions);
                        }
                    }
                })
                .start();

    }

    public interface OnPermissionResult {
        //授权成功
        void onGranted(int requestCode, List<String> permissions);

        //授权失败
        void onDenied(int requestCode, List<String> permissions);

        //授权失败,选择了不在提示选项
        void onAlwaysDenied(int requestCode, List<String> permissions);


    }


    public static final int CODE_PERMISSION_MULTI_IMAGE = 101;
    public static final int CODE_PERMISSION_MULTI_VIDEO = 102;
    public static final int CODE_PERMISSION_MULTI_LOCATION = 103;
    //读取设备码专用
    public static final int CODE_READ_PHONE_STATE = 1;
    //手机拨打电话权限
    public static final int CODE_REQUEST = 1002;
    //存储权限
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 3;
    //相机权限
    public static final int CODE_CAMERA = 4;

    private static class RuntimeRationale implements Rationale<List<String>> {
        /**
         * Show rationale to user.
         *
         * @param context  context.
         * @param data     the data.
         * @param executor executor.
         */
        @Override
        public void showRationale(Context context, List<String> data, RequestExecutor executor) {
            executor.execute();
        }
    }
}
