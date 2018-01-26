package com.ankhrom.binance.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by R' on 1/26/2018.
 */

public class BinBalance {

    @SerializedName("asset")
    public String currency;

    @SerializedName("free")
    public String available;

    public String locket;
}
