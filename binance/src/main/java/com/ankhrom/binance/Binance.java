package com.ankhrom.binance;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.base.networking.volley.VolleyBuilder;
import com.ankhrom.binance.entity.BinAccount;

/**
 * Created by R' on 1/26/2018.
 */

public class Binance {

    private String key;
    private String secret;

    private final RequestQueue requestQueue;

    private Binance(@NonNull RequestQueue queue) {

        this.requestQueue = queue;
    }

    public static Binance init(@NonNull Context context) {

        return new Binance(new VolleyBuilder(context).build());
    }

    public Binance auth(String key, String secret) {

        this.key = key;
        this.secret = secret;

        return this;
    }

    public void getAccountData(ResponseListener<BinAccount> listener) {

        BinApi api = new BinApi(requestQueue);
        api.auth(key, secret);

        api.request(RequestBuilder.get(BinApiUrl.ACCOUNT))
                .listener(listener)
                .asGson(BinAccount.class)
                .queue(requestQueue);
    }
}
