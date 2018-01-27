package com.ankhrom.coinmarketcap.prefs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ankhrom.base.custom.prefs.BasePrefs;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;
import com.ankhrom.coinmarketcap.listener.OnExchangePortfolioChangedListener;
import com.ankhrom.coinmarketcap.listener.OnPortfolioChangedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by R' on 12/30/2017.
 */

public class UserPrefs extends BasePrefs {

    private static final String PREFS = "user_prefs";
    private static final String CURRENCY = "info";
    private static final String FAVOURITES = "favourites";
    private static final String PORTFOLIO = "portfolio";

    private OnPortfolioChangedListener portfolioChangedListener;
    private OnExchangePortfolioChangedListener portfolioExchangeListener;

    public UserPrefs(@NonNull Context context) {
        super(context);
    }

    public void setPortfolioChangedListener(OnPortfolioChangedListener portfolioChangedListener) {
        this.portfolioChangedListener = portfolioChangedListener;
    }

    public void setExchangePortfolioListener(OnExchangePortfolioChangedListener portfolioExchangeListener) {
        this.portfolioExchangeListener = portfolioExchangeListener;
    }

    public void notifyPortfolioChanged(List<PortfolioCoin> portfolio) {

        if (portfolioChangedListener != null) {
            portfolioChangedListener.onPortfolioChanged(portfolio);
        }
    }

    public void notifyExchangePortfolioChanged(ExchangeType exchange) {

        if (portfolioExchangeListener != null) {
            portfolioExchangeListener.onPortfolioChanged(exchange, getPortfolio(exchange));
        }
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

    public void addFavourite(String id) {

        List<String> favs = getFavourites();
        favs.add(id);

        setFavourites(favs);
    }

    public void removeFavourite(String id) {

        List<String> favs = getFavourites();
        favs.remove(id);

        setFavourites(favs);
    }

    public void setPortfolio(List<PortfolioCoin> portfolio) {

        if (portfolio == null || portfolio.isEmpty()) {
            edit().putString(PORTFOLIO, null).apply();
        } else {
            edit().putString(PORTFOLIO, new Gson().toJson(portfolio)).apply();
        }
    }

    public List<PortfolioCoin> getPortfolio() {

        Type type = new TypeToken<List<PortfolioCoin>>() {
        }.getType();

        return new Gson().fromJson(getPrefs().getString(PORTFOLIO, DEFAULT_JSON_LIST), type);
    }

    public void addPortfolioItem(PortfolioItem item) {

        List<PortfolioCoin> portfolio = getPortfolio();

        boolean newItem = true;

        for (PortfolioCoin coin : portfolio) {
            if (coin.coinId.equals(item.coinId)) {
                coin.items.add(item);
                newItem = false;
                break;
            }
        }

        if (newItem) {
            PortfolioCoin coin = new PortfolioCoin();
            coin.coinId = item.coinId;
            coin.items = new ArrayList<>();
            coin.items.add(item);

            portfolio.add(coin);
        }

        setPortfolio(portfolio);
        notifyPortfolioChanged(portfolio);
    }

    public void updatePortfolioItem(PortfolioCoin coin) {

        List<PortfolioCoin> portfolio = getPortfolio();

        Iterator<PortfolioCoin> iterator = portfolio.iterator();
        while (iterator.hasNext()) {
            PortfolioCoin item = iterator.next();
            if (item.coinId.equals(coin.coinId)) {
                iterator.remove();
                break;
            }
        }

        if (coin.items != null && coin.items.size() > 0) {
            portfolio.add(coin);
        }

        setPortfolio(portfolio);
        notifyPortfolioChanged(portfolio);
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

    public void addPortfolioCoin(PortfolioCoin coin, ExchangeType exchange) { // TODO: 1/26/2018 performance

        List<PortfolioCoin> portfolio = getPortfolio(exchange);

        boolean updated = false;

        Iterator<PortfolioCoin> iterator = portfolio.iterator();
        while (iterator.hasNext()) {
            PortfolioCoin item = iterator.next();
            if (item.coinId.equals(coin.coinId)) {
                updated = true;
                item.items.addAll(coin.items);
                break;
            }
        }

        if (!updated) {
            portfolio.add(coin);
        }

        if (portfolioExchangeListener != null) {
            portfolioExchangeListener.onPortfolioChanged(exchange, portfolio);
        }

        setPortfolio(exchange, portfolio);
    }

    private String getExchangePrefKey(ExchangeType exchange) {

        return PORTFOLIO + "_" + exchange;
    }

    @Override
    public String getPrefsName() {
        return PREFS;
    }
}
