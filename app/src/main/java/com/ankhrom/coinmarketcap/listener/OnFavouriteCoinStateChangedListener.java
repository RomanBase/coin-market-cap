package com.ankhrom.coinmarketcap.listener;

import com.ankhrom.coinmarketcap.entity.CoinItem;

/**
 * Created by R' on 1/1/2018.
 */

public interface OnFavouriteCoinStateChangedListener {

    void onFavouriteCoinStateChanged(CoinItem coin, boolean isFavourite);
}
