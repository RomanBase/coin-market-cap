package com.ankhrom.coinmarketcap.entity;

import com.ankhrom.base.common.statics.StringHelper;

/**
 * Created by romanhornak on 1/4/18.
 */

public class AuthCredentials {

    public String key;
    public String secret;
    public boolean persist;

    public boolean isValid() {

        return !(StringHelper.isEmpty(key) && StringHelper.isEmpty(secret));
    }
}
