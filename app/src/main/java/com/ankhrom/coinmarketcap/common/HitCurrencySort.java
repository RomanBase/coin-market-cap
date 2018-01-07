package com.ankhrom.coinmarketcap.common;

import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.hitbtc.entity.HitCurrencyTicker;

import java.util.Comparator;

/**
 * Created by R' on 1/7/2018.
 */

public class HitCurrencySort implements Comparator<HitCurrencyTicker> {

    private final DataHolder holder;

    public HitCurrencySort(DataHolder holder) {
        this.holder = holder;
    }

    @Override
    public int compare(HitCurrencyTicker a, HitCurrencyTicker b) {

        CoinItem coinA = holder.getCoinBySymbol(a.symbol.substring(0, a.symbol.length() - 3));
        CoinItem coinB = holder.getCoinBySymbol(b.symbol.substring(0, b.symbol.length() - 3));

        if (coinA == null) {
            return coinB == null ? 0 : 1;
        }

        if (coinB == null) {
            return -1;
        }

        return Integer.parseInt(coinA.rank) > Integer.parseInt(coinB.rank) ? 1 : -1;
    }
}
