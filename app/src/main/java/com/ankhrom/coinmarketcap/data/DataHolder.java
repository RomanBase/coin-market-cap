package com.ankhrom.coinmarketcap.data;

import com.ankhrom.base.interfaces.ObjectFactory;
import com.ankhrom.coinmarketcap.api.CoinItem;
import com.ankhrom.coinmarketcap.api.MarketData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 12/31/2017.
 */

public class DataHolder {

    private List<CoinItem> coins;
    private MarketData market;

    private final DataFetcher fetcher;

    private DataHolder(ObjectFactory factory) {

        coins = new ArrayList<>();
        fetcher = new DataFetcher(factory);
    }

    public static DataHolder init(ObjectFactory factory) {

        DataHolder holder = new DataHolder(factory);
        holder.fetchData();

        return holder;
    }

    protected void fetchData() {

        fetcher.requestCoins();
        fetcher.requestMarket();
    }

    public DataFetcher getFetcher() {

        return fetcher;
    }

    public void reload() {

        coins.clear();
        fetchData();
    }

    public void updateCoins(List<CoinItem> items) {

        coins.addAll(items);
    }

    public void updateMarket(MarketData data) {

        market = data;
    }

    public List<CoinItem> getCoins() {
        return coins;
    }

    public MarketData getMarket() {
        return market;
    }

    public boolean isCoinListAvailable() {

        return coins != null && coins.size() > 0;
    }

    public boolean isMarketAvailable() {

        return market != null;
    }
}
