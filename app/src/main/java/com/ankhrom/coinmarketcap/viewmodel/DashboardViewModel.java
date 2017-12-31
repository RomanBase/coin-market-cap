package com.ankhrom.coinmarketcap.viewmodel;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.interfaces.ObjectConverter;
import com.ankhrom.base.viewmodel.BaseViewModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.CoinItem;
import com.ankhrom.coinmarketcap.api.MarketData;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.data.DataLoadingListener;
import com.ankhrom.coinmarketcap.databinding.DashboardPageBinding;
import com.ankhrom.coinmarketcap.model.CoinItemModel;
import com.ankhrom.coinmarketcap.model.DashboardModel;

import java.util.List;

/**
 * Created by R' on 12/30/2017.
 */

public class DashboardViewModel extends BaseViewModel<DashboardPageBinding, DashboardModel> implements DataLoadingListener, BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener {

    @Override
    public void onInit() {
        super.onInit();

        requestData();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {

    }

    private void requestData() {

        DataHolder holder = getFactory().get(DataHolder.class);
        holder.getFetcher().setListener(this);
    }

    private void updateModel(List<CoinItemModel> items, MarketData market) {

        setModel(new DashboardModel(getContext(), items, market));
    }

    private List<CoinItemModel> convertCoinItems(List<CoinItem> coins) {

        return ObjectHelper.convert(coins, new ObjectConverter<CoinItemModel, CoinItem>() {
            @Override
            public CoinItemModel convert(CoinItem object) {
                return new CoinItemModel(object);
            }
        });
    }

    @Override
    public void onDataLoading(boolean isLoading, DataHolder holder) {

        this.isLoading.set(isLoading);

        if (!isLoading) {
            updateModel(convertCoinItems(holder.getCoins()), holder.getMarket());
        }
    }

    @Override
    public void onDataLoadingFailed(boolean isLoading, DataHolder holder) {

        this.isLoading.set(isLoading);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dashboard_page;
    }

    @Override
    public int getBindingResource() {
        return BR.VM;
    }
}
