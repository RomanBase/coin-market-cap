package com.ankhrom.coinmarketcap.listener;

import android.support.annotation.Nullable;

import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;

/**
 * Created by R' on 1/20/2018.
 */

public interface OnExchangeAuthChangedListener {

    void onExchangeAuthChanged(ExchangeType type, @Nullable AuthCredentials credentials);
}
