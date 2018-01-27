package com.ankhrom.coinmarketcap.prefs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

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

    public void setExchangeAuthListener(OnExchangeAuthChangedListener listener) {
        this.listener = listener;
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
            edit().putString(Base64.encodeToString(type.name().getBytes(), Base64.DEFAULT), null).apply();
            return;
        }

        edit().putString(Base64.encodeToString(type.name().getBytes(), Base64.DEFAULT), Base64.encodeToString(new Gson().toJson(credentials).getBytes(), Base64.DEFAULT)).apply();
    }

    public AuthCredentials getAuth(ExchangeType type) {

        return new Gson().fromJson(new String(Base64.decode(getPrefs().getString(Base64.encodeToString(type.name().getBytes(), Base64.DEFAULT), Base64.encodeToString(DEFAULT_JSON.getBytes(), Base64.DEFAULT)), Base64.DEFAULT)), AuthCredentials.class);
    }

    public void setTimestamp(ExchangeType type, long timestamp) {

        edit().putLong(type.name(), timestamp).apply();
    }

    public long getTimestamp(ExchangeType type) {

        return getPrefs().getLong(type.name(), -1);
    }
}
