package com.ankhrom.coinmarketcap.model;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.interfaces.OnValueChangedListener;
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

    public PortfolioPlusModel() {

        units.setOnValueChangedListener(onUnitCountChanged);
        unitPrice.setOnValueChangedListener(onUnitPriceChanged);
        sumPrice.setOnValueChangedListener(onSumPriceChanged);
    }

    public final OnValueChangedListener<String> onUnitCountChanged = new OnValueChangedListener<String>() {
        @Override
        public void onValueChanged(String value) {

            if (StringHelper.isEmpty(value)) {
                return;
            }

            value = ensureFormat(value);

            boolean sumAvailable = !StringHelper.isEmpty(sumPrice.get());
            boolean unitAvailable = !StringHelper.isEmpty(unitPrice.get());

            if (sumAvailable) {
                unitPrice.setValue(String.valueOf(Double.parseDouble(sumPrice.get()) / Double.parseDouble(value)));
            } else if (unitAvailable) {
                sumPrice.setValue(String.valueOf(Double.parseDouble(unitPrice.get()) * Double.parseDouble(value)));
            }
        }
    };

    public final OnValueChangedListener<String> onUnitPriceChanged = new OnValueChangedListener<String>() {
        @Override
        public void onValueChanged(String value) {

            if (StringHelper.isEmpty(value) || StringHelper.isEmpty(units.get())) {
                sumPrice.setValue(null);
                return;
            }

            value = ensureFormat(value);

            double price = Double.parseDouble(value);
            double amount = Double.parseDouble(units.get());

            sumPrice.setValue(String.valueOf(price * amount));
        }
    };

    public final OnValueChangedListener<String> onSumPriceChanged = new OnValueChangedListener<String>() {
        @Override
        public void onValueChanged(String value) {

            if (StringHelper.isEmpty(value) || StringHelper.isEmpty(units.get())) {
                unitPrice.setValue(null);
                return;
            }

            value = ensureFormat(value);

            double price = Double.parseDouble(value);
            double amount = Double.parseDouble(units.get());

            unitPrice.setValue(String.valueOf(price / amount));
        }
    };

    private String ensureFormat(String value) {

        return value.startsWith(".") || value.startsWith(",") ? value + "0" : value;
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
