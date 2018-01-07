package com.ankhrom.coinmarketcap.listener;

import com.ankhrom.coinmarketcap.entity.CoinItem;

/**
 * Created by R' on 1/7/2018.
 */

public interface OnCoinSelectedListener {

    void onCoinSelected(CoinItem coin);
}
