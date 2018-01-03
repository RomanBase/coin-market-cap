package com.ankhrom.coinmarketcap.viewmodel.dashboard;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.custom.builder.ToastBuilder;
import com.ankhrom.base.custom.listener.OnTouchActionListener;
import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.common.AppVibrator;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.data.DataLoadingListener;
import com.ankhrom.coinmarketcap.databinding.MarketPageBinding;
import com.ankhrom.coinmarketcap.entity.MarketData;
import com.ankhrom.coinmarketcap.listener.OnItemSwipeListener;
import com.ankhrom.coinmarketcap.model.CoinItemModel;
import com.ankhrom.coinmarketcap.model.CoinsAdapterModel;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;
import com.ankhrom.coinmarketcap.view.ItemSwipeListener;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

import java.util.Date;
import java.util.List;

/**
 * Created by R' on 12/30/2017.
 */

public class MarketViewModel extends AppViewModel<MarketPageBinding, CoinsAdapterModel> implements DataLoadingListener, SwipeRefreshLayout.OnRefreshListener, OnItemSwipeListener {

    public enum ListState {
        NORMAL,
        FAVOURITES
    }

    private ListState state;
    private CoinItemModel activeItem;
    private boolean itemActivated;

    private ItemSwipeListener itemSwipeListener;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        state = args.getArg(ListState.class, ListState.NORMAL);
    }

    @Override
    public void onInit() {
        super.onInit();

        headerTitle.set(getContext().getString(R.string.app_name));

        itemSwipeListener = new ItemSwipeListener(getContext(), R.id.item_foreground, this);

        DataHolder holder = getFactory().get(DataHolder.class);
        holder.getFetcher().addListener(this);
    }

    @Override
    protected void onCreateViewBinding(MarketPageBinding binding) {
        super.onCreateViewBinding(binding);

        binding.itemsContainer.setOnTouchListener(touchActionListener);
        new ItemTouchHelper(itemSwipeListener).attachToRecyclerView(binding.itemsContainer);
    }

    @Override
    public void onRefresh() {

        binding.pullToRefresh.setRefreshing(false);
        model.adapter.clear();

        DataHolder holder = getFactory().get(DataHolder.class);
        holder.reload();
    }

    public void changeState(ListState state) {

        if (this.state == state) {
            return;
        }

        this.state = state;

        UserPrefs prefs = getFactory().get(UserPrefs.class);

        DataHolder holder = getFactory().get(DataHolder.class);
        holder.getFetcher().notifyListeners();
    }

    public ListState getListState() {
        return state;
    }

    private final OnItemSelectedListener<CoinItemModel> itemSelectedListener = new OnItemSelectedListener<CoinItemModel>() {
        @Override
        public void onItemSelected(View view, CoinItemModel model) {
            ToastBuilder.with(getContext()).text("clicked: " + model.coin.name).buildAndShow();
        }
    };

    private final OnItemSelectedListener<CoinItemModel> itemSelectedLongListener = new OnItemSelectedListener<CoinItemModel>() {
        @Override
        public void onItemSelected(View view, CoinItemModel model) {

        }
    };

    private final View.OnTouchListener touchActionListener = new OnTouchActionListener() {
        @Override
        public void onTouchActionDown(View view) {
            itemSwipeListener.swipeBack = false;
        }

        @Override
        public void onTouchActionUp(View view) {
            itemSwipeListener.swipeBack = true;
        }
    };

    protected void toggleItemFavouriteState(CoinItemModel item) {

        item.isFavourite.set(!item.isFavourite.get());

        UserPrefs prefs = getFactory().get(UserPrefs.class);

        if (item.isFavourite.get()) {
            prefs.addFavourite(item.coin.id);
        } else {
            prefs.removeFavourite(item.coin.id);
        }

        prefs.notifyFavouriteItemChanged(item);
    }

    @Override
    public void onSelectedItemChanged(int index) {

        itemActivated = false;

        if (index < 0) {

            if (activeItem == null) {
                return;
            }

            if (activeItem.swipeProgress.get() >= 1.0f) {
                toggleItemFavouriteState(activeItem);
            }

            activeItem.swipeProgress.set(0.0f);
            activeItem = null;
            return;
        }

        activeItem = model.adapter.get(index);
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

    protected void setMarketData(MarketData market) {

        headerSubTitle.set(new Date(market.timestamp * 1000).toLocaleString());
        headerInfo.set(ApiFormat.toShortFormat(String.valueOf(market.marketCap)));
        headerSubInfo.set("BTC " + ApiFormat.toDigitFormat(market.bitcoinDominance) + "%" + " / " + ApiFormat.toShortFormat(String.valueOf(market.marketVolume)));
    }

    @Override
    public void onDataLoading(boolean isLoading, DataHolder holder) {

        this.isLoading.set(isLoading);

        if (!isLoading) {

            setMarketData(holder.getMarket());

            List<CoinItemModel> items = holder.getCoinItems();
            List<CoinItemModel> favs = holder.getFavouriteCoinItems();

            if (model == null || model.adapter.getItemCount() == 0) {

                for (CoinItemModel item : items) {
                    item.setOnItemSelectedListener(itemSelectedListener);
                    item.setOnItemSelectedLongListener(itemSelectedLongListener);
                }

                if (state == ListState.FAVOURITES) {
                    items = holder.getFavouriteCoinItems();
                }

                setModel(new CoinsAdapterModel(getContext(), items));
            } else {

                int count = items.size();
                for (int i = 0; i < count; i++) {
                    CoinItemModel item = items.get(i);

                    if (!favs.contains(item)) {
                        if (state == ListState.NORMAL) {
                            model.adapter.add(i, item);
                        } else {
                            model.adapter.remove(item);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDataLoadingFailed(boolean isLoading, DataHolder holder) {

        this.isLoading.set(isLoading);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.market_page;
    }
}
