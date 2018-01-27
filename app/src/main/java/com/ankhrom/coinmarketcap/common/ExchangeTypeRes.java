package com.ankhrom.coinmarketcap.common;

import com.ankhrom.coinmarketcap.R;

/**
 * Created by R' on 1/20/2018.
 */

public final class ExchangeTypeRes {

    public static int getIcon(ExchangeType type) {

        switch (type) {
            case NONE:
                return R.drawable.ic_logo;
            case HIT_BTC:
                return R.drawable.ic_hit_btc;
            case BINANCE:
                return R.drawable.ic_binance;
            default:
                return R.drawable.ic_logo;
        }
    }
}
