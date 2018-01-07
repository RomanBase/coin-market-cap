package com.ankhrom.coinmarketcap.model.dialog;

import android.content.Context;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.coinmarketcap.BR;

import java.util.Collection;

/**
 * Created by R' on 1/7/2018.
 */

public class SearchAdapterModel extends AdapterModel<SearchItemModel> {

    public final EditTextObservable fulltext = new EditTextObservable();

    public SearchAdapterModel(Context context) {
        super(context);
    }

    public SearchAdapterModel(Context context, Collection<SearchItemModel> collection) {
        super(context, collection);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
