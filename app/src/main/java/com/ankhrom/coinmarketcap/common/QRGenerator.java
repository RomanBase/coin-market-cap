package com.ankhrom.coinmarketcap.common;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * Created by R' on 1/27/2018.
 */

public final class QRGenerator {

    public static Bitmap toBitmap(String data, int size) {

        BitMatrix result;

        try {
            result = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, size, size, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];

        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, size, 0, 0, w, h);

        return bitmap;
    }
}
