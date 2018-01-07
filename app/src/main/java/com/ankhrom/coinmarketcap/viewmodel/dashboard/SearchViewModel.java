package com.ankhrom.coinmarketcap.viewmodel.dashboard;

import com.android.volley.VolleyError;
import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.interfaces.ObjectConverter;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.common.CurrencyConverter;
import com.ankhrom.coinmarketcap.common.HitCurrencySort;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.model.trade.TradeAdapterModel;
import com.ankhrom.coinmarketcap.model.trade.TradeItemModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.hitbtc.HitBTC;
import com.ankhrom.hitbtc.HitFilter;
import com.ankhrom.hitbtc.entity.HitCurrency;
import com.ankhrom.hitbtc.entity.HitCurrencyPair;
import com.ankhrom.hitbtc.entity.HitCurrencyTicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 1/1/2018.
 */

public class SearchViewModel extends AppViewModel {


    @Override
    public void onInit() {
        super.onInit();

        setModel(new TradeAdapterModel(getContext(), new ArrayList<TradeItemModel>()));

        HitBTC hitBTC = getFactory().get(HitBTC.class);

        hitBTC.currency.info(currencyListener);
        hitBTC.currency.pair(currencyPairListener);
        hitBTC.currency.ticker(currencyTickerListener);
    }

    private final ResponseListener<List<HitCurrency>> currencyListener = new ResponseListener<List<HitCurrency>>() {
        @Override
        public void onResponse(List<HitCurrency> response) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    };

    private final ResponseListener<List<HitCurrencyPair>> currencyPairListener = new ResponseListener<List<HitCurrencyPair>>() {
        @Override
        public void onResponse(List<HitCurrencyPair> response) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    };

    private final ResponseListener<List<HitCurrencyTicker>> currencyTickerListener = new ResponseListener<List<HitCurrencyTicker>>() {
        @Override
        public void onResponse(List<HitCurrencyTicker> response) {

            response = HitFilter.filterByCurrency(response, "BTC");

            final CoinItem btc = getFactory().get(DataHolder.class).getCoin("bitcoin");

            ((TradeAdapterModel) model).adapter.addAll(ObjectHelper.sortThenConvert(new HitCurrencySort(getFactory().get(DataHolder.class)), response, new ObjectConverter<TradeItemModel, HitCurrencyTicker>() {
                @Override
                public TradeItemModel convert(HitCurrencyTicker object) {
                    TradeItemModel model = new TradeItemModel();
                    model.info.set(object.symbol.substring(0, object.symbol.length() - 3) + " - " + ApiFormat.toPriceFormat(CurrencyConverter.convert(btc, object.bid)));
                    return model;
                }
            }));
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    };

    @Override
    public int getLayoutResource() {
        return R.layout.trade_page;
    }
}
