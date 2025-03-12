package com.zcbl.suishoupai;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;

import com.common.ui.BaseActivity;
import com.common.util.AppPermissionUtil;
import com.common.util.AppUtils;
import com.common.util.ImageUrlUtils;
import com.zcbl.bjjj_driving.R;
import com.zcbl.manager.MyApplication;

import java.util.List;

import androidx.core.content.ContextCompat;


/**
 * 创建日期：2020/5/19 下午3:41
 * 描述: 举报帮助界面
 * 作者: 赵伟闯
 */
public class SspPermissionActivity extends BaseActivity {

    ImageView ivNet;
    ImageView ivCamera;
    ImageView ivVoice;
    ImageView ivStore;

    @Override
    public void setContentView() {
        alphaActionBar();
        setContentView(R.layout.ssp_aty_permission);
        ivNet = getView(R.id.iv_net);
        ivCamera = getView(R.id.iv_camera);
        ivVoice = getView(R.id.iv_voice);
        ivStore = getView(R.id.iv_storage);
        updatePic();
    }

    private void updatePic() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ImageUrlUtils.setImageResource(ivCamera, R.mipmap.icon_no_power);
        } else {
            ImageUrlUtils.setImageResource(ivCamera, R.mipmap.icon_power_1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ImageUrlUtils.setImageResource(ivNet, R.mipmap.icon_no_power);
        } else {
            ImageUrlUtils.setImageResource(ivNet, R.mipmap.icon_power_1);

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ImageUrlUtils.setImageResource(ivVoice, R.mipmap.icon_no_power);

        } else {
            ImageUrlUtils.setImageResource(ivVoice, R.mipmap.icon_power_1);
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ImageUrlUtils.setImageResource(ivStore, R.mipmap.icon_no_power);

        } else {
            ImageUrlUtils.setImageResource(ivStore, R.mipmap.icon_power_1);

        }

    }

    @Override
    public void findViewById() {
        settTitle("随手拍");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.area_net:
                AppPermissionUtil.requestPermission(this, new AppPermissionUtil.OnPermissionResult() {
                    @Override
                    public void onGranted(int requestCode, List<String> permissions) {
                        updatePic();
                    }

                    @Override
                    public void onDenied(int requestCode, List<String> permissions) {
                        updatePic();
                    }

                    @Override
                    public void onAlwaysDenied(int requestCode, List<String> permissions) {
                        updatePic();

                    }
                }, AppPermissionUtil.CODE_WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE);

                break;
            case R.id.area_camera:
                AppPermissionUtil.requestPermission(this, new AppPermissionUtil.OnPermissionResult() {
                    @Override
                    public void onGranted(int requestCode, List<String> permissions) {
                        updatePic();
                    }

                    @Override
                    public void onDenied(int requestCode, List<String> permissions) {
                        updatePic();

                    }

                    @Override
                    public void onAlwaysDenied(int requestCode, List<String> permissions) {
                        updatePic();

                    }
                }, AppPermissionUtil.CODE_WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

                break;
            case R.id.area_voice:
                AppPermissionUtil.requestPermission(this, new AppPermissionUtil.OnPermissionResult() {
                    @Override
                    public void onGranted(int requestCode, List<String> permissions) {
                        updatePic();

                    }

                    @Override
                    public void onDenied(int requestCode, List<String> permissions) {
                        updatePic();

                    }

                    @Override
                    public void onAlwaysDenied(int requestCode, List<String> permissions) {
                        updatePic();

                    }
                }, AppPermissionUtil.CODE_WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO);
                break;
            case R.id.area_storage:
                AppPermissionUtil.requestPermission(this, new AppPermissionUtil.OnPermissionResult() {
                    @Override
                    public void onGranted(int requestCode, List<String> permissions) {
                        updatePic();

                    }

                    @Override
                    public void onDenied(int requestCode, List<String> permissions) {
                        updatePic();

                    }

                    @Override
                    public void onAlwaysDenied(int requestCode, List<String> permissions) {
                        updatePic();
                    }
                }, AppPermissionUtil.CODE_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

                break;
            case R.id.tv_start:
                startActivity(new Intent(this, HomeSSPActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePic();
    }
}
