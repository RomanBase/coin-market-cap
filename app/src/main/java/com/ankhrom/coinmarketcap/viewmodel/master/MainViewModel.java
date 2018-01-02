package com.ankhrom.coinmarketcap.viewmodel.master;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;

import com.ankhrom.base.interfaces.viewmodel.ViewModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dashboard.MarketViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dashboard.PortfolioViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dashboard.SearchViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dashboard.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 12/31/2017.
 */

public class MainViewModel extends AppViewModel implements BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener {

    public final ObservableField<ViewModel> currentViewModel = new ObservableField<>();

    private List<ViewModel> viewModels;

    @Override
    public void onInit() {
        super.onInit();

        viewModels = initViewModels();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.post(new Runnable() {
            @Override
            public void run() {
                setCurrentPage(0);
            }
        });
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_market:
                setCurrentPage(0);
                ((MarketViewModel) currentViewModel.get()).changeState(MarketViewModel.ListState.NORMAL);
                break;
            case R.id.menu_favourites:
                setCurrentPage(0);
                ((MarketViewModel) currentViewModel.get()).changeState(MarketViewModel.ListState.FAVOURITES);
                break;
            case R.id.menu_search:
                setCurrentPage(2);
                break;
            case R.id.menu_porfolio:
                setCurrentPage(3);
                break;
            case R.id.menu_settings:
                setCurrentPage(4);
                break;
        }

        return true;
    }

    protected List<ViewModel> initViewModels() {

        List<ViewModel> viewModels = new ArrayList<>();
        viewModels.add(getFactory().getViewModel(MarketViewModel.class, MarketViewModel.ListState.NORMAL));
        viewModels.add(getFactory().getViewModel(SearchViewModel.class));
        viewModels.add(getFactory().getViewModel(SearchViewModel.class));
        viewModels.add(getFactory().getViewModel(PortfolioViewModel.class));
        viewModels.add(getFactory().getViewModel(SettingsViewModel.class));

        return viewModels;
    }

    protected void setCurrentPage(int index) {

        if (index < 0) {
            index = 0;
        } else if (index > viewModels.size() - 1) {
            index = viewModels.size() - 1;
        }

        ViewModel vm = viewModels.get(index);

        currentViewModel.set(vm);

        getNavigation().replaceViewModel(vm, R.id.content_container);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main_page;
    }

    @Override
    public int getBindingResource() {
        return BR.VM;
    }
}
