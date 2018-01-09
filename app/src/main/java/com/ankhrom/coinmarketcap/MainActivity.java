package com.ankhrom.coinmarketcap;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ankhrom.base.Base;
import com.ankhrom.base.BaseActivity;
import com.ankhrom.base.BaseFactory;
import com.ankhrom.base.interfaces.viewmodel.ViewModelObserver;
import com.ankhrom.base.viewmodel.BaseViewModelObserver;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.prefs.ExchangePrefs;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;
import com.ankhrom.coinmarketcap.viewmodel.master.MainViewModel;
import com.ankhrom.hitbtc.HitBTC;

public class MainActivity extends BaseActivity {

    @Override
    protected boolean onPreInit() {

        Base.debug = true;

        return super.onPreInit();
    }

    @Override
    protected ViewModelObserver init() {

        BaseFactory factory = BaseFactory.init(this);
        factory.add(new UserPrefs(this));
        factory.add(new ExchangePrefs(this));
        factory.add(DataHolder.init(factory));

        factory.add(HitBTC.init(factory.getRequestQueue()).auth("252d13df5fb7d277c6c6de185c18bb65", "6c60036d5a2655be6e988af8fa385be0"));

        return BaseViewModelObserver.with(factory, R.id.root_container)
                .setViewModel(MainViewModel.class)
                .build();
    }

    @Override
    protected void onPostInit(Bundle state, ViewDataBinding binding) {
        super.onPostInit(state, binding);

        HitBTC hitBTC = getViewModelObserver().getFactory().get(HitBTC.class);

        hitBTC.trading.order();
    }

    @Override
    protected int getMainLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }
}
