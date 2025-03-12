package com.zcbl.suishoupai;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;
import android.widget.VideoView;

import com.common.ui.BaseActivity;
import com.common.util.AppUtils;
import com.zcbl.bjjj_driving.R;
import com.zcbl.suishoupai.view.SspLogic;


/**
 * 拍摄之后的预览
 * <p>
 * 随手拍之后的预览功能
 */
public class PlayVideoSspActivity extends BaseActivity {

    private VideoView videoView;
    private String videoPath;


    public static void launch(Activity activity, String path, String time) {
        Intent intent = new Intent(activity, PlayVideoSspActivity.class);
        intent.putExtra("filePath", path);
        intent.putExtra("time", time);
        activity.startActivity(intent);
    }

    @Override
    public void setContentView() {
        alphaActionBar();
        setContentView(R.layout.ssp_aty_preview_video);
        videoPath = getIntent().getStringExtra("filePath");
        String time = getIntent().getStringExtra("time");
        if (!TextUtils.isEmpty(time)) {
            iniTextView(R.id.tv_play_time, time);
        }
    }

    @Override
    public void findViewById() {
        videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(videoPath);
        videoView.start();
        //监听视频播放完的代码
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_play_cancle:
                SspLogic.FASR_REPORT_VIDEO_PAHT = null;
                SspLogic.showHomeView(this);
                finish();
                break;
            case R.id.tv_play_finish:
                if (videoView.getDuration() > 5000) {
                    SspLogic.FASR_REPORT_VIDEO_PAHT = videoPath;
                    finish();
                } else {
                    AppUtils.showToast("视频录制时间不能少于5秒");
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoView.isPlaying()) {
            videoView.start();
        }
    }
}
