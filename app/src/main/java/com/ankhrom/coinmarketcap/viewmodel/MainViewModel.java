package com.ankhrom.coinmarketcap.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.ankhrom.base.viewmodel.BaseViewModel;
import com.ankhrom.base.viewmodel.CompositeViewModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 12/31/2017.
 */

public class MainViewModel extends CompositeViewModel implements BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onModelCreated();
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_market:
                setCurrentPage(0, true);
                break;
            case R.id.menu_favourites:
                setCurrentPage(1, true);
                break;
            case R.id.menu_search:
                setCurrentPage(2, true);
                break;
            case R.id.menu_porfolio:
                setCurrentPage(3, true);
                break;
            case R.id.menu_settings:
                setCurrentPage(4, true);
                break;
        }

        return true;
    }

    @Override
    protected List<BaseViewModel> initViews() {

        List<BaseViewModel> viewModels = new ArrayList<>();
        viewModels.add(getFactory().getViewModel(MarketViewModel.class));
        viewModels.add(getFactory().getViewModel(MarketViewModel.class));
        viewModels.add(getFactory().getViewModel(MarketViewModel.class));
        viewModels.add(getFactory().getViewModel(MarketViewModel.class));
        viewModels.add(getFactory().getViewModel(MarketViewModel.class));

        return viewModels;
    }

    @Override
    protected int getViewPagerId() {
        return R.id.pager;
    }

    @Override
    protected int getViewTabsId() {
        return 0;
    }

    @Override
    protected ViewPager.PageTransformer getPageTransformer() {
        return new FadePageTransformer();
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
