package com.ankhrom.coinmarketcap.entity;

import com.ankhrom.base.common.statics.StringHelper;

public class TokenItem {

    public String symbol;
    public String address;
    public String error;

    public boolean isValid() {

        return !StringHelper.isEmpty(symbol);
    }
}
