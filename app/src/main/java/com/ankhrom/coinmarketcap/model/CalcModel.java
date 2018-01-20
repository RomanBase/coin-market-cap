package com.ankhrom.coinmarketcap.model;

import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.base.observable.SeekBarObservable;

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

    public CalcModel() {

    }


}
