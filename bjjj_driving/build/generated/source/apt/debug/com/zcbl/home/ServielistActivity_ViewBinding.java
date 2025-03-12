// Generated code from Butter Knife. Do not modify!
package com.zcbl.home;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.common.widget.LoadMoreListView;
import com.zcbl.bjjj_driving.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ServielistActivity_ViewBinding implements Unbinder {
  private ServielistActivity target;

  @UiThread
  public ServielistActivity_ViewBinding(ServielistActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ServielistActivity_ViewBinding(ServielistActivity target, View source) {
    this.target = target;

    target.listView = Utils.findRequiredViewAsType(source, R.id.listView, "field 'listView'", LoadMoreListView.class);
    target.tvNodata = Utils.findRequiredViewAsType(source, R.id.tv_nodate, "field 'tvNodata'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ServielistActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.listView = null;
    target.tvNodata = null;
  }
}
