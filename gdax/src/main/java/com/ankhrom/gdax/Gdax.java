package com.ankhrom.gdax;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.ankhrom.base.Base;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.base.networking.volley.VolleyBuilder;
import com.ankhrom.gdax.entity.GdaxAccount;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by R' on 2/5/2018.
 */

public class Gdax {

    private final RequestQueue requestQueue;

    private String key;
    private String secret;
    private String pass;

    private Gdax(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public static Gdax init(@NonNull Context context) {

        return new Gdax(new VolleyBuilder(context).build());
    }

    public Gdax auth(String key, String secret, String pass) {

        this.key = key;
        this.secret = secret;
        this.pass = pass;

        return this;
    }

    public void getPortfolio(ResponseListener<String> listener) {

        GdaxApi api = new GdaxApi(requestQueue);
        api.auth(key, secret, pass);

        api.request(RequestBuilder.get("https://api.gdax.com/accounts"))
                .listener(new ResponseListener<List<GdaxAccount>>() {

                    @Override
                    public void onResponse(@Nullable List<GdaxAccount> response) {
                        Base.log(response);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
                .asGson(new TypeToken<List<GdaxAccount>>() {
                }.getType())
                .queue(requestQueue);
    }
}
