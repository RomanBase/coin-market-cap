package com.ankhrom.coinmarketcap.common;

import android.Manifest;
import android.content.Context;

import com.ankhrom.base.GlobalCode;
import com.ankhrom.base.common.BaseCamera;
import com.ankhrom.base.common.BasePermission;
import com.ankhrom.base.custom.builder.ToastBuilder;

/**
 * Created by R' on 1/30/2018.
 */

public class CameraRequest {

    public static boolean isAvailable(Context context) {

        if (BaseCamera.isCameraAvailable(context)) {
            if (BasePermission.isAvailable(context, Manifest.permission.CAMERA)) {
                return true;
            } else {
                BasePermission.with(context)
                        .requestCode(GlobalCode.CAMERA_REQUEST)
                        .require(Manifest.permission.CAMERA);
            }
        } else {
            ToastBuilder.with(context)
                    .text("Sorry, there is no camera.")
                    .buildAndShow();
        }

        return false;
    }
}
