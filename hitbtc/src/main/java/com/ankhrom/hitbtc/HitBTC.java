package com.ankhrom.hitbtc;


import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.base.networking.volley.VolleyBuilder;
import com.ankhrom.hitbtc.entity.HitBalance;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class HitBTC {

    private final RequestQueue requestQueue;

    private String apiKey;
    private String apiSecret;

    public final HitApiCurrency currency;
    public final HitApiTrading trading;

    private HitBTC(RequestQueue requestQueue) {

        this.requestQueue = requestQueue;

        this.currency = new HitApiCurrency(requestQueue);
        this.trading = new HitApiTrading(requestQueue);
    }

    public static HitBTC init(@NonNull Context context) {

        return new HitBTC(new VolleyBuilder(context).build());
    }

    public HitBTC auth(String key, String secret) {

        apiKey = key;
        apiSecret = secret;

        currency.auth(key, secret);
        trading.auth(key, secret);

        return this;
    }

    public void balanceTrading(ResponseListener<List<HitBalance>> listener) {

        RequestBuilder.get(HitApiUrl.TRADING_BALANCE)
                .authBasic(apiKey, apiSecret)
                .listener(listener)
                .asGson(new TypeToken<List<HitBalance>>() {
                }.getType())
                .queue(requestQueue);
    }

    public void balanceAccount(ResponseListener<List<HitBalance>> listener) {

        RequestBuilder.get(HitApiUrl.ACCOUNT_BALANCE)
                .authBasic(apiKey, apiSecret)
                .listener(listener)
                .asGson(new TypeToken<List<HitBalance>>() {
                }.getType())
                .queue(requestQueue);
    }
}
