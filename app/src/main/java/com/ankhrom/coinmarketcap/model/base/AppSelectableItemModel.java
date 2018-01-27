package com.ankhrom.coinmarketcap.model.base;

import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.model.SelectableItemModel;
import com.ankhrom.coinmarketcap.BR;

/**
 * Created by R' on 1/27/2018.
 */

public abstract class AppSelectableItemModel extends SelectableItemModel {

    public AppSelectableItemModel() {
    }

    public AppSelectableItemModel(OnItemSelectedListener itemSelectedListener) {
        super(itemSelectedListener);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
