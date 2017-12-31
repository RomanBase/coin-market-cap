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

    public final int change_1h_color_res;
    public final int change_24h_color_res;
    public final int change_7d_color_res;

    public CoinItemModel(CoinItem item) {

        coin = item;

        coin.priceUsd = ApiFormat.toPriceFormat(coin.priceUsd) + " $";
        coin.supply = ApiFormat.toPriceFormat(coin.supply);

        coin.percentChange1h += "%";
        coin.percentChange24h += "%";
        coin.percentChange7d += "%";

        change_1h_color_res = coin.percentChange1h.startsWith("-") ? R.color.red : R.color.green;
        change_24h_color_res = coin.percentChange24h.startsWith("-") ? R.color.red : R.color.green;
        change_7d_color_res = coin.percentChange7d.startsWith("-") ? R.color.red : R.color.green;

        coin.marketCap = ApiFormat.toShortFormat(coin.marketCap);
        coin.volumeUsd = ApiFormat.toShortFormat(coin.volumeUsd);
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
