package com.zcbl.suishoupai.camers2.callback;

import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;

public interface CameraViewTouchCallback {

    CameraCharacteristics getCameraCharacteristics() throws CameraAccessException;

    void setCropRegion(Rect region);

}
