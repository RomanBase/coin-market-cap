package com.ankhrom.coinmarketcap.model;

import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.base.observable.SeekBarObservable;
import com.ankhrom.coinmarketcap.entity.CoinItem;

/**
 * Created by R' on 1/8/2018.
 */

public class CalcModel extends PortfolioPlusModel {

    public final EditTextObservable profitSumPrice = new EditTextObservable();
    public final EditTextObservable profitUnitPrice = new EditTextObservable();

    public final SeekBarObservable profit = new SeekBarObservable(0.1f);
    public final ObservableString profitLabel = new ObservableString();

    public final ObservableString marketCap = new ObservableString();
    public final ObservableString marketCapGrow = new ObservableString();

    public final ObservableString change24h = new ObservableString();
    public final ObservableString change1h = new ObservableString();
    public final ObservableString change7d = new ObservableString();

    public CalcModel() {

    }

    public void setPercenageChange(CoinItem coin) {

        change24h.set(coin.percentChange24h + "%");
        change1h.set(coin.percentChange1h + "%");
        change7d.set(coin.percentChange7d + "%");
    }

}
