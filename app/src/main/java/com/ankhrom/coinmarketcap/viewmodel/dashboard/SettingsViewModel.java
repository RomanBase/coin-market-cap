package com.ankhrom.coinmarketcap.viewmodel.dashboard;

import android.view.View;

import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.databinding.SettingsPageBinding;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.model.settings.SettingsExchangeItemModel;
import com.ankhrom.coinmarketcap.model.settings.SettingsModel;
import com.ankhrom.coinmarketcap.viewmodel.auth.ThirdPartyLoginViewModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
            List<PortfolioCoin> coins = getUserPrefs().getPortfolio(type);

            SettingsExchangeItemModel item = new SettingsExchangeItemModel(type);
            item.setOnItemSelectedListener(this);

            long timestamp = getExchangePrefs().getTimestamp(type);

            if (timestamp > 0) {

                item.note.set(new Date(timestamp).toLocaleString());

                if (credentials.isValid()) {
                    item.state.set(String.format(Locale.US, "active (%s)", coins.size()));
                } else {
                    item.state.set(String.format(Locale.US, "sync once (%s)", coins.size()));
                }

            } else {
                item.state.set("inactive");
                item.note.set("CONNECT");
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
