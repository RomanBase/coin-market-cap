package com.ankhrom.base.common.statics;

import android.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by R' on 2/5/2018.
 */

public class SHA256 {

    public static final String HMAC_SIGNATURE = "HmacSha256";

    public static String HmacHex(String secret, String message) {

        try {
            Mac signature = Mac.getInstance(HMAC_SIGNATURE);
            SecretKeySpec spec = new SecretKeySpec(secret.getBytes(), HMAC_SIGNATURE);
            signature.init(spec);

            return StringHelper.bytesToHex(signature.doFinal(message.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String HmacBase64(String secret, String message, boolean decode) {

        try {
            Mac signature = Mac.getInstance(HMAC_SIGNATURE);
            SecretKeySpec spec = new SecretKeySpec(decode ? Base64.decode(secret, Base64.DEFAULT) : secret.getBytes(), HMAC_SIGNATURE);
            signature.init(spec);

            return Base64.encodeToString(signature.doFinal(message.getBytes()), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
