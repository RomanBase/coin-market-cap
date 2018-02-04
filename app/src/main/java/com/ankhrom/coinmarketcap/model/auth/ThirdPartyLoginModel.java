package com.ankhrom.coinmarketcap.model.auth;

import android.databinding.ObservableBoolean;

import com.ankhrom.base.model.Model;
import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.base.observable.ObservableCheckbox;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.common.ExchangeTypeUtil;

/**
 * Created by romanhornak on 1/4/18.
 */

public class ThirdPartyLoginModel extends Model {

    public final EditTextObservable key = new EditTextObservable();
    public final EditTextObservable secret = new EditTextObservable();
    public final ObservableCheckbox dontStore = new ObservableCheckbox();
    public final ObservableBoolean edit = new ObservableBoolean();

    public final int icon;

    public ThirdPartyLoginModel(ExchangeType type) {

        icon = ExchangeTypeUtil.getLogo(type);
    }

    public void presetEdit(String key, String secret) {

        this.key.set(key);
        this.secret.set(secret);
        this.edit.set(true);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
