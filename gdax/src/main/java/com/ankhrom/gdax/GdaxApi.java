package com.ankhrom.gdax;

import android.net.Uri;

import com.android.volley.RequestQueue;
import com.ankhrom.base.common.statics.SHA256;
import com.ankhrom.base.networking.volley.RequestBuilder;

/**
 * Created by R' on 2/5/2018.
 */

public class GdaxApi {

    protected final RequestQueue requestQueue;

    protected String apiKey;
    protected String apiSecret;
    protected String pass;

    public GdaxApi(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void auth(String apiKey, String apiSecret, String pass) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.pass = pass;
    }

    public RequestBuilder request(RequestBuilder builder) {

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
        String path = Uri.parse(builder.getUrl()).getPath();

        builder.header(GdaxApiConst.TIMESTAMP, timestamp)
                .header(GdaxApiConst.KEY, apiKey)
                .header(GdaxApiConst.PASS, pass)
                .header(GdaxApiConst.SIGN, SHA256.HmacBase64(apiSecret, timestamp + "GET" + path + "", true));

        return builder;
    }
}
