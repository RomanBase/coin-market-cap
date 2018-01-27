package com.ankhrom.coinmarketcap.listener;

import com.ankhrom.coinmarketcap.common.ExchangeType;

/**
 * Created by R' on 1/27/2018.
 */

public interface DataExchangeLoadingListener {

    void onExchangeLoading(ExchangeType exchange, boolean isLoading, boolean isValid);
}
