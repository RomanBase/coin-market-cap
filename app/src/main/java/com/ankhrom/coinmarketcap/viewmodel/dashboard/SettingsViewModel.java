package com.ankhrom.coinmarketcap.viewmodel.dashboard;

import android.view.View;

import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.viewmodel.auth.ThirdPartyLoginViewModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

/**
 * Created by R' on 1/1/2018.
 */

public class SettingsViewModel extends AppViewModel {

    @Override
    public void onInit() {
        super.onInit();

        if (model != null) {
            return;
        }
    }

    public void onHitBTCPressed(View view) {

        addViewModel(ThirdPartyLoginViewModel.class);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.settings_page;
    }
}
