package com.ankhrom.coinmarketcap.viewmodel.dashboard;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.ankhrom.base.common.statics.ArgsHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.coinmarketcap.AppCode;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.common.AppVibrator;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.data.DataLoadingListener;
import com.ankhrom.coinmarketcap.databinding.MarketPageBinding;
import com.ankhrom.coinmarketcap.entity.MarketData;
import com.ankhrom.coinmarketcap.listener.OnItemSwipeListener;
import com.ankhrom.coinmarketcap.model.coin.CoinItemModel;
import com.ankhrom.coinmarketcap.model.coin.CoinsAdapterModel;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;
import com.ankhrom.coinmarketcap.view.ItemSwipeListener;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.coinmarketcap.viewmodel.coin.CoinDetailViewModel;

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
    private ItemTouchHelper itemTouchHelper;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        state = args.getArg(ListState.class, ListState.NORMAL);
    }

    @Override
    public void onInit() {
        super.onInit();

        headerTitle.set(getContext().getString(R.string.app_name));
        isLoading.set(true);

        itemSwipeListener = new ItemSwipeListener(getContext(), R.id.item_foreground, this);

        DataHolder holder = getFactory().get(DataHolder.class);
        holder.getFetcher().addListener(this);
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

        DataHolder holder = getFactory().get(DataHolder.class);
        holder.reload();
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

        DataHolder holder = getFactory().get(DataHolder.class);

        if (holder.isMarketAvailable() && holder.isCoinListAvailable()) {
            updateModel(holder);
        }
    }

    public ListState getListState() {
        return state;
    }

    private final OnItemSelectedListener<CoinItemModel> itemSelectedListener = new OnItemSelectedListener<CoinItemModel>() {
        @Override
        public void onItemSelected(View view, CoinItemModel model) {
            openViewModel(CoinDetailViewModel.class, model.coin);
        }
    };

    private final OnItemSelectedListener<CoinItemModel> itemSelectedLongListener = new OnItemSelectedListener<CoinItemModel>() {
        @Override
        public void onItemSelected(View view, CoinItemModel model) {

        }
    };

    protected void toggleItemFavouriteState(CoinItemModel item) {

        item.isFavourite.set(!item.isFavourite.get());

        UserPrefs prefs = getFactory().get(UserPrefs.class);

        if (item.isFavourite.get()) {
            prefs.addFavourite(item.coin.id);
        } else {
            prefs.removeFavourite(item.coin.id);

            if (state == ListState.FAVOURITES) {
                model.adapter.remove(item);
            }
        }

        prefs.notifyFavouriteItemChanged(item);
    }

    @Override
    public void onSelectedItemChanged(int index) {

        itemActivated = false;
        itemSwipeListener.swipeBack = true;

        if (index > -1) {

            activeItem = model.adapter.get(index);
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

    protected void setMarketData(MarketData market) {

        headerSubTitle.set(new Date(market.timestamp * 1000).toLocaleString());
        headerInfo.set(ApiFormat.toShortFormat(String.valueOf(market.marketCap)));
        headerSubInfo.set("BTC " + ApiFormat.toDigitFormat(market.bitcoinDominance) + "%" + " | " + ApiFormat.toShortFormat(String.valueOf(market.marketVolume)));
    }

    protected void updateModel(DataHolder holder) {

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
        }
/*
        HitBTC hitBTC = getFactory().get(HitBTC.class);
        hitBTC.currency.ticker(new ResponseListener<List<HitCurrencyTicker>>() {
            @Override
            public void onResponse(List<HitCurrencyTicker> response) {
                response = HitFilter.filterByCurrency(response, "BTC");

                Collections.sort(response, new Comparator<HitCurrencyTicker>() {
                    @Override
                    public int compare(HitCurrencyTicker a, HitCurrencyTicker b) {

                        if (StringHelper.isEmpty(a.bid)) {
                            return StringHelper.isEmpty(b.bid) ? 0 : 1;
                        }

                        if (StringHelper.isEmpty(b.bid)) {
                            return -1;
                        }

                        double priceA = Double.parseDouble(a.bid);
                        double priceB = Double.parseDouble(b.bid);

                        CoinItem coinA = getDataHolder().getCoinBySymbol(a.symbol.replace("BTC", ""));
                        CoinItem coinB = getDataHolder().getCoinBySymbol(b.symbol.replace("BTC", ""));

                        if (coinA == null) {
                            return coinB == null ? 0 : 1;
                        }

                        if (coinB == null) {
                            return -1;
                        }

                        return priceA > priceB ? 1 : -1;
                    }
                });

                List<CoinItemModel> items = ObjectHelper.convert(response, new ObjectConverter<CoinItemModel, HitCurrencyTicker>() {
                    @Override
                    public CoinItemModel convert(HitCurrencyTicker object) {

                        CoinItem coin = getDataHolder().getCoinBySymbol(object.symbol.replace("BTC", ""));

                        if (coin == null) {
                            return null;
                        }

                        CoinItemModel itemModel = new CoinItemModel(coin);

                        return itemModel;
                    }
                });

                items.removeAll(Collections.singleton(null));

                //setModel(new CoinsAdapterModel(getContext(), items));
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        */
    }

    @Override
    public void onDataLoading(boolean isLoading, DataHolder holder) {

        this.isLoading.set(isLoading);

        if (!isLoading) {

            setMarketData(holder.getMarket());
            updateModel(holder);
        }
    }

    @Override
    public void onDataLoadingFailed(boolean isLoading, DataHolder holder) {

        this.isLoading.set(isLoading);
    }

    @Override
    public void onReceiveArgs(int requestCode, Object[] args) {
        super.onReceiveArgs(requestCode, args);

        if (requestCode == AppCode.STATE) {
            changeState(ArgsHelper.getArg(ListState.class, args, ListState.NORMAL));
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.market_page;
    }
}
