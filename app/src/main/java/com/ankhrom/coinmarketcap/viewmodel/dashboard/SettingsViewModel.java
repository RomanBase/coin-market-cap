package com.ankhrom.coinmarketcap.viewmodel.dashboard;

import android.view.View;

import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.databinding.SettingsPageBinding;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.ankhrom.coinmarketcap.model.settings.SettingsExchangeItemModel;
import com.ankhrom.coinmarketcap.model.settings.SettingsModel;
import com.ankhrom.coinmarketcap.viewmodel.auth.ThirdPartyLoginViewModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 1/1/2018.
 */

public class SettingsViewModel extends AppViewModel<SettingsPageBinding, SettingsModel> implements OnItemSelectedListener<SettingsExchangeItemModel> {

    @Override
    public void onInit() {
        super.onInit();

        headerTitle.set("Account");

        if (model != null) {
            return;
        }

        List<SettingsExchangeItemModel> exchanges = new ArrayList<>();
        ExchangeType[] types = new ExchangeType[ExchangeType.values().length - 1];
        System.arraycopy(ExchangeType.values(), 1, types, 0, types.length);

        for (ExchangeType type : types) {
            AuthCredentials credentials = getExchangePrefs().getAuth(type);

            SettingsExchangeItemModel item = new SettingsExchangeItemModel(type);
            item.setOnItemSelectedListener(this);

            if (credentials.isValid()) {
                item.state.set("last sync time");
                item.note.set("");
            } else {

            }

            exchanges.add(item);
        }

        setModel(new SettingsModel(getContext(), exchanges));
    }

    @Override
    public void onItemSelected(View view, SettingsExchangeItemModel model) {

        addViewModel(ThirdPartyLoginViewModel.class, model.type);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.settings_page;
    }


}
