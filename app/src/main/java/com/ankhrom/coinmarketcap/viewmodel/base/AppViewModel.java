package com.ankhrom.coinmarketcap.viewmodel.base;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.ankhrom.base.model.Model;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.base.viewmodel.BaseViewModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.prefs.ExchangePrefs;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;

/**
 * Created by R' on 1/1/2018.
 */

public abstract class AppViewModel<S extends ViewDataBinding, T extends Model> extends BaseViewModel<S, T> {

    public final AppViewModel ref = this;

    public final ObservableString headerTitle = new ObservableString();
    public final ObservableString headerSubTitle = new ObservableString();
    public final ObservableString headerInfo = new ObservableString();
    public final ObservableString headerSubInfo = new ObservableString();

    protected DataHolder getDataHolder() {

        return getFactory().get(DataHolder.class);
    }

    protected UserPrefs getUserPrefs() {

        return getFactory().get(UserPrefs.class);
    }

    protected ExchangePrefs getExchangePrefs() {

        return getFactory().get(ExchangePrefs.class);
    }

    public void onClosePressed(View view) {

        getNavigation().navigateBack();
    }

    @Override
    public int getBindingResource() {
        return BR.VM;
    }
}
