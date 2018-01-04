package com.ankhrom.hitbtc;


import android.util.Base64;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.ankhrom.base.Base;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.hitbtc.entity.HitCurrency;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class HitBTC {

    private final RequestQueue requestQueue;

    private final String ApiKey = "252d13df5fb7d277c6c6de185c18bb65";
    private final String ApiSecret = "6c60036d5a2655be6e988af8fa385be0";

    private HitBTC(RequestQueue requestQueue) {

        this.requestQueue = requestQueue;

        balance();
    }

    public static HitBTC init(RequestQueue requestQueue) {

        return new HitBTC(requestQueue);
    }

    public void currency(ResponseListener<List<HitCurrency>> listener) {

        RequestBuilder.get(HitApiUrl.CURRENCY)
                .listener(listener)
                .asGson(new TypeToken<List<HitCurrency>>() {
                }.getType())
                .queue(requestQueue);
    }

    public void currency(String currencyId, ResponseListener<HitCurrency> listener) {

        RequestBuilder.get(HitApiUrl.CURRENCY + currencyId)
                .listener(listener)
                .asGson(new TypeToken<List<HitCurrency>>() {
                }.getType())
                .queue(requestQueue);
    }

    public void balance() {

        RequestBuilder.get(HitApiUrl.BALANCE)
                .authBasic(ApiKey, ApiSecret)
                .listener(new ResponseListener() {
                    @Override
                    public void onResponse(Object response) {
                        Base.log(response);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
                .asString()
                .queue(requestQueue);
    }
}
