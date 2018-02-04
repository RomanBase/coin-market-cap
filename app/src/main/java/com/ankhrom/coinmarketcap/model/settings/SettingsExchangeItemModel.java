package com.ankhrom.coinmarketcap.model.settings;

import android.databinding.ObservableBoolean;

import com.ankhrom.base.model.SelectableItemModel;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.common.ExchangeTypeRes;

/**
 * Created by R' on 1/26/2018.
 */

public class SettingsExchangeItemModel extends SelectableItemModel {

    public final ExchangeType type;
    public final int icon;

    public ObservableBoolean isLoading = new ObservableBoolean();
    public ObservableString state = new ObservableString();
    public ObservableString note = new ObservableString();

    public SettingsExchangeItemModel(ExchangeType type) {

        this.type = type;
        this.icon = ExchangeTypeRes.getLogo(type);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.settings_exchange_item;
    }
}
