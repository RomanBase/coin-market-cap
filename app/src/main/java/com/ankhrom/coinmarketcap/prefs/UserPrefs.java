package com.ankhrom.coinmarketcap.prefs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ankhrom.base.custom.prefs.BasePrefs;
import com.ankhrom.coinmarketcap.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by R' on 12/30/2017.
 */

public class UserPrefs extends BasePrefs {

    private static final String PREFS = "user_prefs";
    private static final String CURRENCY = "currency";
    private static final String FAVOURITES = "favourites";

    public UserPrefs(@NonNull Context context) {
        super(context);
    }

    public void setCurrency(String currency) {

        edit().putString(CURRENCY, currency);
    }

    public String getCurrency() {

        return getPrefs().getString(CURRENCY, context.getResources().getString(R.string.default_currency));
    }

    public void setFavourites(List<String> coins) {

        if (coins == null || coins.isEmpty()) {
            edit().putString(FAVOURITES, null);
        } else {
            edit().putString(FAVOURITES, new Gson().toJson(coins));
        }
    }

    public List<String> getFavourites() {

        Type type = new TypeToken<List<String>>() {
        }.getType();

        return new Gson().fromJson(getPrefs().getString(FAVOURITES, DEFAULT_JSON_LIST), type);
    }

    @Override
    public String getPrefsName() {
        return PREFS;
    }
}
