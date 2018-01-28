package com.ankhrom.coinmarketcap.data;

import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.ankhrom.base.Base;
import com.ankhrom.base.interfaces.ObjectFactory;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.binance.BinFilter;
import com.ankhrom.binance.Binance;
import com.ankhrom.binance.entity.BinAccount;
import com.ankhrom.binance.entity.BinBalance;
import com.ankhrom.coinmarketcap.api.ApiParam;
import com.ankhrom.coinmarketcap.api.ApiUrl;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.MarketData;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;
import com.ankhrom.coinmarketcap.listener.DataExchangeLoadingListener;
import com.ankhrom.coinmarketcap.listener.DataLoadingListener;
import com.ankhrom.coinmarketcap.prefs.ExchangePrefs;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;
import com.ankhrom.hitbtc.HitBTC;
import com.ankhrom.hitbtc.HitFilter;
import com.ankhrom.hitbtc.entity.HitBalance;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 12/31/2017.
 */

public class DataFetcher {

    private final ObjectFactory factory;

    private List<DataLoadingListener> listeners;
    private List<DataExchangeLoadingListener> exchangeListeners;

    private boolean loadingCoins;
    private boolean loadingMarket;

    private boolean loadingHitBTC;
    private boolean loadingBinance;

    public DataFetcher(ObjectFactory factory) {
        this.factory = factory;

        listeners = new ArrayList<>();
        exchangeListeners = new ArrayList<>();
    }

    private UserPrefs getUserPrefs() {

        return factory.get(UserPrefs.class);
    }

    private ExchangePrefs getExchangePrefs() {

        return factory.get(ExchangePrefs.class);
    }

    private DataHolder getDataHolder() {

        return factory.get(DataHolder.class);
    }

    public void addListener(final DataLoadingListener listener) {

        if (!loadingCoins && !loadingMarket) {
            listener.onDataLoading(false, factory.get(DataHolder.class));
        }

        if (listeners.contains(listener)) {
            return;
        }

        listeners.add(listener);
    }

    public void removeListener(DataLoadingListener listener) {

        if (!listeners.contains(listener)) {
            return;
        }

        listeners.remove(listener);
    }

    public void addExchangeListener(DataExchangeLoadingListener listener) {

        if (exchangeListeners.contains(listener)) {
            return;
        }

        exchangeListeners.add(listener);
    }

    public void removeExchangeListener(DataExchangeLoadingListener listener) {

        if (!exchangeListeners.contains(listener)) {
            return;
        }

        exchangeListeners.remove(listener);
    }

    public void requestCoins() {

        loadingCoins = true;
        notifyListeners(true);

        RequestBuilder.get(ApiUrl.TICKER)
                .param(ApiParam.CURRENCY, getUserPrefs().getCurrency())
                .param(ApiParam.COUNT, ApiParam.COUNT_VALUE)
                .listener(coinsListener)
                .asGson(new TypeToken<List<CoinItem>>() {
                }.getType())
                .queue(factory);
    }

    public void requestMarket() {

        loadingMarket = true;
        notifyListeners(true);

        RequestBuilder.get(ApiUrl.GLOBAL)
                .param(ApiParam.CURRENCY, getUserPrefs().getCurrency())
                .param(ApiParam.COUNT, ApiParam.COUNT_VALUE)
                .listener(marketListener)
                .asGson(MarketData.class)
                .queue(factory);
    }

    public void requestExchanges() {

        for (ExchangeType exchange : ExchangeType.values()) {
            requestExchangePortfolio(exchange);
        }
    }

    public void requestExchangePortfolio(ExchangeType exchange) {

        requestExchangePortfolio(exchange, getExchangePrefs().getAuth(exchange));
    }

    public void requestExchangePortfolio(ExchangeType exchange, AuthCredentials credentials) {

        if (credentials == null || !credentials.isValid()) {
            return;
        }

        switch (exchange) {
            case HIT_BTC:
                requestHitBTCPortfolio(credentials);
                break;
            case BINANCE:
                requestBinancePortfolio(credentials);
                break;
        }
    }

    public boolean isLoading() {

        return loadingMarket || loadingCoins;
    }

    public boolean isLoading(ExchangeType exchange) {

        switch (exchange) {
            case HIT_BTC:
                return loadingHitBTC;
            case BINANCE:
                return loadingBinance;
            default:
                return loadingCoins || loadingMarket;
        }
    }

    private void requestHitBTCPortfolio(AuthCredentials credentials) {

        if (loadingHitBTC) {
            return;
        }

        loadingHitBTC = true;
        notifyExchangeListeners(ExchangeType.HIT_BTC, true, false);

        HitBTC hitBTC = HitBTC.init(factory.getRequestQueue()).auth(credentials.key, credentials.secret);

        getUserPrefs().setPortfolio(ExchangeType.HIT_BTC, null);

        //hitBTC.balanceAccount(hitBalanceListener);
        hitBTC.balanceTrading(hitBalanceListener);
    }

    private void requestBinancePortfolio(AuthCredentials credentials) {

        if (loadingBinance) {
            return;
        }

        loadingBinance = true;
        notifyExchangeListeners(ExchangeType.BINANCE, true, false);

        Binance binance = Binance.init(factory.getContext()).auth(credentials.key, credentials.secret);

        getUserPrefs().setPortfolio(ExchangeType.BINANCE, null);

        binance.getAccountData(binanceAcountListener);
    }

    private void notifyListeners(final boolean isValid) {

        if (listeners.size() == 0) {
            return;
        }

        DataHolder holder = factory.get(DataHolder.class);

        boolean isLoading = loadingCoins || loadingMarket;

        for (DataLoadingListener listener : listeners) {
            if (isValid) {
                listener.onDataLoading(isLoading, holder);
            } else {
                listener.onDataLoadingFailed(isLoading, holder);
            }
        }
    }

    private void notifyExchangeListeners(ExchangeType exchange, boolean isLoading, boolean isValid) {

        for (DataExchangeLoadingListener listener : exchangeListeners) {

            listener.onExchangeLoading(exchange, isLoading, isValid);
        }
    }

    private final ResponseListener<List<CoinItem>> coinsListener = new ResponseListener<List<CoinItem>>() {
        @Override
        public void onResponse(List<CoinItem> response) {

            loadingCoins = false;

            if (response == null) {
                notifyListeners(false);
                return;
            }

            getDataHolder().updateCoins(response);

            notifyListeners(true);

            requestExchanges();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();

            loadingCoins = false;
            notifyListeners(false);
        }
    };

    private final ResponseListener<MarketData> marketListener = new ResponseListener<MarketData>() {
        @Override
        public void onResponse(MarketData response) {

            loadingMarket = false;

            if (response == null) {
                notifyListeners(false);
                return;
            }

            getDataHolder().updateMarket(response);

            notifyListeners(true);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();

            loadingCoins = false;
            notifyListeners(false);
        }
    };

    private final ResponseListener<List<HitBalance>> hitBalanceListener = new ResponseListener<List<HitBalance>>() {
        @Override
        public void onResponse(List<HitBalance> response) {

            getExchangePrefs().setTimestamp(ExchangeType.HIT_BTC, System.currentTimeMillis());

            response = HitFilter.filterZeroBalance(response);

            if (response.isEmpty()) {
                loadingHitBTC = false;
                notifyExchangeListeners(ExchangeType.HIT_BTC, false, true);
                return;
            }

            final DataHolder holder = getDataHolder();
            final UserPrefs prefs = getUserPrefs();

            for (HitBalance balance : response) {

                addPortfolioCurrency(prefs, holder.getCoinBySymbol(balance.currency), Double.parseDouble(balance.available) + Double.parseDouble(balance.reserved));
            }

            loadingHitBTC = false;
            notifyExchangeListeners(ExchangeType.HIT_BTC, false, true);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            loadingHitBTC = false;
            notifyExchangeListeners(ExchangeType.HIT_BTC, false, false);
        }
    };

    private final ResponseListener<BinAccount> binanceAcountListener = new ResponseListener<BinAccount>() {
        @Override
        public void onResponse(BinAccount response) {

            getExchangePrefs().setTimestamp(ExchangeType.BINANCE, System.currentTimeMillis());

            List<BinBalance> balances = BinFilter.filterZeroBalance(response.balances);

            if (balances.isEmpty()) {
                loadingBinance = false;
                notifyExchangeListeners(ExchangeType.BINANCE, false, true);
                return;
            }

            final DataHolder holder = getDataHolder();
            final UserPrefs prefs = getUserPrefs();

            for (BinBalance balance : balances) {

                addPortfolioCurrency(prefs, holder.getCoinBySymbol(balance.currency), Double.parseDouble(balance.available));
            }

            loadingBinance = false;
            notifyExchangeListeners(ExchangeType.BINANCE, false, true);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Base.logE(new String(error.networkResponse.data));
            loadingBinance = false;
            notifyExchangeListeners(ExchangeType.BINANCE, false, false);
        }
    };

    private void addPortfolioCurrency(UserPrefs prefs, @Nullable CoinItem coin, double amount) {

        if (coin == null) {
            return;
        }

        PortfolioItem item = new PortfolioItem();
        item.coinId = coin.id;
        item.amount = amount;
        item.exchange = ExchangeType.HIT_BTC;

        PortfolioCoin portfolio = new PortfolioCoin();
        portfolio.coinId = coin.id;
        portfolio.items = new ArrayList<>();
        portfolio.items.add(item);

        prefs.addPortfolioCoin(portfolio, ExchangeType.HIT_BTC);
    }

}
