// Generated code from Butter Knife. Do not modify!
package com.zcbl.home;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.zcbl.bjjj_driving.R;
import com.zcbl.driving_simple.util.NoScrollGridView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ServiceGuideActiity_ViewBinding implements Unbinder {
  private ServiceGuideActiity target;

  @UiThread
  public ServiceGuideActiity_ViewBinding(ServiceGuideActiity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ServiceGuideActiity_ViewBinding(ServiceGuideActiity target, View source) {
    this.target = target;

    target.gvOne = Utils.findRequiredViewAsType(source, R.id.gv_one, "field 'gvOne'", NoScrollGridView.class);
    target.gvTwo = Utils.findRequiredViewAsType(source, R.id.gv_two, "field 'gvTwo'", NoScrollGridView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ServiceGuideActiity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.gvOne = null;
    target.gvTwo = null;
  }
}
