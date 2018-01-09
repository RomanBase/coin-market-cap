package com.ankhrom.hitbtc;


public final class HitApiUrl {

    public static final String API_VERSION = "2";
    public static final String BASE_URL = "https://api.hitbtc.com/api/" + API_VERSION + "/";
    public static final String PUBLIC_URL = BASE_URL + "public/";
    public static final String TRADING = BASE_URL + "trading/";
    public static final String ACCOUNT = BASE_URL + "account/";
    public static final String HISTORY = BASE_URL + "hitory/";

    public static final String CURRENCY = PUBLIC_URL + "currency/";
    public static final String CURRENCY_SYMBOL = PUBLIC_URL + "symbol/";
    public static final String CURRENCY_TICKER = PUBLIC_URL + "ticker/";

    public static final String TRADING_BALANCE = TRADING + "balance/";

    public static final String ACCOUNT_BALANCE = ACCOUNT + "balance/";
    public static final String ACCOUNT_TRANSACTIONS = ACCOUNT + "transactions/";

    public static final String HISTORY_ORDER = HISTORY + "order/";
    public static final String HISTORY_TRADES = HISTORY + "trades/";
}
