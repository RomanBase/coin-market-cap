package com.ankhrom.coinmarketcap;

import android.support.v7.widget.Toolbar;

import com.ankhrom.base.Base;
import com.ankhrom.base.BaseActivity;
import com.ankhrom.base.BaseFactory;
import com.ankhrom.base.interfaces.viewmodel.ViewModelObserver;
import com.ankhrom.base.viewmodel.BaseViewModelObserver;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.data.PortfolioHolder;
import com.ankhrom.coinmarketcap.prefs.ExchangePrefs;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;
import com.ankhrom.coinmarketcap.viewmodel.master.MainViewModel;

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
        factory.add(PortfolioHolder.init(factory));
        factory.add(DataHolder.init(factory));

        //Gdax.init(this).auth("7de06ade6286e81fd8c104824e4c6840", "XwsjLm0Th32B9QAw0JY3F5layFClo2+gNTofJQnHekpbwMqMksn2USqFNjBqFGuRVhjDu3eivS+qj2/tL+/SpQ==", "ahoy").getPortfolio(null);

        return BaseViewModelObserver.with(factory, R.id.root_container)
                .setViewModel(MainViewModel.class)
                .build();
    }

    @Override
    protected int getMainLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    @Override
    protected int getAppTheme() {
        return R.style.AppTheme;
    }
}
