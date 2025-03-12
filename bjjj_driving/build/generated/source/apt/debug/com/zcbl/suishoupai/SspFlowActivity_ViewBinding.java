// Generated code from Butter Knife. Do not modify!
package com.zcbl.suishoupai;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.zcbl.bjjj_driving.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SspFlowActivity_ViewBinding implements Unbinder {
  private SspFlowActivity target;

  @UiThread
  public SspFlowActivity_ViewBinding(SspFlowActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SspFlowActivity_ViewBinding(SspFlowActivity target, View source) {
    this.target = target;

    target.iv1 = Utils.findRequiredViewAsType(source, R.id.iv_1, "field 'iv1'", ImageView.class);
    target.iv2 = Utils.findRequiredViewAsType(source, R.id.iv_2, "field 'iv2'", ImageView.class);
    target.iv3 = Utils.findRequiredViewAsType(source, R.id.iv_3, "field 'iv3'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SspFlowActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.iv1 = null;
    target.iv2 = null;
    target.iv3 = null;
  }
}
