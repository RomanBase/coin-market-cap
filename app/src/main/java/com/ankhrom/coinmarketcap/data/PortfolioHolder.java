package com.ankhrom.coinmarketcap.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ankhrom.base.interfaces.ObjectFactory;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;
import com.ankhrom.coinmarketcap.listener.OnExchangePortfolioChangedListener;
import com.ankhrom.coinmarketcap.listener.OnFavouriteCoinStateChangedListener;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by R' on 2/7/2018.
 */

public class PortfolioHolder {

    private Map<ExchangeType, List<PortfolioCoin>> portfolio;

    private OnExchangePortfolioChangedListener portfolioExchangeListener;
    private OnFavouriteCoinStateChangedListener favouriteCoinChangedListener;

    private final UserPrefs prefs;

    private PortfolioHolder(UserPrefs prefs) {
        this.prefs = prefs;

        portfolio = new HashMap<>();

        for (ExchangeType exchange : ExchangeType.values()) {
            portfolio.put(exchange, prefs.getPortfolio(exchange));
        }
    }

    public static PortfolioHolder init(ObjectFactory factory) {

        return new PortfolioHolder(factory.get(UserPrefs.class));
    }

    public void addPortfolioItem(PortfolioItem item) {

        List<PortfolioCoin> portfolio = getExchange(item.exchange);

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

        this.portfolio.put(item.exchange, portfolio);
    }

    public void updatePortfolioCoin(PortfolioCoin coin, ExchangeType exchange) {

        List<PortfolioCoin> portfolio = getExchange(exchange);

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

        this.portfolio.put(exchange, portfolio);
    }

    public void addPortfolioCoin(PortfolioCoin coin, ExchangeType exchange) {

        List<PortfolioCoin> portfolio = getExchange(exchange);

        boolean updated = false;

        for (PortfolioCoin item : portfolio) {
            if (item.coinId.equals(coin.coinId)) {
                item.items.addAll(coin.items);
                updated = true;
                break;
            }
        }

        if (!updated) {
            portfolio.add(coin);
        }

        this.portfolio.put(exchange, portfolio);
    }

    @NonNull
    public List<PortfolioCoin> getExchange(ExchangeType exchange) {

        if (portfolio.containsKey(exchange)) {
            return portfolio.get(exchange);
        }

        return new ArrayList<>();
    }

    @Nullable
    public PortfolioCoin getPortfolioCoin(ExchangeType exchange, CoinItem coin) {

        List<PortfolioCoin> portfolio = getExchange(exchange);

        for (PortfolioCoin item : portfolio) {
            if (item.coinId.equals(coin.id)) {
                return item;
            }
        }

        return null;
    }

    public List<PortfolioItem> getPortfolioItems(CoinItem coin) {

        List<PortfolioItem> items = new ArrayList<>();

        for (ExchangeType exchange : ExchangeType.values()) {

            PortfolioCoin portfolio = getPortfolioCoin(exchange, coin);

            if (portfolio != null) {
                items.addAll(portfolio.items);
            }
        }

        return items;
    }

    public void clear(ExchangeType exchange, boolean persist) {

        portfolio.put(exchange, new ArrayList<PortfolioCoin>());

        if (persist) {
            prefs.setPortfolio(exchange, null);
        }
    }

    public void persist(ExchangeType exchange) {

        prefs.setPortfolio(exchange, portfolio.containsKey(exchange) ? portfolio.get(exchange) : null);
    }

    public void setExchangePortfolioListener(OnExchangePortfolioChangedListener portfolioExchangeListener) {
        this.portfolioExchangeListener = portfolioExchangeListener;
    }

    public void setFavouriteCoinStateChangedListener(OnFavouriteCoinStateChangedListener favouriteCoinChangedListener) {
        this.favouriteCoinChangedListener = favouriteCoinChangedListener;
    }

    public void notifyExchangePortfolioChanged(ExchangeType exchange) {

        if (portfolioExchangeListener != null) {
            portfolioExchangeListener.onPortfolioChanged(exchange, getExchange(exchange));
        }
    }

    public void notifyFavouriteCoinChanged(CoinItem coin, boolean isFavourite) {

        if (favouriteCoinChangedListener != null) {
            favouriteCoinChangedListener.onFavouriteCoinStateChanged(coin, isFavourite);
        }
    }
}
