package com.ankhrom.coinmarketcap.model;

import android.content.Context;
import android.databinding.ObservableBoolean;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.coinmarketcap.BR;

import java.util.Collection;

/**
 * Created by R' on 1/2/2018.
 */

public class PortfolioAdapterModel extends AdapterModel<PortfolioItemModel> {

    public ObservableBoolean isEmpty = new ObservableBoolean();

    public PortfolioAdapterModel(Context context) {
        super(context);

        isEmpty.set(true);
    }

    public PortfolioAdapterModel(Context context, Collection<PortfolioItemModel> collection) {
        super(context, collection);

        isEmpty.set(collection.size() == 0);
    }

    public void replace(Collection<PortfolioItemModel> collection) {

        isEmpty.set(collection.size() == 0);
        adapter.replace(collection);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
