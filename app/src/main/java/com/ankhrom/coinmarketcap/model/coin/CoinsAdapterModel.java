package com.ankhrom.coinmarketcap.model.coin;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.model.base.SortableCoinItemModel;

import java.util.Collection;

/**
 * Created by R' on 12/30/2017.
 */

public class CoinsAdapterModel extends AdapterModel<SortableCoinItemModel> {

    public final ObservableBoolean isEmpty = new ObservableBoolean();

    public CoinsAdapterModel(Context context) {
        super(context);

        adapter.addOnDataSetChangedListener(datasetChangedListener);
    }

    public CoinsAdapterModel(Context context, Collection collection) {
        super(context, collection);

        adapter.addOnDataSetChangedListener(datasetChangedListener);
    }

    private final ObservableList.OnListChangedCallback<ObservableList<SortableCoinItemModel>> datasetChangedListener = new ObservableList.OnListChangedCallback<ObservableList<SortableCoinItemModel>>() {

        @Override
        public void onChanged(ObservableList<SortableCoinItemModel> itemModels) {

        }

        @Override
        public void onItemRangeChanged(ObservableList<SortableCoinItemModel> itemModels, int i, int i1) {

        }

        @Override
        public void onItemRangeInserted(ObservableList<SortableCoinItemModel> itemModels, int i, int i1) {
            isEmpty.set(false);
        }

        @Override
        public void onItemRangeMoved(ObservableList<SortableCoinItemModel> itemModels, int i, int i1, int i2) {

        }

        @Override
        public void onItemRangeRemoved(ObservableList<SortableCoinItemModel> itemModels, int i, int i1) {
            isEmpty.set(adapter.getItemCount() <= 1);
        }
    };

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
