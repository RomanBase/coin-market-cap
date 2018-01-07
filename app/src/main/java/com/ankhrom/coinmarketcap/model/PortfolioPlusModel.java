package com.ankhrom.coinmarketcap.model;

import android.databinding.ObservableBoolean;

import com.ankhrom.base.model.Model;
import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.coinmarketcap.BR;

/**
 * Created by R' on 1/2/2018.
 */

public class PortfolioPlusModel extends Model {

    public final ObservableString currency = new ObservableString();
    public final EditTextObservable units = new EditTextObservable();
    public final EditTextObservable unitPrice = new EditTextObservable();
    public final EditTextObservable sumPrice = new EditTextObservable();
    public final EditTextObservable bitcoinUnits = new EditTextObservable();
    public final EditTextObservable bitcoinUnitValue = new EditTextObservable();

    public final ObservableBoolean editableCurrency = new ObservableBoolean(true);

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
