package com.ankhrom.coinmarketcap.prefs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ankhrom.base.custom.prefs.BasePrefs;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.ankhrom.coinmarketcap.listener.OnExchangeAuthChangedListener;
import com.google.gson.Gson;

/**
 * Created by romanhornak on 1/4/18.
 */

public class ExchangePrefs extends BasePrefs {

    private static final String PREFS = "exchange";

    private OnExchangeAuthChangedListener listener;

    public ExchangePrefs(@NonNull Context context) {
        super(context);
    }

    @Override
    protected String getPrefsName() {
        return PREFS;
    }

    public void setAuth(ExchangeType type, @Nullable AuthCredentials credentials) {

        if (listener != null) {
            listener.onExchangeAuthChanged(type, credentials);
        }

        if (credentials != null && !credentials.persist) {
            credentials = null;
        }

        if (credentials == null) {
            edit().putString(type.name(), null).apply();
            return;
        }

        edit().putString(type.name(), new Gson().toJson(credentials)).apply();
    }

    public AuthCredentials getAuth(ExchangeType type) {

        return new Gson().fromJson(getPrefs().getString(type.name(), DEFAULT_JSON), AuthCredentials.class);
    }

}
