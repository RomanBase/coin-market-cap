package com.ankhrom.coinmarketcap.model.trade;

import android.content.Context;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.coinmarketcap.BR;

import java.util.Collection;

/**
 * Created by R' on 1/6/2018.
 */

public class TradeAdapterModel extends AdapterModel<TradeItemModel> {

    public TradeAdapterModel(Context context) {
        super(context);
    }

    public TradeAdapterModel(Context context, Collection<TradeItemModel> collection) {
        super(context, collection);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
