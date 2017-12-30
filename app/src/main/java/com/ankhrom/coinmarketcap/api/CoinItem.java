package com.ankhrom.coinmarketcap.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by R' on 12/30/2017.
 */

public class CoinItem {

    public String id;
    public String name;
    public String symbol;
    public String rank;
    public String price_usd;
    public String price_btc;

    @SerializedName("24h_volume_usd")
    public String volume_usd;

    @SerializedName("market_cap_usd")
    public String market_cap;

    @SerializedName("available_supply")
    public String supply;

    @SerializedName("total_supply")
    public String supply_total;

    public String percent_change_1h;
    public String percent_change_24h;
    public String percent_change_7d;

    @SerializedName("last_updated")
    public String timestamp;
}
