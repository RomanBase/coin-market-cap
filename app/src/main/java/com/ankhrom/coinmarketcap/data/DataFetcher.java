package com.ankhrom.coinmarketcap.data;

import com.android.volley.VolleyError;
import com.ankhrom.base.interfaces.ObjectFactory;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.coinmarketcap.api.ApiParam;
import com.ankhrom.coinmarketcap.api.ApiUrl;
import com.ankhrom.coinmarketcap.api.CoinItem;
import com.ankhrom.coinmarketcap.api.MarketData;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by R' on 12/31/2017.
 */

public class DataFetcher {

    private final ObjectFactory factory;

    private DataLoadingListener listener;

    private int index;
    private boolean hasNext;

    private boolean loadingCoins;
    private boolean loadingMarket;

    public DataFetcher(ObjectFactory factory) {
        this.factory = factory;
    }

    public void setListener(DataLoadingListener listener) {
        this.listener = listener;
        notifyListener(true);
    }

    public void requestCoins() {

        loadingCoins = true;
        notifyListener(true);

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
        notifyListener(true);

        RequestBuilder.get(ApiUrl.GLOBAL)
                .param(ApiParam.CURRENCY, factory.get(UserPrefs.class).getCurrency())
                .param(ApiParam.COUNT, ApiParam.COUNT_VALUE)
                .listener(marketListener)
                .asGson(MarketData.class)
                .queue(factory);
    }

    private void notifyListener(boolean isValid) {

        if (listener == null) {
            return;
        }

        DataHolder holder = factory.get(DataHolder.class);

        boolean isLoading = loadingCoins || loadingMarket;

        if (isValid) {
            listener.onDataLoading(isLoading, holder);
        } else {
            listener.onDataLoadingFailed(isLoading, holder);
        }
    }

    private final ResponseListener<List<CoinItem>> coinsListener = new ResponseListener<List<CoinItem>>() {
        @Override
        public void onResponse(List<CoinItem> response) {

            loadingCoins = false;

            if (response == null) {
                notifyListener(false);
                return;
            }

            factory.get(DataHolder.class).updateCoins(response);

            notifyListener(true);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();

            loadingCoins = false;
            notifyListener(false);
        }
    };

    private final ResponseListener<MarketData> marketListener = new ResponseListener<MarketData>() {
        @Override
        public void onResponse(MarketData response) {

            loadingMarket = false;

            if (response == null) {
                notifyListener(false);
                return;
            }

            factory.get(DataHolder.class).updateMarket(response);

            notifyListener(true);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();

            loadingCoins = false;
            notifyListener(false);
        }
    };
}
