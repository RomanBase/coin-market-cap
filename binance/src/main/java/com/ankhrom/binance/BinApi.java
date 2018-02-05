package com.ankhrom.binance;

import com.android.volley.RequestQueue;
import com.ankhrom.base.common.statics.SHA256;
import com.ankhrom.base.networking.volley.RequestBuilder;

/**
 * Created by R' on 1/26/2018.
 */

public class BinApi {

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
                .param(BinApiParam.SIGNATURE, SHA256.HmacHex(apiSecret, builder.queryParams()));
    }
}
