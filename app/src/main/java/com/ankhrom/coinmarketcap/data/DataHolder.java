package com.ankhrom.coinmarketcap.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.interfaces.ObjectConverter;
import com.ankhrom.base.interfaces.ObjectFactory;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.MarketData;
import com.ankhrom.coinmarketcap.model.coin.CoinItemModel;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by R' on 12/31/2017.
 */

public class DataHolder { //todo cache

    private MarketData market;
    private List<CoinItem> coins;
    private List<CoinItemModel> coinItems;

    private List<String> favourites;

    private final ObjectFactory factory;
    private final DataFetcher fetcher;

    private int itemsCount = 500;

    private DataHolder(ObjectFactory factory) {
        this.factory = factory;

        coins = new ArrayList<>();
        fetcher = new DataFetcher(factory);

        favourites = getPrefs().getFavourites();
    }

    public static DataHolder init(ObjectFactory factory) {

        DataHolder holder = new DataHolder(factory);
        holder.fetchData();

        return holder;
    }

    private UserPrefs getPrefs() {

        return factory.get(UserPrefs.class);
    }

    public PortfolioHolder getPortfolio() {

        return factory.get(PortfolioHolder.class);
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

    public List<CoinItemModel> getCoinItems() {

        if (coinItems == null) {
            coinItems = ObjectHelper.convert(coins.subList(0, Math.min(coins.size(), itemsCount)), new CoinItemConverter());

            for (CoinItemModel item : coinItems) {
                item.isFavourite.set(favourites.contains(item.coin.id));
            }

            List<CoinItemModel> favItems = getFavouriteCoinItems();

            for (CoinItemModel item : favItems) {
                if (!coinItems.contains(item)) {
                    coinItems.add(item);
                }
            }
        }

        return coinItems;
    }

    public List<CoinItemModel> getFavouriteCoinItems() {

        List<CoinItemModel> items = getCoinItems();

        List<CoinItemModel> output = new ArrayList<>();

        for (String id : favourites) {

            boolean found = false;

            for (CoinItemModel coin : items) {
                if (coin.coin.id.equals(id)) {
                    output.add(coin);
                    found = true;
                    break;
                }
            }

            if (!found) {
                CoinItemModel coin = getCoinItem(id);
                if (coin != null) {
                    coin.isFavourite.set(true);
                    output.add(coin);
                }
            }
        }

        Collections.sort(output, new Comparator<CoinItemModel>() {
            @Override
            public int compare(CoinItemModel a, CoinItemModel b) {

                int rankA = Integer.valueOf(a.coin.rank);
                int rankB = Integer.valueOf(b.coin.rank);

                return rankA > rankB ? 1 : -1;
            }
        });

        return output;
    }

    @Nullable
    public CoinItem getCoin(@NonNull String id) {

        for (CoinItem coin : coins) {
            if (coin.id.equals(id)) {
                return coin;
            }
        }

        return mockCoin(id);
    }

    @Nullable
    public CoinItemModel getCoinItem(@NonNull String id) {

        for (CoinItemModel coin : coinItems) {
            if (coin.coin.id.equals(id)) {
                return coin;
            }
        }

        CoinItem coin = getCoin(id);

        if (coin != null) {
            return new CoinItemModel(coin);
        }

        return null;
    }

    @Nullable
    public CoinItem getCoinBySymbol(@NonNull String symbol) {

        for (CoinItem coin : coins) {
            if (coin.symbol.equals(symbol)) {
                return coin;
            }
        }

        return mockCoin(symbol);
    }

    public CoinItem mockCoin(String symbol) {

        CoinItem coin = new CoinItem();
        coin.symbol = symbol;
        coin.id = symbol;
        coin.name = symbol;
        coin.rank = String.valueOf(coins.size());
        coin.marketCap = "1";
        coin.priceBtc = "0";
        coin.priceUsd = "0";
        coin.supply = "1";
        coin.supplyMax = "1";
        coin.supplyTotal = "1";
        coin.volumeUsd = "1";
        coin.percentChange1h = "0";
        coin.percentChange7d = "0";
        coin.percentChange24h = "0";
        coin.mock = true;

        return coin;
    }

    public void addFavourite(String id) {

        favourites.add(id);
        getPrefs().setFavourites(favourites);
    }

    public void removeFavourite(String id) {

        favourites.remove(id);
        getPrefs().setFavourites(favourites);
    }

    public List<String> getFavourites() {
        return favourites;
    }

    private class CoinItemConverter implements ObjectConverter<CoinItemModel, CoinItem> {

        @Override
        public CoinItemModel convert(CoinItem object) {
            return new CoinItemModel(object);
        }
    }
}
