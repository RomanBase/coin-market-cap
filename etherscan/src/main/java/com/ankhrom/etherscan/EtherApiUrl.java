package com.ankhrom.etherscan;

/**
 * Created by R' on 6/7/2018.
 */
public class EtherApiUrl {

    public static final String BASE_URL = "https://api.etherscan.io/api";

    public static String get(String module, String action, String address, String key) {

        return BASE_URL + param(EtherApiParam.module, module) + param(EtherApiParam.action, action) + param(EtherApiParam.address, address) + param(EtherApiParam.key, key);
    }

    public static String param(String key, String value) {

        return "&" + key + "=" + value;
    }
}
