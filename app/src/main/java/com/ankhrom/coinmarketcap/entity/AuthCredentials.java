package com.ankhrom.coinmarketcap.entity;

import com.ankhrom.base.common.statics.StringHelper;

/**
 * Created by romanhornak on 1/4/18.
 */

public class AuthCredentials {

    public String key;
    public String secret;
    public boolean persist = true;

    public AuthCredentials() {

    }

    public AuthCredentials(String keySecret) {

        if (keySecret.contains(":")) {
            String[] data = keySecret.split(":");
            key = data[0];
            secret = data[1];
        }
    }

    public AuthCredentials(String key, String secret) {
        this.key = key;
        this.secret = secret;
    }

    public boolean isValid() {

        return !(StringHelper.isEmpty(key) && StringHelper.isEmpty(secret));
    }
}
