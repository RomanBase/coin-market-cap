package com.ankhrom.coincap;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.base.networking.volley.VolleyBuilder;
import com.ankhrom.coincap.entity.CapHistory;

/**
 * Created by R' on 2/1/2018.
 */

public class CoinCap {

    private final RequestQueue requestQueue;

    private CoinCap(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public static CoinCap init(@NonNull Context context) {

        return new CoinCap(new VolleyBuilder(context).build());
    }

    public void getHistory(String currency, CapHistoryTimeFrame timeFrame, ResponseListener<CapHistory> listener) {

        RequestBuilder.get(CapApiUrl.getHistoryUrl(timeFrame) + currency)
                .listener(listener)
                .asGson(CapHistory.class)
                .queue(requestQueue);
    }
}
