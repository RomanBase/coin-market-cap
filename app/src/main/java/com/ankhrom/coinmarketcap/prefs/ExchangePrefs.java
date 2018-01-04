package com.ankhrom.coinmarketcap.prefs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ankhrom.base.custom.prefs.BasePrefs;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.google.gson.Gson;

/**
 * Created by romanhornak on 1/4/18.
 */

public class ExchangePrefs extends BasePrefs {

    private static final String PREFS = "exchange";
    private static final String HIT_BTC = "hit_btc";

    public ExchangePrefs(@NonNull Context context) {
        super(context);
    }

    @Override
    protected String getPrefsName() {
        return PREFS;
    }

    public void setHitBtc(AuthCredentials credentials) {

        edit().putString(HIT_BTC, new Gson().toJson(credentials)).apply();
    }

    public AuthCredentials getHitBtc() {

        return new Gson().fromJson(prefs.getString(HIT_BTC, DEFAULT_JSON), AuthCredentials.class);
    }

}
