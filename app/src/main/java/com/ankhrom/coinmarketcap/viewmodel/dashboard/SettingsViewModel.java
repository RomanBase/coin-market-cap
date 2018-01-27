package com.ankhrom.coinmarketcap.viewmodel.dashboard;

import android.view.View;

import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.data.DataFetcher;
import com.ankhrom.coinmarketcap.databinding.SettingsPageBinding;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.listener.DataExchangeLoadingListener;
import com.ankhrom.coinmarketcap.model.settings.SettingsExchangeItemModel;
import com.ankhrom.coinmarketcap.model.settings.SettingsModel;
import com.ankhrom.coinmarketcap.viewmodel.auth.ThirdPartyLoginViewModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dialog.FeatureRequestViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by R' on 1/1/2018.
 */

public class SettingsViewModel extends AppViewModel<SettingsPageBinding, SettingsModel> implements OnItemSelectedListener<SettingsExchangeItemModel>, DataExchangeLoadingListener {

    @Override
    public void onInit() {
        super.onInit();

        headerTitle.set("Account");

        if (model != null) {
            return;
        }

        DataFetcher fetcher = getDataHolder().getFetcher();
        fetcher.addExchangeListener(this);

        List<SettingsExchangeItemModel> exchanges = new ArrayList<>();
        ExchangeType[] types = new ExchangeType[ExchangeType.values().length - 1];
        System.arraycopy(ExchangeType.values(), 1, types, 0, types.length);

        for (ExchangeType type : types) {

            SettingsExchangeItemModel item = new SettingsExchangeItemModel(type);
            item.setOnItemSelectedListener(this);
            item.isLoading.set(fetcher.isLoading(type));

            setExchangeState(item);

            exchanges.add(item);
        }

        setModel(new SettingsModel(getContext(), exchanges));
    }

    private void setExchangeState(SettingsExchangeItemModel item) {

        AuthCredentials credentials = getExchangePrefs().getAuth(item.type);
        List<PortfolioCoin> coins = getUserPrefs().getPortfolio(item.type);

        long timestamp = getExchangePrefs().getTimestamp(item.type);

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
    }

    @Override
    public void onExchangeLoading(ExchangeType exchange, boolean isLoading, boolean isValid) {

        if (isModelAvailable()) {

            List<SettingsExchangeItemModel> items = model.adapter.getItems();

            for (SettingsExchangeItemModel item : items) {
                if (item.type.equals(exchange)) {

                    item.isLoading.set(isLoading);
                    setExchangeState(item);

                    break;
                }
            }
        }
    }

    @Override
    public void onItemSelected(View view, SettingsExchangeItemModel model) {

        addViewModel(ThirdPartyLoginViewModel.class, model.type);
    }

    public void onFeatureRequestPressed(View view) {

        addViewModel(FeatureRequestViewModel.class);
    }

    public void onDonatePressed(View view) {

    }

    @Override
    public int getLayoutResource() {
        return R.layout.settings_page;
    }

}
