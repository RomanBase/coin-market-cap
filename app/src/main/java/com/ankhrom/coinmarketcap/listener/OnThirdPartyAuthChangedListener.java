package com.ankhrom.coinmarketcap.listener;

import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;

/**
 * Created by R' on 1/4/2018.
 */

public interface OnThirdPartyAuthChangedListener {

    void onThirdPartyAuthChanged(ExchangeType type, AuthCredentials credentials);
}
