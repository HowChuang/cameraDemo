// Generated code from Butter Knife. Do not modify!
package com.zcbl.suishoupai;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.baidu.mapapi.map.MapView;
import com.zcbl.bjjj_driving.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CreateBreakLowActivity_ViewBinding implements Unbinder {
  private CreateBreakLowActivity target;

  @UiThread
  public CreateBreakLowActivity_ViewBinding(CreateBreakLowActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CreateBreakLowActivity_ViewBinding(CreateBreakLowActivity target, View source) {
    this.target = target;

    target.tvTop = Utils.findRequiredViewAsType(source, R.id.tv_top, "field 'tvTop'", TextView.class);
    target.ivAtyHeadBack = Utils.findRequiredViewAsType(source, R.id.iv_aty_head_back, "field 'ivAtyHeadBack'", ImageView.class);
    target.vHeadAtyTopBack = Utils.findRequiredViewAsType(source, R.id.v_head_aty_top_back, "field 'vHeadAtyTopBack'", LinearLayout.class);
    target.tvTitle = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'tvTitle'", TextView.class);
    target.vHeadAtyTopLine = Utils.findRequiredView(source, R.id.v_head_aty_top_line, "field 'vHeadAtyTopLine'");
    target.tvTitleRight = Utils.findRequiredViewAsType(source, R.id.tv_title_right, "field 'tvTitleRight'", TextView.class);
    target.ivHeadRight = Utils.findRequiredViewAsType(source, R.id.iv_head_right, "field 'ivHeadRight'", ImageView.class);
    target.vHeadRightImg = Utils.findRequiredViewAsType(source, R.id.v_head_right_img, "field 'vHeadRightImg'", LinearLayout.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", RelativeLayout.class);
    target.tvLocal = Utils.findRequiredViewAsType(source, R.id.tv_local, "field 'tvLocal'", TextView.class);
    target.tvTime = Utils.findRequiredViewAsType(source, R.id.tv_time, "field 'tvTime'", TextView.class);
    target.ivVideo = Utils.findRequiredViewAsType(source, R.id.iv_video, "field 'ivVideo'", ImageView.class);
    target.ivPlay = Utils.findRequiredViewAsType(source, R.id.iv_play, "field 'ivPlay'", ImageView.class);
    target.tvVideoFloat = Utils.findRequiredViewAsType(source, R.id.tv_video_float, "field 'tvVideoFloat'", TextView.class);
    target.areaVideo = Utils.findRequiredViewAsType(source, R.id.area_video, "field 'areaVideo'", RelativeLayout.class);
    target.tvPic1Float = Utils.findRequiredViewAsType(source, R.id.tv_pic1_float, "field 'tvPic1Float'", TextView.class);
    target.tvPic2Float = Utils.findRequiredViewAsType(source, R.id.tv_pic2_float, "field 'tvPic2Float'", TextView.class);
    target.areaPic = Utils.findRequiredViewAsType(source, R.id.area_pic, "field 'areaPic'", LinearLayout.class);
    target.tvBreakLow = Utils.findRequiredViewAsType(source, R.id.tv_break_low, "field 'tvBreakLow'", TextView.class);
    target.ivAgreement = Utils.findRequiredViewAsType(source, R.id.iv_agreement, "field 'ivAgreement'", ImageView.class);
    target.iv_agreement_nor = Utils.findRequiredViewAsType(source, R.id.iv_agreement_nor, "field 'iv_agreement_nor'", ImageView.class);
    target.areaAgreement = Utils.findRequiredViewAsType(source, R.id.area_agreement, "field 'areaAgreement'", LinearLayout.class);
    target.areaBottomAgreement = Utils.findRequiredViewAsType(source, R.id.area_bottom_agreement, "field 'areaBottomAgreement'", LinearLayout.class);
    target.tvSubmit = Utils.findRequiredViewAsType(source, R.id.tv_submit, "field 'tvSubmit'", TextView.class);
    target.bmapView = Utils.findRequiredViewAsType(source, R.id.maView, "field 'bmapView'", MapView.class);
    target.tvCarNo = Utils.findRequiredViewAsType(source, R.id.tv_carNo, "field 'tvCarNo'", TextView.class);
    target.etDes = Utils.findRequiredViewAsType(source, R.id.et_des, "field 'etDes'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CreateBreakLowActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvTop = null;
    target.ivAtyHeadBack = null;
    target.vHeadAtyTopBack = null;
    target.tvTitle = null;
    target.vHeadAtyTopLine = null;
    target.tvTitleRight = null;
    target.ivHeadRight = null;
    target.vHeadRightImg = null;
    target.title = null;
    target.tvLocal = null;
    target.tvTime = null;
    target.ivVideo = null;
    target.ivPlay = null;
    target.tvVideoFloat = null;
    target.areaVideo = null;
    target.tvPic1Float = null;
    target.tvPic2Float = null;
    target.areaPic = null;
    target.tvBreakLow = null;
    target.ivAgreement = null;
    target.iv_agreement_nor = null;
    target.areaAgreement = null;
    target.areaBottomAgreement = null;
    target.tvSubmit = null;
    target.bmapView = null;
    target.tvCarNo = null;
    target.etDes = null;
  }
}
