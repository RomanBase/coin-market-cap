package com.ankhrom.binance;

import com.android.volley.RequestQueue;
import com.ankhrom.base.networking.volley.RequestBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by R' on 1/26/2018.
 */

public class BinApi {

    public static final String SIGNATURE_TYPE = "HmacSha256";

    protected final RequestQueue requestQueue;

    protected String apiKey;
    protected String apiSecret;

    public BinApi(RequestQueue queue) {
        this.requestQueue = queue;
    }

    public void auth(String key, String secret) {

        apiKey = key;
        apiSecret = secret;
    }

    public RequestBuilder request(RequestBuilder builder) {

        return builder
                .header(BinApiParam.API_KEY, apiKey)
                .param(BinApiParam.TIMESTAMP, String.valueOf(System.currentTimeMillis() - 1000))
                .param(BinApiParam.VALID_DELAY, BinApiParam.MAX_DELAY)
                .param(BinApiParam.SIGNATURE, getSignature(builder.queryParams()));
    }

    protected String getSignature(String query) {

        try {
            Mac signature = Mac.getInstance(SIGNATURE_TYPE);
            SecretKeySpec key = new SecretKeySpec(apiSecret.getBytes(), SIGNATURE_TYPE);
            signature.init(key);

            return bytesToHex(signature.doFinal(query.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String bytesToHex(byte[] bytes) {

        final char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

}
