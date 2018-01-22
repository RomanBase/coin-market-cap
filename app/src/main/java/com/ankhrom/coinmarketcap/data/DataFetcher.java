package com.ankhrom.coinmarketcap.data;

import com.android.volley.VolleyError;
import com.ankhrom.base.Base;
import com.ankhrom.base.interfaces.ObjectFactory;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.coinmarketcap.api.ApiParam;
import com.ankhrom.coinmarketcap.api.ApiUrl;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.MarketData;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;
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

    private boolean loadingCoins;
    private boolean loadingMarket;

    private boolean loadingHitBTC;

    public DataFetcher(ObjectFactory factory) {
        this.factory = factory;

        listeners = new ArrayList<>();
    }

    public void addListener(final DataLoadingListener listener) {

        if (!loadingCoins && !loadingMarket) {
            Base.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.onDataLoading(false, factory.get(DataHolder.class));
                }
            }, 25);
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

    public void requestCoins() {

        loadingCoins = true;
        notifyListeners(true);

        RequestBuilder.get(ApiUrl.TICKER)
                .param(ApiParam.CURRENCY, factory.get(UserPrefs.class).getCurrency())
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
                .param(ApiParam.CURRENCY, factory.get(UserPrefs.class).getCurrency())
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

        requestExchangePortfolio(exchange, factory.get(ExchangePrefs.class).getAuth(exchange));
    }

    public void requestExchangePortfolio(ExchangeType exchange, AuthCredentials credentials) {

        if (credentials == null || !credentials.isValid()) {
            return;
        }

        switch (exchange) {
            case ETORO:
                requestEtoroPortfolio();
                break;
            case HIT_BTC:
                requestHitBTCPortfolio(credentials);
                break;
        }
    }

    private void requestEtoroPortfolio() {

        // TODO: 1/21/2018 waiting for keys
    }

    private void requestHitBTCPortfolio(AuthCredentials credentials) { // 252d13df5fb7d277c6c6de185c18bb65:6c60036d5a2655be6e988af8fa385be0

        if (loadingHitBTC) {
            return;
        }

        loadingHitBTC = true;

        HitBTC hitBTC = HitBTC.init(factory.getRequestQueue()).auth(credentials.key, credentials.secret);

        factory.get(UserPrefs.class).setPortfolio(ExchangeType.HIT_BTC, null);

        hitBTC.balanceAccount(hitBalanceListener);
        hitBTC.balanceTrading(hitBalanceListener);
    }

    private void notifyListeners(boolean isValid) {

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

    private final ResponseListener<List<CoinItem>> coinsListener = new ResponseListener<List<CoinItem>>() {
        @Override
        public void onResponse(List<CoinItem> response) {

            loadingCoins = false;

            if (response == null) {
                notifyListeners(false);
                return;
            }

            factory.get(DataHolder.class).updateCoins(response);

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

            factory.get(DataHolder.class).updateMarket(response);

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

            response = HitFilter.filterZeroBalance(response);

            if (response.isEmpty()) {
                return;
            }

            final DataHolder holder = factory.get(DataHolder.class);
            final UserPrefs prefs = factory.get(UserPrefs.class);

            for (HitBalance balance : response) {

                CoinItem coin = holder.getCoinBySymbol(balance.currency);

                if (coin == null) {
                    continue;
                }

                PortfolioItem item = new PortfolioItem();
                item.coinId = coin.id;
                item.amount = Double.parseDouble(balance.available) + Double.parseDouble(balance.reserved);
                item.exchange = ExchangeType.HIT_BTC;

                PortfolioCoin portfolio = new PortfolioCoin();
                portfolio.coinId = coin.id;
                portfolio.items = new ArrayList<>();
                portfolio.items.add(item);

                prefs.addPortfolioCoin(portfolio, ExchangeType.HIT_BTC);
            }

            loadingHitBTC = false;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            loadingHitBTC = false;
        }
    };
}
