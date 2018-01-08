package com.ankhrom.coinmarketcap.viewmodel.dashboard;

import android.view.View;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.interfaces.OnValueChangedListener;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.databinding.CalcPageBinding;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.listener.OnCoinSelectedListener;
import com.ankhrom.coinmarketcap.model.CalcModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dialog.SearchViewModel;

/**
 * Created by R' on 1/8/2018.
 */

public class CalcViewModel extends AppViewModel<CalcPageBinding, CalcModel> implements OnCoinSelectedListener {

    private CoinItem coin;
    private CoinItem bitcoin;

    @Override
    public void onInit() {
        super.onInit();

        headerTitle.set("Calculator");
    }

    @Override
    public void loadModel() {
        super.loadModel();

        bitcoin = getDataHolder().getCoin("bitcoin");

        setModel(new CalcModel());

        model.units.setOnValueChangedListener(onUnitsChanged);
        model.sumPrice.setOnValueChangedListener(onSumPriceChanged);
        model.bitcoinUnits.setOnValueChangedListener(onBitcionUnitsChanged);
        model.profit.setOnValueChangedListener(onProfitChanged);

        onCoinSelected(bitcoin);
    }

    private final OnValueChangedListener<String> onUnitsChanged = new OnValueChangedListener<String>() {
        @Override
        public void onValueChanged(String value) {

            if (StringHelper.isEmpty(value)) {
                model.sumPrice.setValue(null);
                model.bitcoinUnits.setValue(null);
                return;
            }

            value = ensureFormat(value);

            double units = Double.parseDouble(value);
            double unitPrice = Double.parseDouble(coin.priceUsd);
            double btcPrice = Double.parseDouble(bitcoin.priceUsd);

            double sumPrice = unitPrice * units;

            model.sumPrice.setValue(String.valueOf(sumPrice));
            model.bitcoinUnits.setValue(ApiFormat.toPriceFormat(sumPrice / btcPrice));

            onProfitChanged.onValueChanged(model.profit.get());
        }
    };

    private final OnValueChangedListener<String> onSumPriceChanged = new OnValueChangedListener<String>() {
        @Override
        public void onValueChanged(String value) {

            if (StringHelper.isEmpty(value)) {
                model.units.setValue(null);
                model.bitcoinUnits.setValue(null);
                return;
            }

            value = ensureFormat(value);

            double sumPrice = Double.parseDouble(value);
            double unitPrice = Double.parseDouble(coin.priceUsd);
            double btcPrice = Double.parseDouble(bitcoin.priceUsd);

            double units = sumPrice / unitPrice;

            model.units.setValue(String.valueOf(units));
            model.bitcoinUnits.setValue(ApiFormat.toPriceFormat(sumPrice / btcPrice));

            onProfitChanged.onValueChanged(model.profit.get());
        }
    };

    private final OnValueChangedListener<String> onBitcionUnitsChanged = new OnValueChangedListener<String>() {
        @Override
        public void onValueChanged(String value) {

            if (StringHelper.isEmpty(value)) {
                model.units.setValue(null);
                model.sumPrice.setValue(null);
                return;
            }

            value = ensureFormat(value);

            double btcUnits = Double.parseDouble(value);
            double unitPrice = Double.parseDouble(coin.priceUsd);
            double btcPrice = Double.parseDouble(bitcoin.priceUsd);

            double sumPrice = btcUnits * btcPrice;

            model.units.setValue(String.valueOf(sumPrice / unitPrice));
            model.sumPrice.setValue(String.valueOf(sumPrice));

            onProfitChanged.onValueChanged(model.profit.get());
        }
    };

    private final OnValueChangedListener<Integer> onProfitChanged = new OnValueChangedListener<Integer>() {
        @Override
        public void onValueChanged(Integer value) {

            double profit = model.profit.getPercentage() * 10.0;
            model.profitLabel.set("profit " + value * 10 + "%");

            if (StringHelper.isEmpty(model.sumPrice.get()) || StringHelper.isEmpty(model.unitPrice.get())) {
                return;
            }

            double sumPrice = Double.parseDouble(model.sumPrice.get());
            double unitPrice = Double.parseDouble(coin.priceUsd);

            model.profitSumPrice.set(ApiFormat.toPriceFormat(sumPrice + sumPrice * profit));
            model.profitUnitPrice.set(ApiFormat.toPriceFormat(unitPrice + unitPrice * profit));
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

        this.coin = coin;

        if (coin == null) {
            model.currency.set("-");
            model.units.set(null);
            model.sumPrice.set(null);
            model.unitPrice.set(null);
            model.bitcoinUnits.set(null);
            model.bitcoinUnitValue.set(null);
        }

        model.currency.set(coin.toString());
        model.unitPrice.setValue(ApiFormat.toPriceFormat(coin.priceUsd));
        model.bitcoinUnitValue.setValue(ApiFormat.toPriceFormat(coin.priceBtc));

        if (!StringHelper.isEmpty(model.units)) {
            onSumPriceChanged.onValueChanged(model.sumPrice.get());
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.calc_page;
    }
}
