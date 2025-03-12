// Generated code from Butter Knife. Do not modify!
package com.zcbl.suishoupai;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.baidu.mapapi.map.MapView;
import com.zcbl.bjjj_driving.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MapSspActivity_ViewBinding implements Unbinder {
  private MapSspActivity target;

  @UiThread
  public MapSspActivity_ViewBinding(MapSspActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MapSspActivity_ViewBinding(MapSspActivity target, View source) {
    this.target = target;

    target.bmapView = Utils.findRequiredViewAsType(source, R.id.bmapView, "field 'bmapView'", MapView.class);
    target.listView = Utils.findRequiredViewAsType(source, R.id.listView, "field 'listView'", ListView.class);
    target.editText = Utils.findRequiredViewAsType(source, R.id.et_1, "field 'editText'", EditText.class);
    target.area_tips = Utils.findRequiredView(source, R.id.area_tips, "field 'area_tips'");
  }

  @Override
  @CallSuper
  public void unbind() {
    MapSspActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.bmapView = null;
    target.listView = null;
    target.editText = null;
    target.area_tips = null;
  }
}
