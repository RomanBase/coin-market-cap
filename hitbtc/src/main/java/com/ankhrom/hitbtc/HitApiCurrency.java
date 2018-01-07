package com.ankhrom.hitbtc;

import com.android.volley.RequestQueue;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.hitbtc.entity.HitCurrency;
import com.ankhrom.hitbtc.entity.HitCurrencyPair;
import com.ankhrom.hitbtc.entity.HitCurrencyTicker;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by R' on 1/6/2018.
 */

public class HitApiCurrency {

    private final RequestQueue requestQueue;

    public HitApiCurrency(RequestQueue queue) {
        this.requestQueue = queue;
    }

    public void info(ResponseListener<List<HitCurrency>> listener) {

        RequestBuilder.get(HitApiUrl.CURRENCY)
                .listener(listener)
                .asGson(new TypeToken<List<HitCurrency>>() {
                }.getType())
                .queue(requestQueue);
    }

    public void info(String currencyId, ResponseListener<HitCurrency> listener) {

        RequestBuilder.get(HitApiUrl.CURRENCY + currencyId)
                .listener(listener)
                .asGson(new TypeToken<List<HitCurrency>>() {
                }.getType())
                .queue(requestQueue);
    }

    public void pair(ResponseListener<List<HitCurrencyPair>> listener) {

        RequestBuilder.get(HitApiUrl.CURRENCY_SYMBOL)
                .listener(listener)
                .asGson(new TypeToken<List<HitCurrencyPair>>() {
                }.getType())
                .queue(requestQueue);
    }

    public void pair(String pairId, ResponseListener<HitCurrencyPair> listener) {

        RequestBuilder.get(HitApiUrl.CURRENCY_SYMBOL + pairId)
                .listener(listener)
                .asGson(new TypeToken<List<HitCurrencyPair>>() {
                }.getType())
                .queue(requestQueue);
    }

    public void ticker(ResponseListener<List<HitCurrencyTicker>> listener) {

        RequestBuilder.get(HitApiUrl.CURRENCY_TICKER)
                .listener(listener)
                .asGson(new TypeToken<List<HitCurrencyTicker>>() {
                }.getType())
                .queue(requestQueue);
    }

    public void ticker(String pairId, ResponseListener<HitCurrencyTicker> listener) {

        RequestBuilder.get(HitApiUrl.CURRENCY_TICKER + pairId)
                .listener(listener)
                .asGson(new TypeToken<List<HitCurrencyTicker>>() {
                }.getType())
                .queue(requestQueue);
    }
}
