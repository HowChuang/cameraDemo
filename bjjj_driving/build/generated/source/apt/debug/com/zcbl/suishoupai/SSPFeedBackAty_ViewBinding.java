// Generated code from Butter Knife. Do not modify!
package com.zcbl.suishoupai;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.zcbl.bjjj_driving.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SSPFeedBackAty_ViewBinding implements Unbinder {
  private SSPFeedBackAty target;

  @UiThread
  public SSPFeedBackAty_ViewBinding(SSPFeedBackAty target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SSPFeedBackAty_ViewBinding(SSPFeedBackAty target, View source) {
    this.target = target;

    target.etContent = Utils.findRequiredViewAsType(source, R.id.et_content, "field 'etContent'", EditText.class);
    target.tvHistory = Utils.findRequiredViewAsType(source, R.id.tv_history, "field 'tvHistory'", TextView.class);
    target.tvSubmit = Utils.findRequiredViewAsType(source, R.id.tv_submit, "field 'tvSubmit'", TextView.class);
    target.areaBottom = Utils.findRequiredViewAsType(source, R.id.area_bottom, "field 'areaBottom'", LinearLayout.class);
    target.tvQuestionType = Utils.findRequiredViewAsType(source, R.id.tv_type, "field 'tvQuestionType'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SSPFeedBackAty target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etContent = null;
    target.tvHistory = null;
    target.tvSubmit = null;
    target.areaBottom = null;
    target.tvQuestionType = null;
  }
}
