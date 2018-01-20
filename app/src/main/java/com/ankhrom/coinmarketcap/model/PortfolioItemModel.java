package com.ankhrom.coinmarketcap.model;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableFloat;

import com.ankhrom.base.model.SelectableItemModel;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;

import java.util.List;

/**
 * Created by R' on 1/2/2018.
 */

public class PortfolioItemModel extends SelectableItemModel {

    public final ObservableFloat swipeProgress = new ObservableFloat();
    public final ObservableBoolean swipeDirectionLeft = new ObservableBoolean();

    public final ObservableString investedValue = new ObservableString();
    public final ObservableString currentValue = new ObservableString();
    public final ObservableString amount = new ObservableString();
    public final ObservableString avgPrice = new ObservableString();
    public final ObservableString profitLoss = new ObservableString();
    public final ObservableString profitLossAmount = new ObservableString();

    public final CoinItem coin;
    public final String marketPrice;

    public double invested;
    public double current;

    public PortfolioItemModel(CoinItem coin) {

        this.coin = coin;

        marketPrice = ApiFormat.toPriceFormat(coin.priceUsd);
    }

    public PortfolioItemModel(CoinItem coin, PortfolioItem item) {
        this(coin);

        updateData(item);
    }

    public PortfolioItemModel(CoinItem coin, List<PortfolioItem> items) {
        this(coin);

        updateData(items);
    }

    public void updateData(PortfolioItem item) {

        if (!(item.unitPrice < 0.0f)) {
            item.unitPrice = Double.parseDouble(coin.priceUsd) - 0.000001;
        }

        double profit = Double.parseDouble(coin.priceUsd) / item.unitPrice;
        double profit100;

        if (profit > 1.0f) {
            profit100 = profit * 100.0 - 100.0;
        } else {
            profit100 = -(1.0f - profit) * 100.0;
        }

        double profitAmount = profit100 / 100.0 * invested;

        invested = item.unitPrice * item.amount;
        current = invested * profit;

        investedValue.set(ApiFormat.toPriceFormat(invested) + " $");
        currentValue.set(ApiFormat.toPriceFormat(current) + " $");
        amount.set(ApiFormat.toPriceFormat(item.amount));

        avgPrice.set(ApiFormat.toPriceFormat(item.unitPrice));
        profitLoss.set(ApiFormat.toDigitFormat(profit100) + "%");

        profitLossAmount.set(Math.abs(profitAmount) > 1.0 ? ApiFormat.toPriceFormat(profitAmount) : ApiFormat.toDigitFormat(profitAmount));
    }

    public void updateData(List<PortfolioItem> items) {

        double priceSum = priceSum(items);
        double amountSum = amountSum(items);
        double averagePrice = priceSum / amountSum;
        double profit = Double.parseDouble(coin.priceUsd) / averagePrice;
        double profit100;

        if (profit > 1.0f) {
            profit100 = profit * 100.0 - 100.0;
        } else {
            profit100 = -(1.0f - profit) * 100.0;
        }

        double profitAmount = profit100 / 100.0 * priceSum;

        invested = priceSum;
        current = invested * profit;

        investedValue.set(ApiFormat.toPriceFormat(invested) + " $");
        currentValue.set(ApiFormat.toPriceFormat(current) + " $");
        amount.set(ApiFormat.toPriceFormat(amountSum));

        avgPrice.set(ApiFormat.toPriceFormat(averagePrice));
        profitLoss.set(ApiFormat.toDigitFormat(profit100) + "%");

        profitLossAmount.set(Math.abs(profitAmount) > 1.0 ? ApiFormat.toPriceFormat(profitAmount) : ApiFormat.toDigitFormat(profitAmount));
    }

    private double priceSum(List<PortfolioItem> items) {

        double sum = 0.0f;

        for (PortfolioItem item : items) {

            if (!(item.unitPrice < 0.0f)) {
                item.unitPrice = Double.parseDouble(coin.priceUsd) - 0.000001;
            }

            sum += item.unitPrice * item.amount;
        }

        return sum;
    }

    private double amountSum(List<PortfolioItem> items) {

        double sum = 0.0f;

        for (PortfolioItem item : items) {
            sum += item.amount;
        }

        return sum;
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.portfolio_item;
    }
}
