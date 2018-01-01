package com.ankhrom.coinmarketcap.model;

import com.ankhrom.base.model.SelectableItemModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.api.CoinItem;

/**
 * Created by R' on 12/30/2017.
 */

public class CoinItemModel extends SelectableItemModel {

    public final CoinItem coin;

    public final String price;
    public final String supply;
    public final String change1h;
    public final String change24h;
    public final String marketCap;
    public final String volume;

    public final int change_1h_color_res;
    public final int change_24h_color_res;

    public CoinItemModel(CoinItem item) {

        coin = item;

        change_1h_color_res = coin.percentChange1h.startsWith("-") ? R.color.red : R.color.green;
        change_24h_color_res = coin.percentChange24h.startsWith("-") ? R.color.red : R.color.green;

        price = ApiFormat.toPriceFormat(coin.priceUsd) + " $";
        supply = ApiFormat.toShortFormat(coin.supply);

        change1h = coin.percentChange1h + "%";
        change24h = coin.percentChange24h + "%";

        marketCap = ApiFormat.toShortFormat(coin.marketCap);
        volume = ApiFormat.toShortFormat(coin.volumeUsd);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.coin_item;
    }
}
