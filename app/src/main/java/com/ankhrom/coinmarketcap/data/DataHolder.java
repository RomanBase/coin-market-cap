package com.ankhrom.coinmarketcap.data;

import android.support.annotation.Nullable;

import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.interfaces.ObjectConverter;
import com.ankhrom.base.interfaces.ObjectFactory;
import com.ankhrom.coinmarketcap.BuildConfig;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.MarketData;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.model.coin.CoinItemModel;
import com.ankhrom.coinmarketcap.prefs.ExchangePrefs;
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

    private final ObjectFactory factory;
    private final DataFetcher fetcher;

    private DataHolder(ObjectFactory factory) {
        this.factory = factory;

        coins = new ArrayList<>();
        fetcher = new DataFetcher(factory);
    }

    public static DataHolder init(ObjectFactory factory) {

        debugExchanges(factory.get(ExchangePrefs.class));

        DataHolder holder = new DataHolder(factory);
        holder.fetchData();

        return holder;
    }

    private static void debugExchanges(ExchangePrefs prefs) {

        if (BuildConfig.DEBUG) {

            prefs.setAuth(ExchangeType.HIT_BTC, new AuthCredentials("252d13df5fb7d277c6c6de185c18bb65:6c60036d5a2655be6e988af8fa385be0"));
            prefs.setAuth(ExchangeType.BINANCE, new AuthCredentials("dqCaO566NUtorPE0IR6IdeSY8TY1F49AOTHqqfb4vA2SAh5oadMxzzpW4ooPjHG4:yl5rlhffiPqcA2c1TahjfABYJs2FkQzsHijJozBuYYog6hnLulbdWGH11rJXvgNA"));
        }
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
            coinItems = ObjectHelper.convert(coins.subList(0, Math.min(coins.size(), 100)), new CoinItemConverter());

            List<String> favs = factory.get(UserPrefs.class).getFavourites();

            for (CoinItemModel item : coinItems) {
                item.isFavourite.set(favs.contains(item.coin.id));
            }
        }

        return coinItems;
    }

    public List<CoinItemModel> getFavouriteCoinItems() {

        List<String> favs = factory.get(UserPrefs.class).getFavourites();
        List<CoinItemModel> items = getCoinItems();

        List<CoinItemModel> output = new ArrayList<>();

        for (String id : favs) {
            for (CoinItemModel coin : items) {
                if (coin.coin.id.equals(id)) {
                    output.add(coin);
                    break;
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
    public CoinItem getCoin(String id) {

        for (CoinItem coin : coins) {
            if (coin.id.equals(id)) {
                return coin;
            }
        }

        return null;
    }

    public CoinItemModel getCoinItem(String id) {

        for (CoinItemModel coin : coinItems) {
            if (coin.coin.id.equals(id)) {
                return coin;
            }
        }

        return null;
    }

    @Nullable
    public CoinItem getCoinBySymbol(String symbol) {

        for (CoinItem coin : coins) {
            if (coin.symbol.equals(symbol)) {
                return coin;
            }
        }

        return null;
    }

    @Nullable
    public PortfolioCoin getPortfolioCoin(String id) {

        List<PortfolioCoin> portfolio = factory.get(UserPrefs.class).getPortfolio();

        for (PortfolioCoin coin : portfolio) {
            if (coin.coinId.equals(id)) {
                return coin;
            }
        }

        return null;
    }

    private class CoinItemConverter implements ObjectConverter<CoinItemModel, CoinItem> {

        @Override
        public CoinItemModel convert(CoinItem object) {
            return new CoinItemModel(object);
        }
    }
}
