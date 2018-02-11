package com.ankhrom.coinmarketcap.data;

import android.support.annotation.Nullable;

import com.android.volley.Cache;
import com.android.volley.VolleyError;
import com.ankhrom.base.interfaces.ObjectFactory;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseCacheListener;
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
import com.ankhrom.gdax.Gdax;
import com.ankhrom.gdax.GdaxFilter;
import com.ankhrom.gdax.entity.CoinbaseAccount;
import com.ankhrom.gdax.entity.GdaxAccount;
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
    private boolean loadingGdax;
    private boolean loadingCoinbase;

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

    private PortfolioHolder getPortfoliHolder() {

        return factory.get(PortfolioHolder.class);
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
                .queue(factory.getRequestQueue());
    }

    public void requestMarket() {

        loadingMarket = true;
        notifyListeners(true);

        RequestBuilder.get(ApiUrl.GLOBAL)
                .param(ApiParam.CURRENCY, getUserPrefs().getCurrency())
                .param(ApiParam.COUNT, ApiParam.COUNT_VALUE)
                .listener(marketListener)
                .asGson(MarketData.class)
                .queue(factory.getRequestQueue());
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
            case GDAX:
                requestGdaxPortfolio(credentials);
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
            case GDAX:
                return loadingGdax | loadingCoinbase;
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

        HitBTC hitBTC = HitBTC.init(factory.getContext()).auth(credentials.key, credentials.secret);

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

        binance.getAccountData(binanceAcountListener);
    }

    private void requestGdaxPortfolio(AuthCredentials credentials) {

        if (loadingGdax || loadingCoinbase) {
            return;
        }

        loadingGdax = true;
        loadingCoinbase = true;
        notifyExchangeListeners(ExchangeType.GDAX, true, false);

        getPortfoliHolder().clear(ExchangeType.GDAX, false);

        Gdax gdax = Gdax.init(factory.getContext()).auth(credentials.key, credentials.secret, credentials.pass);

        gdax.getAccounts(gdaxAccountListener);
        gdax.getCoinbaseAccounts(coinbaseAccountListener);
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

    private final ResponseListener<List<CoinItem>> coinsListener = new ResponseCacheListener<List<CoinItem>>() {

        @Override
        public boolean onCacheResponse(Cache.Entry cache, @Nullable List<CoinItem> response) {

            onResponse(response);
            return true;
        }

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

    private final ResponseListener<MarketData> marketListener = new ResponseCacheListener<MarketData>() {

        @Override
        public boolean onCacheResponse(Cache.Entry cache, @Nullable MarketData response) {

            onResponse(response);
            return true;
        }

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

            getPortfoliHolder().clear(ExchangeType.HIT_BTC, false);
            getExchangePrefs().setTimestamp(ExchangeType.HIT_BTC, System.currentTimeMillis());

            response = HitFilter.filterZeroBalance(response);

            if (response.isEmpty()) {
                loadingHitBTC = false;
                notifyExchangeListeners(ExchangeType.HIT_BTC, false, true);
                return;
            }

            final DataHolder holder = getDataHolder();
            final PortfolioHolder portfolio = holder.getPortfolio();

            for (HitBalance balance : response) {

                addPortfolioCurrency(portfolio, holder.getCoinBySymbol(balance.currency), ExchangeType.HIT_BTC, Double.parseDouble(balance.available) + Double.parseDouble(balance.reserved));
            }

            portfolio.persist(ExchangeType.HIT_BTC);

            loadingHitBTC = false;
            notifyExchangeListeners(ExchangeType.HIT_BTC, false, true);
            getPortfoliHolder().notifyExchangePortfolioChanged(ExchangeType.HIT_BTC);
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

            getPortfoliHolder().clear(ExchangeType.BINANCE, false);
            getExchangePrefs().setTimestamp(ExchangeType.BINANCE, System.currentTimeMillis());

            List<BinBalance> balances = BinFilter.filterZeroBalance(response.balances);

            if (balances.isEmpty()) {
                loadingBinance = false;
                notifyExchangeListeners(ExchangeType.BINANCE, false, true);
                return;
            }

            final DataHolder holder = getDataHolder();
            final PortfolioHolder portfolio = holder.getPortfolio();

            for (BinBalance balance : balances) {

                addPortfolioCurrency(portfolio, holder.getCoinBySymbol(balance.currency), ExchangeType.BINANCE, Double.parseDouble(balance.available));
            }

            portfolio.persist(ExchangeType.BINANCE);

            loadingBinance = false;
            notifyExchangeListeners(ExchangeType.BINANCE, false, true);
            getPortfoliHolder().notifyExchangePortfolioChanged(ExchangeType.BINANCE);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            loadingBinance = false;
            notifyExchangeListeners(ExchangeType.BINANCE, false, false);
        }
    };

    private final ResponseListener<List<GdaxAccount>> gdaxAccountListener = new ResponseListener<List<GdaxAccount>>() {
        @Override
        public void onResponse(@Nullable List<GdaxAccount> response) {

            getExchangePrefs().setTimestamp(ExchangeType.GDAX, System.currentTimeMillis());

            List<GdaxAccount> accounts = GdaxFilter.filterZeroAccounts(response);

            if (accounts.isEmpty()) {
                loadingGdax = false;
                notifyExchangeListeners(ExchangeType.GDAX, isLoading(ExchangeType.GDAX), true);
                return;
            }

            final DataHolder holder = getDataHolder();
            final PortfolioHolder portfolio = holder.getPortfolio();

            for (GdaxAccount account : accounts) {
                addPortfolioCurrency(portfolio, holder.getCoinBySymbol(account.currency), ExchangeType.GDAX, Double.parseDouble(account.balance));
            }

            portfolio.persist(ExchangeType.GDAX);

            loadingGdax = false;
            notifyExchangeListeners(ExchangeType.GDAX, isLoading(ExchangeType.GDAX), true);
            getPortfoliHolder().notifyExchangePortfolioChanged(ExchangeType.GDAX);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            loadingGdax = false;
            notifyExchangeListeners(ExchangeType.GDAX, isLoading(ExchangeType.GDAX), false);
        }
    };

    private final ResponseListener<List<CoinbaseAccount>> coinbaseAccountListener = new ResponseListener<List<CoinbaseAccount>>() {
        @Override
        public void onResponse(@Nullable List<CoinbaseAccount> response) {

            List<CoinbaseAccount> accounts = GdaxFilter.filterZeroCoinbaseAccounts(response);

            if (accounts.isEmpty()) {
                loadingCoinbase = false;
                notifyExchangeListeners(ExchangeType.GDAX, isLoading(ExchangeType.GDAX), true);
                return;
            }

            final DataHolder holder = getDataHolder();
            final PortfolioHolder portfolio = holder.getPortfolio();

            for (CoinbaseAccount account : accounts) {
                addPortfolioCurrency(portfolio, holder.getCoinBySymbol(account.currency), ExchangeType.GDAX, Double.parseDouble(account.balance));
            }

            portfolio.persist(ExchangeType.GDAX);

            loadingCoinbase = false;
            notifyExchangeListeners(ExchangeType.GDAX, isLoading(ExchangeType.GDAX), true);
            getPortfoliHolder().notifyExchangePortfolioChanged(ExchangeType.GDAX);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            loadingCoinbase = false;
            notifyExchangeListeners(ExchangeType.GDAX, isLoading(ExchangeType.GDAX), false);
        }
    };

    private void addPortfolioCurrency(PortfolioHolder holder, @Nullable CoinItem coin, ExchangeType exchange, double amount) {

        if (coin == null) {
            return;
        }

        PortfolioItem item = new PortfolioItem();
        item.coinId = coin.id;
        item.amount = amount;
        item.exchange = exchange;

        PortfolioCoin portfolio = new PortfolioCoin();
        portfolio.coinId = coin.id;
        portfolio.items = new ArrayList<>();
        portfolio.items.add(item);

        holder.addPortfolioCoin(portfolio, exchange);
    }

}
