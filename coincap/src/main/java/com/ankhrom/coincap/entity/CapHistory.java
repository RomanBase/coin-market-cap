package com.ankhrom.coincap.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by R' on 2/1/2018.
 */

public class CapHistory {

    @SerializedName("market_cap")
    public CapHistoryItem marketCap;

    public CapHistoryItem price;

    public CapHistoryItem volume;
}
