package com.ankhrom.hitbtc;

import com.android.volley.RequestQueue;

/**
 * Created by romanhornak on 1/9/18.
 */

public class HitApi {

    protected final RequestQueue requestQueue;

    protected String apiKey;
    protected String apiSecret;

    public HitApi(RequestQueue queue) {
        this.requestQueue = queue;
    }

    public void auth(String key, String secret) {

        apiKey = key;
        apiSecret = secret;
    }

}
