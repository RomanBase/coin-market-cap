package com.ankhrom.coinmarketcap.viewmodel.portfolio;

import android.view.View;

import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.common.statics.ScreenHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.databinding.PortfolioPlusPageBinding;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;
import com.ankhrom.coinmarketcap.listener.OnCoinSelectedListener;
import com.ankhrom.coinmarketcap.listener.OnPortfolioChangedListener;
import com.ankhrom.coinmarketcap.model.PortfolioPlusModel;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dialog.SearchViewModel;

/**
 * Created by R' on 1/2/2018.
 */

public class PortfolioPlusViewModel extends AppViewModel<PortfolioPlusPageBinding, PortfolioPlusModel> implements OnCoinSelectedListener {

    private CoinItem coin;
    private OnPortfolioChangedListener listener;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        coin = args.getArg(CoinItem.class);
        listener = args.getArg(OnPortfolioChangedListener.class);
    }

    @Override
    public void onInit() {
        super.onInit();

        setModel(new PortfolioPlusModel());

        if (coin != null) {
            model.currency.set(coin.symbol + " - " + coin.name);
        } else {
            coin = getDataHolder().getCoin("bitcoin");
            model.currency.set(coin.symbol + " - " + coin.name);
        }
    }

    public void onSearchPressed(View view) {

        addViewModel(SearchViewModel.class, this);
    }

    @Override
    public void onCoinSelected(CoinItem coin) {

        if (coin == null) {
            this.coin = null;
            model.currency.set("-");
            return;
        }

        this.coin = coin;
        model.currency.set(coin.symbol + " - " + coin.name);
    }

    public void onCreatePressed(View view) {

        if (coin == null || !(model.units.isValid() && model.unitPrice.isValid())) {
            return;
        }

        PortfolioItem item = new PortfolioItem();
        item.coinId = coin.id;
        item.amount = Double.parseDouble(model.units.get());
        item.unitPrice = Double.parseDouble(model.unitPrice.get());

        UserPrefs prefs = getUserPrefs();
        prefs.addPorfolioItem(item);

        if (listener != null) {
            listener.onPortfolioChanged(prefs.getPortfolio());
        }

        close();
    }

    public void onCancelPressed(View view) {
        close();
    }

    private void close() {

        ScreenHelper.hideSoftKeyboard(getBaseActivity());
        getNavigation().setPreviousViewModel();
        FragmentHelper.removePage(getContext(), this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.portfolio_plus_page;
    }
}
