// Generated code from Butter Knife. Do not modify!
package com.zcbl.suishoupai;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.common.widget.HackyViewPager;
import com.common.widget.LineImageValue;
import com.zcbl.bjjj_driving.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PhotoPreviewActivity_ViewBinding implements Unbinder {
  private PhotoPreviewActivity target;

  @UiThread
  public PhotoPreviewActivity_ViewBinding(PhotoPreviewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PhotoPreviewActivity_ViewBinding(PhotoPreviewActivity target, View source) {
    this.target = target;

    target.ivAtyHeadBack = Utils.findRequiredViewAsType(source, R.id.iv_aty_head_back, "field 'ivAtyHeadBack'", ImageView.class);
    target.vHeadAtyTopBack = Utils.findRequiredViewAsType(source, R.id.v_head_aty_top_back, "field 'vHeadAtyTopBack'", LinearLayout.class);
    target.li1 = Utils.findRequiredViewAsType(source, R.id.li_1, "field 'li1'", LineImageValue.class);
    target.li2 = Utils.findRequiredViewAsType(source, R.id.li_2, "field 'li2'", LineImageValue.class);
    target.tvBack = Utils.findRequiredViewAsType(source, R.id.tv_back, "field 'tvBack'", TextView.class);
    target.previewVp = Utils.findRequiredViewAsType(source, R.id.photo_preview_vp, "field 'previewVp'", HackyViewPager.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PhotoPreviewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivAtyHeadBack = null;
    target.vHeadAtyTopBack = null;
    target.li1 = null;
    target.li2 = null;
    target.tvBack = null;
    target.previewVp = null;
  }
}
