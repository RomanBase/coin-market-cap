package com.ankhrom.coinmarketcap.viewmodel.master;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;

import com.ankhrom.base.model.Model;
import com.ankhrom.coinmarketcap.AppCode;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.databinding.ActivityMainPageBinding;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dashboard.CalcViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dashboard.MarketViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dashboard.PortfolioViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dashboard.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 12/31/2017.
 */

public class MainViewModel extends AppViewModel<ActivityMainPageBinding, Model> implements BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener {

    public final ObservableField<AppViewModel> currentViewModel = new ObservableField<>();

    private List<AppViewModel> viewModels;
    private int currentPageIndex = -1;

    @Override
    public void onInit() {
        super.onInit();

        viewModels = initViewModels();
    }

    @Override
    public boolean onBackPressed() {

        boolean defaultPage = currentViewModel.get() instanceof MarketViewModel && ((MarketViewModel) currentViewModel.get()).getListState() == MarketViewModel.ListState.NORMAL;

        if (!defaultPage && binding != null) {

            binding.bottomNavigation.setCurrentItem(0);

            return true;
        }

        return super.onBackPressed();
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
                setCurrentPage(0, MarketViewModel.ListState.NORMAL);
                break;
            case R.id.menu_favourites:
                setCurrentPage(0, MarketViewModel.ListState.FAVOURITES);
                break;
            case R.id.menu_search:
                setCurrentPage(1);
                break;
            case R.id.menu_porfolio:
                setCurrentPage(2);
                break;
            case R.id.menu_settings:
                setCurrentPage(3);
                break;
        }

        return true;
    }

    protected List<AppViewModel> initViewModels() {

        List<AppViewModel> viewModels = new ArrayList<>();
        viewModels.add(getFactory().getViewModel(MarketViewModel.class));
        viewModels.add(getFactory().getViewModel(CalcViewModel.class));
        viewModels.add(getFactory().getViewModel(PortfolioViewModel.class));
        viewModels.add(getFactory().getViewModel(SettingsViewModel.class));

        return viewModels;
    }

    protected void setCurrentPage(int index, Object... args) {

        if (currentPageIndex == index && currentViewModel.get() != null) {
            currentViewModel.get().onReceiveArgs(AppCode.STATE, args);
            return;
        }

        if (index < 0) {
            index = 0;
        } else if (index > viewModels.size() - 1) {
            index = viewModels.size() - 1;
        }

        if (index == 0) { // TODO: 1/5/2018 item swipe listener / fragment state workaround
            viewModels.remove(0);
            viewModels.add(0, getFactory().getViewModel(MarketViewModel.class, args));
        }

        AppViewModel vm = viewModels.get(currentPageIndex = index);

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
