package com.ankhrom.coinmarketcap.model;

import com.ankhrom.base.model.SelectableItemModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiMath;
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
        coin.price_usd = ApiMath.toPriceFormat(coin.price_usd);

        coin.percent_change_1h += "%";
        coin.percent_change_24h += "%";
        coin.percent_change_7d += "%";

        coin.market_cap = ApiMath.toShortFormat(coin.market_cap);
        coin.volume_usd = ApiMath.toShortFormat(coin.volume_usd);

        change_1h_color_res = coin.percent_change_1h.startsWith("-") ? R.color.red : R.color.green;
        change_24h_color_res = coin.percent_change_24h.startsWith("-") ? R.color.red : R.color.green;
        change_7d_color_res = coin.percent_change_7d.startsWith("-") ? R.color.red : R.color.green;
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
