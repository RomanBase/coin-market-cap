package com.ankhrom.coinmarketcap.prefs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ankhrom.base.custom.prefs.BasePrefs;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by R' on 12/30/2017.
 */

public class UserPrefs extends BasePrefs {

    private static final String PREFS = "user_prefs";
    private static final String CURRENCY = "info";
    private static final String FAVOURITES = "favourites";
    private static final String PORTFOLIO = "portfolio";

    public DataHolder holder;

    public UserPrefs(@NonNull Context context) {
        super(context);
    }

    public void setCurrency(String currency) {

        edit().putString(CURRENCY, currency).apply();
    }

    public String getCurrency() {

        return getPrefs().getString(CURRENCY, context.getResources().getString(R.string.default_currency));
    }

    public void setFavourites(List<String> coins) {

        if (coins == null || coins.isEmpty()) {
            edit().putString(FAVOURITES, null).apply();
        } else {
            edit().putString(FAVOURITES, new Gson().toJson(coins)).apply();
        }
    }

    public List<String> getFavourites() {

        Type type = new TypeToken<List<String>>() {
        }.getType();

        return new Gson().fromJson(getPrefs().getString(FAVOURITES, DEFAULT_JSON_LIST), type);
    }

    public void setPortfolio(ExchangeType exchange, List<PortfolioCoin> portfolio) {

        if (portfolio == null || portfolio.isEmpty()) {
            edit().putString(getExchangePrefKey(exchange), null).apply();
        } else {
            edit().putString(getExchangePrefKey(exchange), new Gson().toJson(portfolio)).apply();
        }
    }

    public List<PortfolioCoin> getPortfolio(ExchangeType exchange) {

        Type type = new TypeToken<List<PortfolioCoin>>() {
        }.getType();

        return new Gson().fromJson(getPrefs().getString(getExchangePrefKey(exchange), DEFAULT_JSON_LIST), type);
    }

    private String getExchangePrefKey(ExchangeType exchange) {

        return PORTFOLIO + "_" + exchange;
    }

    @Override
    public String getPrefsName() {
        return PREFS;
    }
}
