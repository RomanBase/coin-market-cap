package com.ankhrom.coinmarketcap.viewmodel.base;

import android.databinding.ViewDataBinding;

import com.ankhrom.base.model.Model;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.base.viewmodel.BaseViewModel;
import com.ankhrom.coinmarketcap.BR;

/**
 * Created by R' on 1/1/2018.
 */

public abstract class AppViewModel<S extends ViewDataBinding, T extends Model> extends BaseViewModel<S, T> {

    public final ObservableString headerTitle = new ObservableString();
    public final ObservableString headerSubtitle = new ObservableString();
    public final ObservableString headerInfo = new ObservableString();
    public final ObservableString headerSubinfo = new ObservableString();

    @Override
    public int getBindingResource() {
        return BR.VM;
    }
}
