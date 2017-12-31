package com.ankhrom.coinmarketcap.model;

import android.content.Context;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.api.ApiMath;
import com.ankhrom.coinmarketcap.api.MarketData;

import java.util.Collection;

/**
 * Created by R' on 12/30/2017.
 */

public class DashboardModel extends AdapterModel<CoinItemModel> {

    public final String marketCap;
    public final String marketVolume;

    public DashboardModel(Context context, Collection<CoinItemModel> collection, MarketData market) {
        super(context, collection);

        marketCap = ApiMath.toShortFormat(String.valueOf(market.marketCap));
        marketVolume = ApiMath.toShortFormat(String.valueOf(market.marketVolume));
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
