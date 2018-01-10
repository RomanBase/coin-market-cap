package com.ankhrom.coinmarketcap.listener;

import com.ankhrom.coinmarketcap.model.coin.CoinItemModel;

/**
 * Created by R' on 1/1/2018.
 */

public interface OnFavouriteItemChangedListener {

    void onFavouriteItemPrefsChanged(CoinItemModel item);
}
