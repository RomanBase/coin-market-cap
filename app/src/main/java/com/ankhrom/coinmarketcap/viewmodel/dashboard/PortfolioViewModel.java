package com.ankhrom.coinmarketcap.viewmodel.dashboard;

import android.view.View;

import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.data.DataLoadingListener;
import com.ankhrom.coinmarketcap.databinding.PortfolioPageBinding;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.listener.OnPortfolioChangedListener;
import com.ankhrom.coinmarketcap.model.PortfolioAdapterModel;
import com.ankhrom.coinmarketcap.model.PortfolioItemModel;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.coinmarketcap.viewmodel.portfolio.PortfolioPlusViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 1/1/2018.
 */

public class PortfolioViewModel extends AppViewModel<PortfolioPageBinding, PortfolioAdapterModel> implements DataLoadingListener, OnPortfolioChangedListener {

    @Override
    public void onInit() {
        super.onInit();

        headerTitle.set("Portfolio");

        DataHolder holder = getFactory().get(DataHolder.class);
        holder.getFetcher().addListener(this);

        UserPrefs prefs = getFactory().get(UserPrefs.class);
        prefs.setPortfolioChangedListener(this);
    }

    public void onAddPressed(View view) {

        addViewModel(PortfolioPlusViewModel.class);
    }

    private void updatePortfolio(List<PortfolioCoin> portfolio) {

        List<PortfolioItemModel> items = new ArrayList<>();
        double invested = 0.0;
        double current = 0.0;

        DataHolder holder = getFactory().get(DataHolder.class);

        for (PortfolioCoin item : portfolio) {

            CoinItem coin = holder.getCoin(item.coinId);

            if (coin != null) {

                PortfolioItemModel model = new PortfolioItemModel(coin, item.items);
                invested += model.invested;
                current += model.current;

                items.add(model);
            }
        }

        double profit = current / invested;

        if (profit > 1.0f) {
            profit -= 1.0;
        } else {
            profit = -(1.0f - profit);
        }

        headerSubtitle.set(ApiFormat.toDigitFormat(invested * profit));
        headerInfo.set(ApiFormat.toDigitFormat(current));
        headerSubinfo.set(ApiFormat.toDigitFormat(invested));

        setModel(new PortfolioAdapterModel(getContext(), items));
    }

    @Override
    public void onPortfolioChanged(List<PortfolioCoin> portfolio) {

        updatePortfolio(portfolio);
    }

    @Override
    public void onDataLoading(boolean isLoading, DataHolder holder) {

        this.isLoading.set(isLoading);

        if (!isLoading) {
            updatePortfolio(getFactory().get(UserPrefs.class).getPortfolio());
        }
    }

    @Override
    public void onDataLoadingFailed(boolean isLoading, DataHolder holder) {

        this.isLoading.set(false);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.portfolio_page;
    }
}
