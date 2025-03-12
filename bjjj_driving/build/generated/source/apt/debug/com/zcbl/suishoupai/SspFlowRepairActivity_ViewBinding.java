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

public class SspFlowRepairActivity_ViewBinding implements Unbinder {
  private SspFlowRepairActivity target;

  @UiThread
  public SspFlowRepairActivity_ViewBinding(SspFlowRepairActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SspFlowRepairActivity_ViewBinding(SspFlowRepairActivity target, View source) {
    this.target = target;

    target.iv1 = Utils.findRequiredViewAsType(source, R.id.iv_1, "field 'iv1'", ImageView.class);
    target.iv2 = Utils.findRequiredViewAsType(source, R.id.iv_2, "field 'iv2'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SspFlowRepairActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.iv1 = null;
    target.iv2 = null;
  }
}
