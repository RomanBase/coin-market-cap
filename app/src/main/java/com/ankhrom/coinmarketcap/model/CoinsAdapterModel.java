package com.ankhrom.coinmarketcap.model;

import android.content.Context;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.api.MarketData;

import java.util.Collection;
import java.util.Date;

/**
 * Created by R' on 12/30/2017.
 */

public class CoinsAdapterModel extends AdapterModel<CoinItemModel> {

    public final String marketCap;
    public final String marketVolume;
    public final String date;

    public CoinsAdapterModel(Context context, Collection<CoinItemModel> collection, MarketData market) {
        super(context, collection);

        marketCap = ApiFormat.toShortFormat(String.valueOf(market.marketCap));
        marketVolume = ApiFormat.toShortFormat(String.valueOf(market.marketVolume));
        date = new Date(market.timestamp * 1000).toLocaleString();
    }

    public CoinsAdapterModel(Context context, Collection<CoinItemModel> collection) {
        super(context, collection);

        marketCap = null;
        marketVolume = null;
        date = null;
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
