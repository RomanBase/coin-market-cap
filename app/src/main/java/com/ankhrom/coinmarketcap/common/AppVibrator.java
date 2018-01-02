package com.ankhrom.coinmarketcap.common;

import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;

/**
 * Created by R' on 1/2/2018.
 */

public final class AppVibrator {

    public static void itemActivated(@NonNull Context context) {

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(75);
        }
    }
}
