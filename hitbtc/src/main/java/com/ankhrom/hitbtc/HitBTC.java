package com.ankhrom.hitbtc;


import com.android.volley.RequestQueue;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.hitbtc.entity.HitBalance;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class HitBTC {

    private final RequestQueue requestQueue;

    private String apiKey;
    private String apiSecret;

    public final HitApiCurrency currency;

    private HitBTC(RequestQueue requestQueue) {

        this.requestQueue = requestQueue;
        this.currency = new HitApiCurrency(requestQueue);
    }

    public static HitBTC init(RequestQueue requestQueue) {

        return new HitBTC(requestQueue);
    }

    public HitBTC auth(String key, String secret) {

        apiKey = key;
        apiSecret = secret;

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
