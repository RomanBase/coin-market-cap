package com.ankhrom.coinmarketcap.viewmodel.dashboard;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.model.ItemModel;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.common.AppVibrator;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.databinding.PortfolioPageBinding;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;
import com.ankhrom.coinmarketcap.listener.DataLoadingListener;
import com.ankhrom.coinmarketcap.listener.OnExchangeAuthChangedListener;
import com.ankhrom.coinmarketcap.listener.OnExchangePortfolioChangedListener;
import com.ankhrom.coinmarketcap.listener.OnItemSwipeListener;
import com.ankhrom.coinmarketcap.listener.OnPortfolioChangedListener;
import com.ankhrom.coinmarketcap.model.PortfolioAdapterModel;
import com.ankhrom.coinmarketcap.model.PortfolioItemModel;
import com.ankhrom.coinmarketcap.view.ItemSwipeListener;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.coinmarketcap.viewmodel.portfolio.PortfolioEditViewModel;
import com.ankhrom.coinmarketcap.viewmodel.portfolio.PortfolioPlusViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by R' on 1/1/2018.
 */

public class PortfolioViewModel extends AppViewModel<PortfolioPageBinding, PortfolioAdapterModel> implements DataLoadingListener, OnPortfolioChangedListener, OnExchangePortfolioChangedListener, OnItemSelectedListener<PortfolioItemModel>, OnExchangeAuthChangedListener, SwipeRefreshLayout.OnRefreshListener, OnItemSwipeListener {

    private PortfolioItemModel activeItem;
    private boolean itemActivated;

    private ItemSwipeListener itemSwipeListener;
    private ItemTouchHelper itemTouchHelper;

    @Override
    public void onInit() {
        super.onInit();

        if (model != null) {
            return;
        }

        isLoading.set(true);
        headerTitle.set("Portfolio");

        setModel(new PortfolioAdapterModel(getContext()));
        model.setOnAddItemPressedListener(onAddItemPressed);

        itemSwipeListener = new ItemSwipeListener(getContext(), R.id.item_foreground, this);

        DataHolder holder = getDataHolder();
        holder.getFetcher().addListener(this);

        getPortfolio().setExchangePortfolioListener(this);
        getPortfolio().setPortfolioChangedListener(this);
        getExchangePrefs().addExchangeAuthListener(this);
    }

    @Override
    protected void onCreateViewBinding(PortfolioPageBinding binding) {
        super.onCreateViewBinding(binding);

        attachSwipeListener();
    }

    private final OnItemSelectedListener onAddItemPressed = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(View view, ItemModel model) {
            onAddPressed(view);
        }
    };

    private void attachSwipeListener() {

        if (binding == null) {
            return;
        }

        if (itemTouchHelper == null) {
            itemTouchHelper = new ItemTouchHelper(itemSwipeListener);
        }

        itemTouchHelper.attachToRecyclerView(binding.itemsContainer);
    }

    private void dettachSwipeListener() {

        if (itemTouchHelper == null) {
            return;
        }

        itemTouchHelper.attachToRecyclerView(null);
    }

    @Override
    public void onRefresh() {

        isLoading.set(true);

        binding.pullToRefresh.setRefreshing(false);

        if (isModelAvailable()) {
            model.adapter.clear();
        }

        DataHolder holder = getDataHolder();
        holder.reload();
    }

    protected void toggleItemFavouriteState(PortfolioItemModel item) {

        item.isFavourite.set(!item.isFavourite.get());

        if (item.isFavourite.get()) {
            getDataHolder().addFavourite(item.coin.id);
        } else {
            getDataHolder().removeFavourite(item.coin.id);
        }

        getPortfolio().notifyFavouriteCoinChanged(item.coin, item.isFavourite.get());
    }

    public void onAddPressed(View view) {

        addViewModel(PortfolioPlusViewModel.class);
    }

    @Override
    public void onItemSelected(View view, PortfolioItemModel model) {

        if (model == null) {
            return;
        }

        addViewModel(PortfolioEditViewModel.class, model.coin);
    }

    @Override
    public void onSelectedItemChanged(int index) {

        itemActivated = false;
        itemSwipeListener.swipeBack = true;

        if (index > -1) {

            ItemModel model = this.model.adapter.get(index);
            PortfolioItemModel item = model instanceof PortfolioItemModel ? (PortfolioItemModel) model : null;

            if (item != null) {

                activeItem = item;
                activeItem.isFavourite.set(getDataHolder().getFavourites().contains(activeItem.coin.id));
            }

            return;
        }

        if (activeItem == null) {
            return;
        }

        if (activeItem.swipeProgress.get() >= 1.0f) {
            toggleItemFavouriteState(activeItem);
        }

        activeItem.swipeProgress.set(0.0f);
        activeItem = null;
    }

    @Override
    public void onItemSwipeProgress(int index, float progress, boolean directionToLeft) {

        if (activeItem == null) {
            return;
        }

        activeItem.swipeProgress.set(progress);
        activeItem.swipeDirectionLeft.set(directionToLeft);

        boolean activate = progress >= 1.0f;

        if (activate != itemActivated) {
            itemActivated = activate;
            if (itemActivated) {
                AppVibrator.itemActivated(getContext());
            }
        }
    }

    private void updatePortfolio(ExchangeType exchange, List<PortfolioCoin> portfolio) {

        List<PortfolioItemModel> items = removeExchange(exchange, model.adapter.getItems(PortfolioItemModel.class));

        if (portfolio != null && !portfolio.isEmpty()) {
            items = addExchange(items, portfolio);
        }

        updateHeader(items);
        changeDataSet(items);
    }

    private void updateHeader(List<PortfolioItemModel> items) {

        if (items.size() > 0) {

            double invested = 0.0;
            double current = 0.0;

            List<String> favs = getDataHolder().getFavourites();

            for (PortfolioItemModel item : items) {
                invested += item.invested;
                current += item.current;
                item.isFavourite.set(favs.contains(item.coin.id));
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

        } else {
            headerSubTitle.set(null);
            headerInfo.set(null);
            headerSubInfo.set(null);
        }
    }

    private void changeDataSet(List<PortfolioItemModel> items) {

        Collections.sort(items, new Comparator<PortfolioItemModel>() {
            @Override
            public int compare(PortfolioItemModel a, PortfolioItemModel b) {
                return a.current == b.current ? 0 : a.current < b.current ? 1 : -1;
            }
        });

        model.replace(items);
    }

    private List<PortfolioItemModel> addExchange(List<PortfolioItemModel> currentPortfolio, List<PortfolioCoin> portfolio) {

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

    private void addItem(PortfolioItem item) {

        List<PortfolioItemModel> currentPortfolio = model.adapter.getItems(PortfolioItemModel.class);

        boolean updated = false;

        for (PortfolioItemModel model : currentPortfolio) {
            if (model.coin.id.equals(item.coinId)) {
                model.items.add(item);
                model.updateData(model.items);
                this.model.adapter.notifyItemChanged(model);
                updated = true;
            }
        }

        if (!updated) {
            CoinItem coin = getDataHolder().getCoin(item.coinId);
            if (coin != null) {
                List<PortfolioItem> items = new ArrayList<>();
                items.add(item);

                PortfolioItemModel model = new PortfolioItemModel(coin, items);
                model.setOnItemSelectedListener(this);

                int count = currentPortfolio.size();
                int index = count;
                for (int i = 0; i < count; i++) {
                    if (model.current > currentPortfolio.get(i).current) {
                        index = i;
                        break;
                    }
                }

                this.model.adapter.add(index, model);
                this.model.checkFooter();
                this.model.checkEmptiness();
            }
        }

        updateHeader(model.adapter.getItems(PortfolioItemModel.class));
    }

    private void removeItem(PortfolioItem item) {

        List<PortfolioItemModel> currentPortfolio = model.adapter.getItems(PortfolioItemModel.class);
        PortfolioItemModel emptyModel = null;

        for (PortfolioItemModel model : currentPortfolio) {
            if (model.items.contains(item)) {
                model.items.remove(item);
                if (model.items.size() == 0) {
                    emptyModel = model;
                } else {
                    model.updateData(model.items);
                    this.model.adapter.notifyItemChanged(model);
                }
                break;
            }
        }

        if (emptyModel != null) {
            currentPortfolio.remove(emptyModel);
            model.adapter.remove(emptyModel);
        }

        updateHeader(currentPortfolio);

        model.checkEmptiness();
    }

    private void updateExchanges() {

        for (ExchangeType exchange : ExchangeType.values()) {

            List<PortfolioCoin> portfolio = getPortfolio().getExchange(exchange);

            if (!portfolio.isEmpty()) {
                updatePortfolio(exchange, portfolio);
            }
        }
    }

    @Override
    public void onPortfolioItemAdded(PortfolioItem item) {

        addItem(item);
    }

    @Override
    public void onPortfolioItemRemoved(PortfolioItem item) {

        removeItem(item);
    }

    @Override
    public void onPortfolioChanged(ExchangeType exchange, List<PortfolioCoin> portfolio) {

        updatePortfolio(exchange, portfolio);
    }

    @Override
    public void onDataLoading(boolean isLoading, DataHolder holder) {

        this.error.set(false);

        if (!isLoading) {
            updateExchanges();
        }

        this.isLoading.set(isLoading);
    }

    @Override
    public void onDataLoadingFailed(boolean isLoading, DataHolder holder) {

        this.isLoading.set(false);
        this.error.set(true);
    }

    @Override
    public void onExchangeAuthChanged(ExchangeType type, @Nullable AuthCredentials credentials) {

        if (credentials == null) {
            getPortfolio().clear(type, true);
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
