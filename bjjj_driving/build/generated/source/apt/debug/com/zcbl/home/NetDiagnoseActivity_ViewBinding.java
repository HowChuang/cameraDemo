// Generated code from Butter Knife. Do not modify!
package com.zcbl.home;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.zcbl.bjjj_driving.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NetDiagnoseActivity_ViewBinding implements Unbinder {
  private NetDiagnoseActivity target;

  @UiThread
  public NetDiagnoseActivity_ViewBinding(NetDiagnoseActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public NetDiagnoseActivity_ViewBinding(NetDiagnoseActivity target, View source) {
    this.target = target;

    target.vHeadAtyTopBack = Utils.findRequiredViewAsType(source, R.id.v_head_aty_top_back, "field 'vHeadAtyTopBack'", LinearLayout.class);
    target.tvVersion = Utils.findRequiredViewAsType(source, R.id.tv_version, "field 'tvVersion'", TextView.class);
    target.tvMobileType = Utils.findRequiredViewAsType(source, R.id.tv_mobile_type, "field 'tvMobileType'", TextView.class);
    target.tvMobileSystem = Utils.findRequiredViewAsType(source, R.id.tv_mobile_system, "field 'tvMobileSystem'", TextView.class);
    target.tvLogin = Utils.findRequiredViewAsType(source, R.id.tv_login, "field 'tvLogin'", TextView.class);
    target.tvNetBrand = Utils.findRequiredViewAsType(source, R.id.tv_net_brand, "field 'tvNetBrand'", TextView.class);
    target.tvNetStatus = Utils.findRequiredViewAsType(source, R.id.tv_net_status, "field 'tvNetStatus'", TextView.class);
    target.tvNetError = Utils.findRequiredViewAsType(source, R.id.tv_net_error, "field 'tvNetError'", TextView.class);
    target.rlMyinfoZiliao = Utils.findRequiredViewAsType(source, R.id.rl_myinfo_ziliao, "field 'rlMyinfoZiliao'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    NetDiagnoseActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.vHeadAtyTopBack = null;
    target.tvVersion = null;
    target.tvMobileType = null;
    target.tvMobileSystem = null;
    target.tvLogin = null;
    target.tvNetBrand = null;
    target.tvNetStatus = null;
    target.tvNetError = null;
    target.rlMyinfoZiliao = null;
  }
}
