package com.ankhrom.coinmarketcap.viewmodel.portfolio;

import android.view.View;

import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.interfaces.ObjectConverter;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.databinding.PortfolioEditPageBinding;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;
import com.ankhrom.coinmarketcap.model.PortfolioAdapterModel;
import com.ankhrom.coinmarketcap.model.PortfolioItemModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

import java.util.List;

/**
 * Created by R' on 1/2/2018.
 */

public class PortfolioEditViewModel extends AppViewModel<PortfolioEditPageBinding, PortfolioAdapterModel> {

    private CoinItem coin;
    private PortfolioItemModel parentModel;

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

        DataHolder holder = getFactory().get(DataHolder.class);
        PortfolioCoin portfolio = holder.getPortfolioCoin(coin.id);

        if (portfolio == null) {
            return;
        }

        if (parentModel == null) {
            parentModel = new PortfolioItemModel(coin, portfolio.items);
        }

        headerTitle.set(coin.symbol + " - " + coin.name + " - " + parentModel.amount.get());
        headerSubTitle.set(parentModel.profitLossAmount.get() + " / " + parentModel.profitLoss.get());
        headerInfo.set(parentModel.currentValue.get());
        headerSubInfo.set(parentModel.investedValue.get());

        List<PortfolioItemModel> items = ObjectHelper.convert(portfolio.items, new ObjectConverter<PortfolioItemModel, PortfolioItem>() {
            @Override
            public PortfolioItemModel convert(PortfolioItem object) {
                return new PortfolioItemModel(coin, object);
            }
        });

        setModel(new PortfolioAdapterModel(getContext(), items));
    }

    public void onAddPressed(View view) {

    }

    @Override
    public int getLayoutResource() {
        return R.layout.portfolio_edit_page;
    }
}
