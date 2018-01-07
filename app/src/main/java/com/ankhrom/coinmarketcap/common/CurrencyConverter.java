package com.ankhrom.coinmarketcap.common;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.coinmarketcap.entity.CoinItem;

/**
 * Created by R' on 1/7/2018.
 */

public final class CurrencyConverter {

    public static double convert(CoinItem coin, double value) {

        return Double.valueOf(coin.priceUsd) * value;
    }

    public static double convert(CoinItem coin, String value) {

        if (StringHelper.isEmpty(value)) {
            return -1;
        }

        return convert(coin, Double.valueOf(value));
    }
}
