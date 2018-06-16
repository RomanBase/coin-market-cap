package com.ankhrom.coinmarketcap.model.auth;

import android.databinding.ObservableBoolean;
import android.view.View;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.interfaces.OnValueChangedListener;
import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.base.observable.ViewGroupObservable;
import com.ankhrom.coinmarketcap.common.ExchangeType;

/**
 * Created by R' on 6/8/2018.
 */
public class EtherWalletLoginModel extends ThirdPartyLoginModel {

    public final EditTextObservable address = new EditTextObservable();
    public final EditTextObservable contract = new EditTextObservable();

    public final ObservableBoolean contractEdit = new ObservableBoolean();
    public final ObservableBoolean contractEnable = new ObservableBoolean();

    public final ViewGroupObservable<EtherContractItemModel> contracts = new ViewGroupObservable<>();

    public EtherWalletLoginModel(ExchangeType type) {
        super(type);

        address.setOnValueChangedListener(addressValueListener);
        contract.setOnValueChangedListener(contractValueListener);
    }

    private final OnValueChangedListener<String> contractValueListener = new OnValueChangedListener<String>() {
        @Override
        public void onValueChanged(String value) {

            contractEdit.set(!StringHelper.isEmpty(value));
        }
    };

    private final OnValueChangedListener<String> addressValueListener = new OnValueChangedListener<String>() {
        @Override
        public void onValueChanged(String value) {

            contractEnable.set(!StringHelper.isEmpty(value));
        }
    };
}
