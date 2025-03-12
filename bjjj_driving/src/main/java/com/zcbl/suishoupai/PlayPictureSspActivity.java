package com.zcbl.suishoupai;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.common.ui.BaseActivity;
import com.common.util.ImageUrlUtils;
import com.zcbl.bjjj_driving.R;
import com.zcbl.suishoupai.view.SspLogic;

import uk.co.senab.photoview.PhotoView;


/**
 * 拍摄之后的预览
 * <p>
 * 随手拍之后的图片预览功能
 */
public class PlayPictureSspActivity extends BaseActivity {

    private PhotoView videoView;
    private String picPath;
    private String picTime;

    public static void launch(Activity activity, String path, String time) {
        Intent intent = new Intent(activity, PlayPictureSspActivity.class);
        intent.putExtra("filePath", path);
        intent.putExtra("time", time);
        activity.startActivity(intent);

    }

    @Override
    public void setContentView() {
        alphaActionBar();
        setContentView(R.layout.ssp_aty_preview_pic);
        picPath = getIntent().getStringExtra("filePath");
        picTime = getIntent().getStringExtra("time");
    }

    @Override
    public void findViewById() {

        videoView = findViewById(R.id.imageView);
        ImageUrlUtils.setGlideImageUrl(picPath, videoView);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_play_cancle:
                onBackPressed();
                break;
            case R.id.tv_play_finish:
                SspLogic.REPAIR_PIC_1 = picPath;
                SspLogic.REPAIR_PIC_1_TIME = picTime;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        SspLogic.showHomeView(this);
        finish();
    }
}
