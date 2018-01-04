package com.ankhrom.coinmarketcap.viewmodel.auth;

import android.view.View;

import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

/**
 * Created by romanhornak on 1/4/18.
 */

public class ThirdPartyLoginViewModel extends AppViewModel {

    public enum ExchangeType {
        HIT_BTC,
        ETORO
    }

    public void onLoginPressed(View view) {

    }

    public void onCancelPressed(View view) {

    }

    @Override
    public int getLayoutResource() {
        return R.layout.third_party_login;
    }
}
