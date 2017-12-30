package com.ankhrom.coinmarketcap.model;

import android.content.Context;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.coinmarketcap.BR;

import java.util.Collection;

/**
 * Created by R' on 12/30/2017.
 */

public class DashboardModel extends AdapterModel<CoinItemModel> {

    public DashboardModel(Context context) {
        super(context);
    }

    public DashboardModel(Context context, Collection<CoinItemModel> collection) {
        super(context, collection);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
