package com.ankhrom.etherscan;

/**
 * Created by R' on 6/7/2018.
 */
public final class EtherApiParam {

    public static final String module = "module";
    public static final String action = "action";
    public static final String address = "address";
    public static final String contact = "contractaddress";
    public static final String tag = "tag";
    public static final String page = "page";
    public static final String sort = "sort";
    public static final String offset = "offset";
    public static final String key = "apiKey";

    public static final String ok = "OK";
    public static final Double VALUE_MULTIPLIER = 1000000000000000000.0;

    public static final String CURRENCY = "ETH";

    public static final class Module {

        public static final String account = "account";
        public static final String contract = "contract";
        public static final String block = "block";
        public static final String log = "logs";
        public static final String stats = "stats";
    }

    public static final class Action {

        public static final String balance = "balance";
        public static final String balanceToken = "tokenbalance";
        public static final String abi = "getabi";
        public static final String balanceMulti = "balancemulti";
        public static final String transactions = "txlist";
        public static final String transactionsInternal = "txlistinternal";
        public static final String transactionsToken = "tokentx";
    }
}
