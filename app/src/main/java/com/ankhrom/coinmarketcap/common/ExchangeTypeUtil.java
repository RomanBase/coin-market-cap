package com.ankhrom.coinmarketcap.common;

import com.ankhrom.coinmarketcap.R;

/**
 * Created by R' on 1/20/2018.
 */

public final class ExchangeTypeUtil {

    public static int getLogo(ExchangeType type) {

        switch (type) {
            case NONE:
                return R.drawable.ic_virtual_icon;
            case HIT_BTC:
                return R.drawable.ic_hitbtc;
            case BINANCE:
                return R.drawable.ic_binance;
            case GDAX:
                return R.drawable.ic_gdax;
            default:
                return R.drawable.ic_logo;
        }
    }

    public static int getIcon(ExchangeType type) {

        switch (type) {
            case NONE:
                return R.drawable.ic_virtual_icon;
            case HIT_BTC:
                return R.drawable.ic_hitbtc_icon;
            case BINANCE:
                return R.drawable.ic_binance_icon;
            case GDAX:
                return R.drawable.ic_gdax_icon;
            default:
                return R.drawable.ic_logo;
        }
    }

    public static boolean isListed(ExchangeType type) {

        return !(type == ExchangeType.NONE);
    }

    public static boolean isPortfolioPriceAvailable(ExchangeType type) {

        return type == ExchangeType.NONE;
    }

    public static boolean isPassRequired(ExchangeType type) {

        return type == ExchangeType.GDAX;
    }
}
