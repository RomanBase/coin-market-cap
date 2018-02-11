package com.ankhrom.gdax;

import com.ankhrom.gdax.entity.CoinbaseAccount;
import com.ankhrom.gdax.entity.GdaxAccount;

import java.util.Iterator;
import java.util.List;

/**
 * Created by R' on 2/10/2018.
 */

public final class GdaxFilter {

    public static List<GdaxAccount> filterZeroAccounts(List<GdaxAccount> accounts) {

        Iterator<GdaxAccount> iterator = accounts.iterator();
        while (iterator.hasNext()) {
            GdaxAccount account = iterator.next();

            if (Double.parseDouble(account.balance) < 0.0000001) {
                iterator.remove();
            }
        }

        return accounts;
    }

    public static List<CoinbaseAccount> filterZeroCoinbaseAccounts(List<CoinbaseAccount> accounts) {

        Iterator<CoinbaseAccount> iterator = accounts.iterator();
        while (iterator.hasNext()) {
            CoinbaseAccount account = iterator.next();

            if (Double.parseDouble(account.balance) < 0.0000001) {
                iterator.remove();
            }
        }

        return accounts;
    }
}
