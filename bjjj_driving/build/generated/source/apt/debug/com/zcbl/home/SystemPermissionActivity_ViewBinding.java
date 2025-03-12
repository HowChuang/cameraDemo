// Generated code from Butter Knife. Do not modify!
package com.zcbl.home;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.zcbl.bjjj_driving.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SystemPermissionActivity_ViewBinding implements Unbinder {
  private SystemPermissionActivity target;

  @UiThread
  public SystemPermissionActivity_ViewBinding(SystemPermissionActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SystemPermissionActivity_ViewBinding(SystemPermissionActivity target, View source) {
    this.target = target;

    target.ivNet = Utils.findRequiredViewAsType(source, R.id.iv_net, "field 'ivNet'", ImageView.class);
    target.ivCamera = Utils.findRequiredViewAsType(source, R.id.iv_camera, "field 'ivCamera'", ImageView.class);
    target.ivVoice = Utils.findRequiredViewAsType(source, R.id.iv_voice, "field 'ivVoice'", ImageView.class);
    target.ivLocation = Utils.findRequiredViewAsType(source, R.id.iv_location, "field 'ivLocation'", ImageView.class);
    target.ivStore = Utils.findRequiredViewAsType(source, R.id.iv_storage, "field 'ivStore'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SystemPermissionActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivNet = null;
    target.ivCamera = null;
    target.ivVoice = null;
    target.ivLocation = null;
    target.ivStore = null;
  }
}
