package com.ankhrom.binance;

import com.ankhrom.binance.entity.BinBalance;

import java.util.Iterator;
import java.util.List;

/**
 * Created by R' on 1/26/2018.
 */

public class BinFilter {

    public static List<BinBalance> filterZeroBalance(List<BinBalance> balances) {

        Iterator<BinBalance> iterator = balances.iterator();
        while (iterator.hasNext()) {

            BinBalance balance = iterator.next();
            if (!(Double.parseDouble(balance.available) > 0.0)) {
                iterator.remove();
            }
        }

        return balances;
    }
}
