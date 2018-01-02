package com.ankhrom.coinmarketcap.viewmodel.dashboard;

import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.data.DataLoadingListener;
import com.ankhrom.coinmarketcap.databinding.PortfolioPageBinding;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.model.PortfolioAdapterModel;
import com.ankhrom.coinmarketcap.model.PortfolioItemModel;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 1/1/2018.
 */

public class PortfolioViewModel extends AppViewModel<PortfolioPageBinding, PortfolioAdapterModel> implements DataLoadingListener {

    @Override
    public void onInit() {
        super.onInit();

        DataHolder holder = getFactory().get(DataHolder.class);
        holder.getFetcher().addListener(this);
    }

    private void updatePortfolio(List<PortfolioCoin> portfolio, List<CoinItem> coins) {

        List<PortfolioItemModel> items = new ArrayList<>();

        DataHolder holder = getFactory().get(DataHolder.class);

        for (PortfolioCoin item : portfolio) {

            CoinItem coin = holder.getCoin(item.coinId);

            if (coin != null) {
                items.add(new PortfolioItemModel(coin, item.items));
            }
        }

        setModel(new PortfolioAdapterModel(getContext(), items));
    }

    @Override
    public void onDataLoading(boolean isLoading, DataHolder holder) {

        this.isLoading.set(isLoading);

        if (!isLoading) {
            updatePortfolio(getFactory().get(UserPrefs.class).getPortfolio(), holder.getCoins());
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
