package com.ankhrom.etherscan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.base.networking.volley.VolleyBuilder;
import com.ankhrom.etherscan.entity.EtherResponse;

/**
 * Created by R' on 6/7/2018.
 */
public class Etherscan {

    private final RequestQueue requestQueue;

    private String address;
    private String apiKey;

    private Etherscan(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public static Etherscan init(@NonNull Context context) {
        return new Etherscan(new VolleyBuilder(context).build());
    }

    public Etherscan auth(String address, String apiKey) {

        this.address = address;
        this.apiKey = apiKey;

        return this;
    }

    public void requestBalance(final ResponseListener<Double> listener) {

        RequestBuilder.get(EtherApiUrl.BASE_URL)
                .param(EtherApiParam.module, EtherApiParam.Module.account)
                .param(EtherApiParam.action, EtherApiParam.Action.balance)
                .param(EtherApiParam.address, address)
                .param(EtherApiParam.key, apiKey)
                .listener(new ResponseListener<EtherResponse>() {
                    @Override
                    public void onResponse(@Nullable EtherResponse response) {

                        if (response == null) {
                            listener.onResponse(0.0);
                            return;
                        }

                        double value = Double.parseDouble(response.result);

                        listener.onResponse(value / EtherApiParam.VALUE_MULTIPLIER);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onErrorResponse(error);
                    }
                })
                .asGson(EtherResponse.class)
                .queue(requestQueue);
    }
}
