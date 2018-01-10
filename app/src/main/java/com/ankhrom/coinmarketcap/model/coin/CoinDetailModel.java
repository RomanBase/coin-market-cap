package com.ankhrom.coinmarketcap.model.coin;

import com.ankhrom.base.model.Model;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.api.ApiUrl;
import com.ankhrom.coinmarketcap.entity.CoinItem;

/**
 * Created by R' on 1/10/2018.
 */

public class CoinDetailModel extends Model {

    public final CoinItem coin;
    public final String url;

    public CoinDetailModel(CoinItem coin) {

        this.coin = coin;
        this.url = ApiUrl.COIN_MARKET_CAP_CURRECY + coin.id;
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
