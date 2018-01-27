package com.ankhrom.coinmarketcap.model.settings;

import android.content.Context;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.coinmarketcap.BR;

import java.util.Collection;

/**
 * Created by R' on 1/26/2018.
 */

public class SettingsModel extends AdapterModel<SettingsExchangeItemModel> {

    public SettingsModel(Context context) {
        super(context);
    }

    public SettingsModel(Context context, Collection<SettingsExchangeItemModel> collection) {
        super(context, collection);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
