package com.ankhrom.coinmarketcap.model;

import android.content.Context;
import android.databinding.ObservableBoolean;

import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.base.model.ItemModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.model.coin.CoinAdapterFooterItemModel;

import java.util.Collection;

/**
 * Created by R' on 1/2/2018.
 */

public class PortfolioAdapterModel extends AdapterModel<ItemModel> {

    public ObservableBoolean isEmpty = new ObservableBoolean();

    private OnItemSelectedListener listener;

    public PortfolioAdapterModel(Context context) {
        super(context);

        isEmpty.set(true);
    }

    public void setOnAddItemPressedListener(OnItemSelectedListener listener) {

        this.listener = listener;
    }

    public void replace(Collection<? extends ItemModel> collection) {

        isEmpty.set(collection.size() == 0);
        adapter.replace(collection);
        adapter.add(new CoinAdapterFooterItemModel(listener));
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }

    public void checkEmptiness() {

        isEmpty.set(adapter.getItemCount() <= 1/*footer*/);
    }

    public void checkFooter() {

        if (adapter.getItemCount() == 0 || !(adapter.get(adapter.getItemCount() - 1) instanceof CoinAdapterFooterItemModel)) {
            adapter.add(new CoinAdapterFooterItemModel(listener));
        }
    }
}
