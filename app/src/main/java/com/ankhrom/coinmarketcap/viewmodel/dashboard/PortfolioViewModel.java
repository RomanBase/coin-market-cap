package com.ankhrom.coinmarketcap.viewmodel.dashboard;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.listener.DataLoadingListener;
import com.ankhrom.coinmarketcap.databinding.PortfolioPageBinding;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;
import com.ankhrom.coinmarketcap.listener.OnExchangeAuthChangedListener;
import com.ankhrom.coinmarketcap.listener.OnExchangePortfolioChangedListener;
import com.ankhrom.coinmarketcap.listener.OnPortfolioChangedListener;
import com.ankhrom.coinmarketcap.model.PortfolioAdapterModel;
import com.ankhrom.coinmarketcap.model.PortfolioItemModel;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.coinmarketcap.viewmodel.portfolio.PortfolioEditViewModel;
import com.ankhrom.coinmarketcap.viewmodel.portfolio.PortfolioPlusViewModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by R' on 1/1/2018.
 */

public class PortfolioViewModel extends AppViewModel<PortfolioPageBinding, PortfolioAdapterModel> implements DataLoadingListener, OnPortfolioChangedListener, OnExchangePortfolioChangedListener, OnItemSelectedListener<PortfolioItemModel>, OnExchangeAuthChangedListener, SwipeRefreshLayout.OnRefreshListener {

    @Override
    public void onInit() {
        super.onInit();

        if (model != null) {
            return;
        }

        isLoading.set(true);

        headerTitle.set("Portfolio");

        DataHolder holder = getDataHolder();
        holder.getFetcher().addListener(this);

        UserPrefs prefs = getUserPrefs();
        prefs.setPortfolioChangedListener(this);
        prefs.setExchangePortfolioListener(this);

        getExchangePrefs().addExchangeAuthListener(this);
    }

    @Override
    public void onRefresh() {

        isLoading.set(true);

        binding.pullToRefresh.setRefreshing(false);
        model.adapter.clear();

        DataHolder holder = getDataHolder();
        holder.reload();
    }

    public void onAddPressed(View view) {

        addViewModel(PortfolioPlusViewModel.class);
    }

    @Override
    public void onItemSelected(View view, PortfolioItemModel model) {

        if (model == null) {
            return;
        }

        addViewModel(PortfolioEditViewModel.class, model, model.coin);
    }

    private void updatePortfolio(ExchangeType exchange, List<PortfolioCoin> portfolio) {

        if (model == null) {
            setModel(new PortfolioAdapterModel(getContext()));
        }

        List<PortfolioItemModel> items = removeExchange(exchange, model.adapter.getItems());

        if (portfolio != null && !portfolio.isEmpty()) {
            items = addExchange(exchange, items, portfolio);
        }

        if (items.size() > 0) {

            double invested = 0.0;
            double current = 0.0;

            for (PortfolioItemModel item : items) {
                invested += item.invested;
                current += item.current;
            }

            double profit = current / invested;

            if (profit > 1.0f) {
                profit -= 1.0;
            } else {
                profit = -(1.0f - profit);
            }

            double profitAmount = invested * profit;

            if (Math.abs(profitAmount) < 0.1) {
                headerSubTitle.set("- / -");
            } else {
                String profitValue = Math.abs(profitAmount) > 1.0 ? ApiFormat.toPriceFormat(profitAmount) : ApiFormat.toDigitFormat(profitAmount);
                headerSubTitle.set(profitValue + " / " + ApiFormat.toDigitFormat(profit * 100.0) + "%");
            }

            headerInfo.set(ApiFormat.toDigitFormat(current));
            headerSubInfo.set(ApiFormat.toDigitFormat(invested));

            Collections.sort(items, new Comparator<PortfolioItemModel>() {
                @Override
                public int compare(PortfolioItemModel a, PortfolioItemModel b) {
                    return a.current < b.current ? 1 : -1; //Integer.parseInt(a.coin.rank) > Integer.parseInt(b.coin.rank) ? 1 : -1;
                }
            });

        } else {
            headerSubTitle.set(null);
            headerInfo.set(null);
            headerSubInfo.set(null);
        }

        setModel(new PortfolioAdapterModel(getContext(), items));
    }

    private List<PortfolioItemModel> addExchange(ExchangeType exchange, List<PortfolioItemModel> currentPortfolio, List<PortfolioCoin> portfolio) {

        for (PortfolioCoin item : portfolio) {

            boolean updated = false;

            for (PortfolioItemModel model : currentPortfolio) {
                if (model.coin.id.equals(item.coinId)) {
                    model.items.addAll(item.items);
                    model.updateData(model.items);
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                CoinItem coin = getDataHolder().getCoin(item.coinId);
                if (coin != null) {
                    PortfolioItemModel model = new PortfolioItemModel(coin, item.items);
                    model.setOnItemSelectedListener(this);
                    currentPortfolio.add(model);
                }
            }
        }

        return currentPortfolio;
    }

    private List<PortfolioItemModel> removeExchange(ExchangeType exchange, List<PortfolioItemModel> currentPortfolio) {

        Iterator<PortfolioItemModel> iterator = currentPortfolio.iterator();
        while (iterator.hasNext()) {

            PortfolioItemModel model = iterator.next();
            Iterator<PortfolioItem> itemIterator = model.items.iterator();

            while (itemIterator.hasNext()) {
                PortfolioItem item = itemIterator.next();
                if (ObjectHelper.equals(exchange, item.exchange)) {
                    itemIterator.remove();
                }
            }

            if (model.items.size() == 0) {
                iterator.remove();
            } else {
                model.updateData(model.items);
            }
        }

        return currentPortfolio;
    }

    private void updateExchanges() {

        for (ExchangeType exchange : ExchangeType.values()) {

            List<PortfolioCoin> portfolio = getUserPrefs().getPortfolio(exchange);

            if (portfolio != null && !portfolio.isEmpty()) {
                updatePortfolio(exchange, portfolio);
            }
        }
    }

    @Override
    public void onPortfolioChanged(List<PortfolioCoin> portfolio) {

        updatePortfolio(ExchangeType.NONE, portfolio);
    }

    @Override
    public void onPortfolioChanged(ExchangeType exchange, List<PortfolioCoin> portfolio) {

        updatePortfolio(exchange, portfolio);
    }

    @Override
    public void onDataLoading(boolean isLoading, DataHolder holder) {

        if (!isLoading) {
            updatePortfolio(ExchangeType.NONE, getUserPrefs().getPortfolio());
            updateExchanges();
        }

        this.isLoading.set(isLoading);
    }

    @Override
    public void onDataLoadingFailed(boolean isLoading, DataHolder holder) {

        this.isLoading.set(false);
    }

    @Override
    public void onExchangeAuthChanged(ExchangeType type, @Nullable AuthCredentials credentials) {

        if (credentials == null) {
            getUserPrefs().setPortfolio(type, null);
            onPortfolioChanged(type, null);
        } else {
            getDataHolder().getFetcher().requestExchangePortfolio(type, credentials);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.portfolio_page;
    }
}
