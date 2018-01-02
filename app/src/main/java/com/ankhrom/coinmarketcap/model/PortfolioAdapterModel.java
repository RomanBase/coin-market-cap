package com.ankhrom.coinmarketcap.model;

import android.content.Context;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.coinmarketcap.BR;

import java.util.Collection;

/**
 * Created by R' on 1/2/2018.
 */

public class PortfolioAdapterModel extends AdapterModel<PortfolioItemModel> {

    public PortfolioAdapterModel(Context context, Collection<PortfolioItemModel> collection) {
        super(context, collection);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
