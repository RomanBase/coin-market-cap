package com.ankhrom.coinmarketcap.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by R' on 12/30/2017.
 */

public class CoinItem {

    public String id;
    public String name;
    public String symbol;
    public String rank;

    @SerializedName("price_usd")
    public String priceUsd;

    @SerializedName("price_btc")
    public String priceBtc;

    @SerializedName("24h_volume_usd")
    public String volumeUsd;

    @SerializedName("market_cap_usd")
    public String marketCap;

    @SerializedName("available_supply")
    public String supply;

    @SerializedName("total_supply")
    public String supplyTotal;

    @SerializedName("max_supply")
    public String supplyMax;

    @SerializedName("percent_change_1h")
    public String percentChange1h;

    @SerializedName("percent_change_24h")
    public String percentChange24h;

    @SerializedName("percent_change_7d")
    public String percentChange7d;

    @SerializedName("last_updated")
    public String timestamp;

    public boolean mock;

    @Override
    public String toString() {
        return symbol + " - " + name;
    }
}
