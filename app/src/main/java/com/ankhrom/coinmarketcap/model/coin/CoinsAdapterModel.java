package com.ankhrom.coinmarketcap.model.coin;

import android.content.Context;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.base.model.ItemModel;
import com.ankhrom.coinmarketcap.BR;

import java.util.Collection;

/**
 * Created by R' on 12/30/2017.
 */

public class CoinsAdapterModel extends AdapterModel<ItemModel> {

    public CoinsAdapterModel(Context context) {
        super(context);
    }

    public CoinsAdapterModel(Context context, Collection collection) {
        super(context, collection);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
