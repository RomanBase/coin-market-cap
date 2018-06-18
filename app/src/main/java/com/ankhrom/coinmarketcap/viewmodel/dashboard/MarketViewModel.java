package com.ankhrom.coinmarketcap.viewmodel.dashboard;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.ankhrom.base.common.statics.ArgsHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.custom.builder.ToastBuilder;
import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.model.ItemModel;
import com.ankhrom.coinmarketcap.AppCode;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.common.AppVibrator;
import com.ankhrom.coinmarketcap.common.CoinItemComparator;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.databinding.MarketPageBinding;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.MarketData;
import com.ankhrom.coinmarketcap.listener.DataLoadingListener;
import com.ankhrom.coinmarketcap.listener.OnCoinSelectedListener;
import com.ankhrom.coinmarketcap.listener.OnFavouriteCoinStateChangedListener;
import com.ankhrom.coinmarketcap.listener.OnItemSwipeListener;
import com.ankhrom.coinmarketcap.model.base.SortableCoinItemModel;
import com.ankhrom.coinmarketcap.model.coin.CoinAdapterFooterItemModel;
import com.ankhrom.coinmarketcap.model.coin.CoinItemModel;
import com.ankhrom.coinmarketcap.model.coin.CoinsAdapterModel;
import com.ankhrom.coinmarketcap.view.ItemSwipeListener;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.coinmarketcap.viewmodel.coin.CoinDetailViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dialog.SearchViewModel;

import java.util.List;

/**
 * Created by R' on 12/30/2017.
 */

public class MarketViewModel extends AppViewModel<MarketPageBinding, CoinsAdapterModel> implements DataLoadingListener, SwipeRefreshLayout.OnRefreshListener, OnItemSwipeListener, OnCoinSelectedListener, OnFavouriteCoinStateChangedListener {

    public enum ListState {
        NORMAL,
        FAVOURITES
    }

    public enum SortState {
        RANK,
        PRICE_UP,
        PRICE_DOWN,
        CHANGE_UP,
        CHANGE_DOWN
    }

    private ListState state;
    private SortState sort;
    private CoinItemModel activeItem;
    private boolean itemActivated;

    private ItemSwipeListener itemSwipeListener;
    private ItemTouchHelper itemTouchHelper;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        state = args.getArg(ListState.class, ListState.NORMAL);
        sort = SortState.RANK;
    }

    @Override
    public void onInit() {
        super.onInit();

        if (model != null) {
            return;
        }

        isLoading.set(true);
        headerTitle.set("Coin Market Cap");

        setModel(new CoinsAdapterModel(getContext()));

        itemSwipeListener = new ItemSwipeListener(getContext(), R.id.item_foreground, this);

        DataHolder holder = getDataHolder();
        holder.getFetcher().addListener(this);

        getPortfolio().setFavouriteCoinStateChangedListener(this);
    }

    @Override
    protected void onCreateViewBinding(MarketPageBinding binding) {
        super.onCreateViewBinding(binding);

        attachSwipeListener();
    }

    @Override
    public void onRefresh() {

        isLoading.set(true);

        binding.pullToRefresh.setRefreshing(false);
        model.adapter.clear();

        DataHolder holder = getDataHolder();
        holder.reload();
    }

    private final OnItemSelectedListener onSearchSelected = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(View view, ItemModel model) {
            onSearchPressed(view);
        }
    };

    private final OnItemSelectedListener<CoinItemModel> itemSelectedListener = new OnItemSelectedListener<CoinItemModel>() {
        @Override
        public void onItemSelected(View view, CoinItemModel model) {

            addViewModel(CoinDetailViewModel.class, model.coin);
        }
    };

    private final OnItemSelectedListener<CoinItemModel> itemSelectedLongListener = new OnItemSelectedListener<CoinItemModel>() {
        @Override
        public void onItemSelected(View view, CoinItemModel model) {

        }
    };

    public void onSearchPressed(View view) {

        addViewModel(SearchViewModel.class, this);
    }

    public void onTogglePercentageFilter(View view) {

        sort = sort == SortState.CHANGE_UP ? SortState.CHANGE_DOWN : SortState.CHANGE_UP;
        sortItems();
    }

    public void onToggleUnitPriceFilter(View view) {

        sort = sort == SortState.PRICE_DOWN ? SortState.PRICE_UP : SortState.PRICE_DOWN;
        sortItems();
    }

    public void onToggleRankFilter(View view) {

        sort = SortState.RANK;
        sortItems();
    }

    private void sortItems() {

        model.adapter.sort(CoinItemComparator.getSorter(sort));
    }

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

    public void changeState(ListState state) {

        if (this.state == state) {
            return;
        }

        this.state = state;

        DataHolder holder = getDataHolder();

        if (holder.isMarketAvailable() && holder.isCoinListAvailable()) {
            updateModel(holder);
        }
    }

    public ListState getListState() {
        return state;
    }

    protected void toggleItemFavouriteState(CoinItemModel item) {

        if (item.coin.mock) {
            ToastBuilder.with(getContext())
                    .text("Not listed yet")
                    .buildAndShow();

            return;
        }

        item.isFavourite.set(!item.isFavourite.get());

        if (item.isFavourite.get()) {
            getDataHolder().addFavourite(item.coin.id);
        } else {
            getDataHolder().removeFavourite(item.coin.id);

            if (state == ListState.FAVOURITES) {
                model.adapter.remove(item);
            }
        }
    }

    @Override
    public void onCoinSelected(CoinItem coin) {

        CoinItemModel model = getDataHolder().getCoinItem(coin.id);

        if (model == null || model.isFavourite.get()) {
            return;
        }

        model.setOnItemSelectedListener(itemSelectedListener);

        insertItemIntoAdapter(model);

        toggleItemFavouriteState(model);
    }

    @Override
    public void onSelectedItemChanged(int index) {

        itemActivated = false;
        itemSwipeListener.swipeBack = true;

        if (index > -1) {

            ItemModel item = model.adapter.get(index);

            if (item instanceof CoinItemModel) {

                activeItem = (CoinItemModel) item;
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

    protected void setMarketData(final MarketData market) {

        headerSubTitle.set(ApiFormat.toTimeFormat(market.timestamp * 1000));
        headerInfo.set(ApiFormat.toShortFormat(String.valueOf(market.marketCap)));
        headerSubInfo.set("BTC " + ApiFormat.toDigitFormat(market.bitcoinDominance) + "%" + " | " + ApiFormat.toShortFormat(String.valueOf(market.marketVolume)));
    }

    protected void updateModel(final DataHolder holder) {

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

            model.adapter.add(new CoinAdapterFooterItemModel(onSearchSelected));

        } else {
            int count = items.size();

            if (sort != SortState.RANK) {
                model.adapter.sort(CoinItemComparator.getSorter(sort = SortState.RANK));
            }

            for (int i = 0; i < count; i++) {
                CoinItemModel item = items.get(i);

                boolean isListed = model.adapter.getItems().contains(item);

                if (state == ListState.NORMAL) {
                    if (!isListed) {
                        model.adapter.add(i, item);
                    }
                } else {
                    boolean isFavourite = favs.contains(item);
                    if (isFavourite) {
                        if (!isListed) {
                            model.adapter.add(i, item);
                        }
                    } else {
                        if (isListed) {
                            model.adapter.remove(item);
                        }
                    }
                }
            }

            model.adapter.scrollUp(false);
        }
    }

    private void insertItemIntoAdapter(CoinItemModel model) {

        List<SortableCoinItemModel> items = this.model.adapter.getItems();
        int count = items.size();

        for (int i = 0; i < count; i++) {

            SortableCoinItemModel item = items.get(i);

            if (item.isSortable) {
                if (model.itemRank < item.itemRank) {
                    this.model.adapter.add(i, model);
                    break;
                }
            } else {
                this.model.adapter.add(i, model);
                break;
            }
        }
    }

    @Override
    public void onFavouriteCoinStateChanged(CoinItem coin, boolean isFavourite) {

        if (!isModelAvailable()) {
            return;
        }

        CoinItemModel item = getDataHolder().getCoinItem(coin.id);

        if (item == null) {
            if (isFavourite) {
                onCoinSelected(coin);
            }

            return;
        }

        item.isFavourite.set(isFavourite);

        boolean isListed = model.adapter.getItems().contains(item);

        if (state == ListState.NORMAL) {
            if (!isListed) {
                model.adapter.add(item);
            }
        } else {
            if (isFavourite) {
                if (!isListed) {
                    insertItemIntoAdapter(item);
                }
            } else {
                if (isListed) {
                    model.adapter.remove(item);
                }
            }
        }
    }

    @Override
    public void onDataLoading(boolean isLoading, DataHolder holder) {

        this.error.set(false);

        if (!isLoading) {

            setMarketData(holder.getMarket());
            updateModel(holder);
        }

        this.isLoading.set(isLoading);
    }

    @Override
    public void onDataLoadingFailed(boolean isLoading, DataHolder holder) {

        this.isLoading.set(isLoading);
        this.error.set(true);
    }

    @Override
    public void onReceiveArgs(int requestCode, Object[] args) {
        super.onReceiveArgs(requestCode, args);

        if (requestCode == AppCode.STATE) {
            changeState(ArgsHelper.getArg(ListState.class, args, ListState.NORMAL));
        } else if (requestCode == AppCode.NOTIFY) {
            if (isModelAvailable()) {
                model.adapter.scrollUp(false);
            }
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.market_page;
    }
}
