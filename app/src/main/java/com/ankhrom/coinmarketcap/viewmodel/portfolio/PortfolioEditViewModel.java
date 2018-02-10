package com.ankhrom.coinmarketcap.viewmodel.portfolio;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.interfaces.ObjectConverter;
import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.interfaces.viewmodel.CloseableViewModel;
import com.ankhrom.base.model.ItemModel;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.common.AppVibrator;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.databinding.PortfolioEditPageBinding;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;
import com.ankhrom.coinmarketcap.listener.OnExchangePortfolioChangedListener;
import com.ankhrom.coinmarketcap.listener.OnItemSwipeListener;
import com.ankhrom.coinmarketcap.model.PortfolioAdapterModel;
import com.ankhrom.coinmarketcap.model.PortfolioItemModel;
import com.ankhrom.coinmarketcap.view.ItemSwipeListener;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.coinmarketcap.viewmodel.coin.CoinDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 1/2/2018.
 */

public class PortfolioEditViewModel extends AppViewModel<PortfolioEditPageBinding, PortfolioAdapterModel> implements OnItemSwipeListener, OnExchangePortfolioChangedListener, CloseableViewModel {

    private CoinItem coin;
    private PortfolioItemModel parentModel;

    private PortfolioItemModel activeItem;
    private boolean itemActivated;

    private ItemSwipeListener itemSwipeListener;
    private ItemTouchHelper itemTouchHelper;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        coin = args.getArg(CoinItem.class);
    }

    @Override
    public void onInit() {
        super.onInit();

        if (coin == null) {
            return;
        }

        setModel(new PortfolioAdapterModel(getContext(), coin));
        model.setOnAddItemPressedListener(onAddItemPressed);

        itemSwipeListener = new ItemSwipeListener(getContext(), R.id.item_foreground, this);

        reloadParentModel();
        reloadHeader();
        reloadModel();
    }

    @Override
    protected void onCreateViewBinding(PortfolioEditPageBinding binding) {
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

    protected void reloadParentModel() {

        if (parentModel == null) {
            parentModel = new PortfolioItemModel(coin);
        }

        List<PortfolioItem> portfolio = getPortfolio().getPortfolioItems(coin);
        if (portfolio != null) {
            parentModel.updateData(portfolio);
        }
    }

    protected void reloadHeader() {

        if (parentModel == null) {
            return;
        }

        headerTitle.set(coin.symbol + " - " + coin.name + " (" + parentModel.amount.get() + ")");
        headerSubTitle.set(parentModel.profitLossAmount.get() + " / " + parentModel.profitLoss.get());
        headerInfo.set(parentModel.currentValue.get());
        headerSubInfo.set(parentModel.investedValue.get());
    }

    protected void reloadModel() {

        if (parentModel == null || parentModel.items == null) {
            return;
        }

        List<PortfolioItemModel> items = ObjectHelper.convert(parentModel.items, new ObjectConverter<PortfolioItemModel, PortfolioItem>() {
            @Override
            public PortfolioItemModel convert(PortfolioItem object) {
                return new PortfolioItemModel(coin, object);
            }
        });

        model.replace(items);
    }

    @Override
    public void onSelectedItemChanged(int index) {

        itemActivated = false;
        itemSwipeListener.swipeBack = true;

        if (index < 0) {

            if (activeItem == null) {
                return;
            }

            if (activeItem.swipeProgress.get() >= 1.0f) {
                removePortfolioItem(activeItem);
            }

            activeItem.swipeProgress.set(0.0f);
            activeItem = null;
            return;
        }

        activeItem = (PortfolioItemModel) model.adapter.get(index);
    }

    @Override
    public void onItemSwipeProgress(int index, float progress, boolean directionToLeft) {

        if (activeItem == null) {
            return;
        }

        if (ObjectHelper.equals(activeItem.items.get(0).exchange, ExchangeType.NONE)) {
            activeItem.swipeProgress.set(progress);
            activeItem.swipeDirectionLeft.set(directionToLeft);
        }

        boolean activate = progress >= 1.0f;

        if (activate != itemActivated) {
            itemActivated = activate;
            if (itemActivated) {
                AppVibrator.itemActivated(getContext());
            }
        }
    }

    protected void removePortfolioItem(PortfolioItemModel itemModel) {

        int index = model.adapter.getItems().indexOf(itemModel);

        if (index < 0) {
            return;
        }

        PortfolioItem item = itemModel.items.get(0); //one and only item

        if (ObjectHelper.equals(item.exchange, ExchangeType.NONE)) {

            if (parentModel.items.remove(item)) {

                PortfolioCoin portfolio = new PortfolioCoin();
                portfolio.coinId = coin.id;
                portfolio.items = getPortfolioItems(item.exchange);

                getPortfolio().updatePortfolioCoin(portfolio, item.exchange);
                getPortfolio().notifyPortfolioItemRemoved(item);
            }

            model.adapter.remove(index);

            getPortfolio().persist(item.exchange);

            model.checkEmptiness();

            if (model.isEmpty.get()) {
                getNavigation().navigateBack();
            } else {
                reloadParentModel();
                reloadHeader();
            }
        }
    }

    private List<PortfolioItem> getPortfolioItems(ExchangeType exchange) {

        List<PortfolioItem> items = new ArrayList<>();

        for (PortfolioItem item : parentModel.items) {
            if (ObjectHelper.equals(item.exchange, exchange)) {
                items.add(item);
            }
        }

        return items;
    }

    public void onAddPressed(View view) {

        addViewModel(PortfolioPlusViewModel.class, coin, this);
    }

    public void onCoinDetailPressed(View view) {

        addViewModel(CoinDetailViewModel.class, coin);
    }

    @Override
    public void onPortfolioChanged(ExchangeType exchange, List<PortfolioCoin> portfolio) {

        reloadParentModel();
        reloadHeader();
        reloadModel();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.portfolio_edit_page;
    }

    @Override
    public boolean isCloseable() {
        return true;
    }


}
