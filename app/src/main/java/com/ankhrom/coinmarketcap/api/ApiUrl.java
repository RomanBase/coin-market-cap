package com.ankhrom.coinmarketcap.api;

import android.net.Uri;

import com.ankhrom.coinmarketcap.entity.CoinItem;

/**
 * Created by R' on 12/30/2017.
 */

public final class ApiUrl {

    public static final String COIN_MARKET_CAP_CURRECY = "https://coinmarketcap.com/currencies/";

    public static final String BASE_URL = "https://api.coinmarketcap.com/v1/";
    public static final String TICKER = BASE_URL + "ticker/";
    public static final String GLOBAL = BASE_URL + "global/";

    public static Uri icon(CoinItem coin) {

        return Uri.parse("https://raw.githubusercontent.com/cjdowner/cryptocurrency-icons/master/128/color/" + coin.symbol.toLowerCase() + ".png");
    }
}
