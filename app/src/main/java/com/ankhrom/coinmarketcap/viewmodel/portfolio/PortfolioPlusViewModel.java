package com.ankhrom.coinmarketcap.viewmodel.portfolio;

import android.view.View;

import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.common.statics.ScreenHelper;
import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.interfaces.OnValueChangedListener;
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
    private CoinItem bitcoin;
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


    }

    @Override
    public void loadModel() {
        super.loadModel();

        setModel(new PortfolioPlusModel());

        bitcoin = getDataHolder().getCoin("bitcoin");

        model.units.setOnValueChangedListener(onUnitCountChanged);
        model.unitPrice.setOnValueChangedListener(onUnitPriceChanged);
        model.sumPrice.setOnValueChangedListener(onSumPriceChanged);
        model.bitcoinUnits.setOnValueChangedListener(onBitcoinUnitsChanged);

        if (coin != null) {
            model.editableCurrency.set(false);
            model.currency.set(coin.symbol + " - " + coin.name);
        } else {
            coin = bitcoin;
            model.currency.set(coin.symbol + " - " + coin.name);
        }
    }

    public final OnValueChangedListener<String> onUnitCountChanged = new OnValueChangedListener<String>() {
        @Override
        public void onValueChanged(String value) {

            if (StringHelper.isEmpty(value)) {
                return;
            }

            value = ensureFormat(value);

            boolean sumAvailable = !StringHelper.isEmpty(model.sumPrice.get());
            boolean unitAvailable = !StringHelper.isEmpty(model.unitPrice.get());
            double btcPrice = Double.parseDouble(bitcoin.priceUsd);

            if (sumAvailable) {
                double price = Double.parseDouble(model.sumPrice.get());
                double unitPrice = price / Double.parseDouble(value);
                model.unitPrice.setValue(String.valueOf(unitPrice));
                model.bitcoinUnits.setValue(String.valueOf(price / btcPrice));
                model.bitcoinUnitValue.setValue(String.valueOf(unitPrice / btcPrice));
            } else if (unitAvailable) {
                double unitPrice = Double.parseDouble(model.unitPrice.get());
                double price = unitPrice * Double.parseDouble(value);
                model.sumPrice.setValue(String.valueOf(price));
                model.bitcoinUnits.setValue(String.valueOf(price / btcPrice));
                model.bitcoinUnitValue.setValue(String.valueOf(unitPrice / btcPrice));
            }
        }
    };

    public final OnValueChangedListener<String> onUnitPriceChanged = new OnValueChangedListener<String>() {
        @Override
        public void onValueChanged(String value) {

            if (StringHelper.isEmpty(value) || StringHelper.isEmpty(model.units.get())) {
                model.sumPrice.setValue(null);
                model.bitcoinUnits.setValue(null);
                model.bitcoinUnitValue.setValue(null);
                return;
            }

            value = ensureFormat(value);

            double unitPrice = Double.parseDouble(value);
            double amount = Double.parseDouble(model.units.get());
            double price = unitPrice * amount;
            double btcPrice = Double.parseDouble(bitcoin.priceUsd);

            model.sumPrice.setValue(String.valueOf(price));
            model.bitcoinUnits.setValue(String.valueOf(price / btcPrice));
            model.bitcoinUnitValue.setValue(String.valueOf(unitPrice / btcPrice));
        }
    };

    public final OnValueChangedListener<String> onSumPriceChanged = new OnValueChangedListener<String>() {
        @Override
        public void onValueChanged(String value) {

            if (StringHelper.isEmpty(value) || StringHelper.isEmpty(model.units.get())) {
                model.unitPrice.setValue(null);
                model.bitcoinUnits.setValue(null);
                model.bitcoinUnitValue.setValue(null);
                return;
            }

            value = ensureFormat(value);

            double price = Double.parseDouble(value);
            double amount = Double.parseDouble(model.units.get());
            double unitPrice = price / amount;
            double btcPrice = Double.parseDouble(bitcoin.priceUsd);

            model.unitPrice.setValue(String.valueOf(unitPrice));
            model.bitcoinUnits.setValue(String.valueOf(price / btcPrice));
            model.bitcoinUnitValue.setValue(String.valueOf(unitPrice / btcPrice));
        }
    };

    public final OnValueChangedListener<String> onBitcoinUnitsChanged = new OnValueChangedListener<String>() {
        @Override
        public void onValueChanged(String value) {

            if (StringHelper.isEmpty(value) || StringHelper.isEmpty(model.units.get())) {
                return;
            }

            value = ensureFormat(value);

            double btcUnits = Double.parseDouble(value);
            double amount = Double.parseDouble(model.units.get());
            double btcPrice = Double.parseDouble(bitcoin.priceUsd);

            double price = btcUnits * btcPrice;
            double unitPrice = price / amount;

            model.sumPrice.setValue(String.valueOf(price));
            model.unitPrice.setValue(String.valueOf(unitPrice));
            model.bitcoinUnitValue.setValue(String.valueOf(unitPrice / btcPrice));
        }
    };

    private String ensureFormat(String value) {

        return value.startsWith(".") || value.startsWith(",") ? value + "0" : value;
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
