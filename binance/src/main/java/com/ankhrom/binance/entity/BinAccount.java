package com.ankhrom.binance.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by R' on 1/26/2018.
 */

public class BinAccount {

    @SerializedName("updateTime")
    public long timestamp;

    public List<BinBalance> balances;
}
