package com.ankhrom.etherscan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.base.networking.volley.VolleyBuilder;
import com.ankhrom.etherscan.entity.EtherBalance;
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

    public void requestBalance(final ResponseListener<EtherBalance> listener) {

        RequestBuilder.get(EtherApiUrl.BASE_URL)
                .param(EtherApiParam.module, EtherApiParam.Module.account)
                .param(EtherApiParam.action, EtherApiParam.Action.balance)
                .param(EtherApiParam.address, address)
                .param(EtherApiParam.key, apiKey)
                .listener(new ResponseListener<EtherResponse>() {
                    @Override
                    public void onResponse(@Nullable EtherResponse response) {

                        if (response == null) {
                            listener.onResponse(null);
                            return;
                        }

                        try {
                            double value = Double.parseDouble(response.result);

                            EtherBalance balance = new EtherBalance();
                            balance.address = address;
                            balance.balance = value / EtherApiParam.VALUE_MULTIPLIER;

                            listener.onResponse(balance);
                        } catch (Exception ex) {
                            listener.onErrorResponse(new VolleyError(ex));
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onErrorResponse(error);
                    }
                })
                .asGson(EtherResponse.class)
                .queue(requestQueue);
    }

    public void requestContract(final String contract, final ResponseListener<EtherBalance> listener) {

        RequestBuilder.get(EtherApiUrl.BASE_URL)
                .param(EtherApiParam.module, EtherApiParam.Module.account)
                .param(EtherApiParam.action, EtherApiParam.Action.balanceToken)
                .param(EtherApiParam.contact, contract)
                .param(EtherApiParam.address, address)
                .param(EtherApiParam.key, apiKey)
                .listener(new ResponseListener<EtherResponse>() {
                    @Override
                    public void onResponse(@Nullable EtherResponse response) {

                        if (response == null) {
                            listener.onResponse(null);
                            return;
                        }

                        try {
                            double value = Double.parseDouble(response.result);

                            EtherBalance balance = new EtherBalance();
                            balance.address = contract;
                            balance.balance = value / EtherApiParam.VALUE_MULTIPLIER;

                            listener.onResponse(balance);
                        } catch (Exception e) {
                            listener.onErrorResponse(new VolleyError(e));
                        }
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
