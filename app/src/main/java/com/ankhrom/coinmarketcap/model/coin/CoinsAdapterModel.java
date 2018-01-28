package com.ankhrom.coinmarketcap.model.coin;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.base.model.ItemModel;
import com.ankhrom.coinmarketcap.BR;

import java.util.Collection;

/**
 * Created by R' on 12/30/2017.
 */

public class CoinsAdapterModel extends AdapterModel<ItemModel> {

    public final ObservableBoolean isEmpty = new ObservableBoolean();

    public CoinsAdapterModel(Context context) {
        super(context);

        adapter.addOnDataSetChangedListener(datasetChangedListener);
    }

    public CoinsAdapterModel(Context context, Collection collection) {
        super(context, collection);

        adapter.addOnDataSetChangedListener(datasetChangedListener);
    }

    private final ObservableList.OnListChangedCallback<ObservableList<ItemModel>> datasetChangedListener = new ObservableList.OnListChangedCallback<ObservableList<ItemModel>>() {

        @Override
        public void onChanged(ObservableList<ItemModel> itemModels) {

        }

        @Override
        public void onItemRangeChanged(ObservableList<ItemModel> itemModels, int i, int i1) {

        }

        @Override
        public void onItemRangeInserted(ObservableList<ItemModel> itemModels, int i, int i1) {
            isEmpty.set(false);
        }

        @Override
        public void onItemRangeMoved(ObservableList<ItemModel> itemModels, int i, int i1, int i2) {

        }

        @Override
        public void onItemRangeRemoved(ObservableList<ItemModel> itemModels, int i, int i1) {
            isEmpty.set(adapter.getItemCount() <= 1);
        }
    };

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
