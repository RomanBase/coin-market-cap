package com.ankhrom.hitbtc;

import com.ankhrom.hitbtc.entity.HitBalance;
import com.ankhrom.hitbtc.entity.HitCurrencyTicker;

import java.util.Iterator;
import java.util.List;

/**
 * Created by romanhornak on 1/4/18.
 */

public final class HitFilter {

    public static List<HitBalance> filterZeroBalance(List<HitBalance> balances) {

        Iterator<HitBalance> iterator = balances.iterator();
        while (iterator.hasNext()) {

            HitBalance balance = iterator.next();
            if (balance.available.equals("0") && balance.reserved.equals("0")) {
                iterator.remove();
            }
        }

        return balances;
    }

    public static List<HitCurrencyTicker> filterByCurrency(List<HitCurrencyTicker> tickers, String currency) {

        Iterator<HitCurrencyTicker> iterator = tickers.iterator();
        while (iterator.hasNext()) {

            HitCurrencyTicker ticker = iterator.next();
            if (!ticker.symbol.endsWith(currency)) {
                iterator.remove();
            }
        }

        return tickers;
    }
}
