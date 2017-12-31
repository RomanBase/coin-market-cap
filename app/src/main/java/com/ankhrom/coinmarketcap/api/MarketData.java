package com.ankhrom.coinmarketcap.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by R' on 12/31/2017.
 */

public class MarketData {

    @SerializedName("total_market_cap_usd")
    public long marketCap;

    @SerializedName("total_24h_volume_usd")
    public long marketVolume;

    @SerializedName("bitcoin_percentage_of_market_cap")
    public float bitcoinDominance;

    @SerializedName("active_currencies")
    public int currenciesCount;

    @SerializedName("active_assets")
    public int assetsCount;

    @SerializedName("active_markets")
    public int marketsCount;

    @SerializedName("last_updated")
    public long timestamp;
}
