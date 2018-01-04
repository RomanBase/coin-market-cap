package com.ankhrom.hitbtc;

import com.ankhrom.hitbtc.entity.HitBalance;

import java.util.Iterator;
import java.util.List;

/**
 * Created by romanhornak on 1/4/18.
 */

public final class HitFilter {


    public List<HitBalance> filterZeroBalance(List<HitBalance> balances) {

        Iterator<HitBalance> iterator = balances.iterator();
        while (iterator.hasNext()) {

            HitBalance balance = iterator.next();
            if (balance.available.equals("0") && balance.reserved.equals("0")) {
                iterator.remove();
            }
        }

        return balances;
    }
}
