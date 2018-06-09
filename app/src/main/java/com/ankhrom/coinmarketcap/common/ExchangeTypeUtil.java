package com.ankhrom.coinmarketcap.common;

import com.ankhrom.coinmarketcap.R;

/**
 * Created by R' on 1/20/2018.
 */

public final class ExchangeTypeUtil {

    public static int getLogo(ExchangeType type) {

        switch (type) {
            case HIT_BTC:
                return R.drawable.ic_hitbtc;
            case BINANCE:
                return R.drawable.ic_binance;
            case GDAX:
                return R.drawable.ic_gdax;
            case ETHER:
                return R.drawable.ic_ether_wallet;
            default:
                return R.drawable.ic_logo_dark;
        }
    }

    public static int getIcon(ExchangeType type) {

        switch (type) {
            case HIT_BTC:
                return R.drawable.ic_hitbtc_icon;
            case BINANCE:
                return R.drawable.ic_binance_icon;
            case GDAX:
                return R.drawable.ic_gdax_icon;
            default:
                return R.drawable.ic_logo_dark;
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
