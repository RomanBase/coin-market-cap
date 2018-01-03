package com.ankhrom.coinmarketcap.viewmodel.portfolio;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.interfaces.ObjectConverter;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.common.AppVibrator;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.databinding.PortfolioEditPageBinding;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;
import com.ankhrom.coinmarketcap.listener.OnItemSwipeListener;
import com.ankhrom.coinmarketcap.model.PortfolioAdapterModel;
import com.ankhrom.coinmarketcap.model.PortfolioItemModel;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;
import com.ankhrom.coinmarketcap.view.ItemSwipeListener;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

import java.util.List;

/**
 * Created by R' on 1/2/2018.
 */

public class PortfolioEditViewModel extends AppViewModel<PortfolioEditPageBinding, PortfolioAdapterModel> implements OnItemSwipeListener {

    private CoinItem coin;
    private PortfolioItemModel parentModel;

    private PortfolioItemModel activeItem;
    private boolean itemActivated;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        parentModel = args.getArg(PortfolioItemModel.class);
        coin = args.getArg(CoinItem.class, parentModel.coin);
    }

    @Override
    public void onInit() {
        super.onInit();

        if (coin == null) {
            return;
        }

        reloadParentModel();
        reloadHeader();
        reloadModel();
    }

    @Override
    protected void onCreateViewBinding(PortfolioEditPageBinding binding) {
        super.onCreateViewBinding(binding);

        new ItemTouchHelper(new ItemSwipeListener(getContext(), R.id.item_foreground, this)).attachToRecyclerView(binding.itemsContainer);
    }

    protected void reloadParentModel() {

        DataHolder holder = getFactory().get(DataHolder.class);
        PortfolioCoin portfolio = holder.getPortfolioCoin(coin.id);

        if (parentModel == null) {
            parentModel = new PortfolioItemModel(coin);
        }

        parentModel.updateData(portfolio.items);
    }

    protected void reloadHeader() {

        headerTitle.set(coin.symbol + " - " + coin.name + " - " + parentModel.amount.get());
        headerSubTitle.set(parentModel.profitLossAmount.get() + " / " + parentModel.profitLoss.get());
        headerInfo.set(parentModel.currentValue.get());
        headerSubInfo.set(parentModel.investedValue.get());
    }

    protected void reloadModel() {

        DataHolder holder = getFactory().get(DataHolder.class);
        PortfolioCoin portfolio = holder.getPortfolioCoin(coin.id);

        if (portfolio == null) {
            return;
        }

        List<PortfolioItemModel> items = ObjectHelper.convert(portfolio.items, new ObjectConverter<PortfolioItemModel, PortfolioItem>() {
            @Override
            public PortfolioItemModel convert(PortfolioItem object) {
                return new PortfolioItemModel(coin, object);
            }
        });

        setModel(new PortfolioAdapterModel(getContext(), items));
    }

    @Override
    public void onSelectedItemChanged(int index) {

        itemActivated = false;

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

    protected void removePortfolioItem(PortfolioItemModel itemModel) {

        int index = model.adapter.getItems().indexOf(itemModel);

        if (index < 0) {
            return;
        }

        DataHolder holder = getFactory().get(DataHolder.class);
        PortfolioCoin portfolio = holder.getPortfolioCoin(coin.id);
        portfolio.items.remove(index);

        UserPrefs prefs = getFactory().get(UserPrefs.class);
        prefs.updatePorfolioItem(portfolio);

        model.adapter.remove(index);

        reloadParentModel();
        reloadHeader();
    }

    public void onAddPressed(View view) {

    }

    @Override
    public int getLayoutResource() {
        return R.layout.portfolio_edit_page;
    }
}
