package com.ankhrom.coinmarketcap.model;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.model.SelectableItemModel;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.common.ExchangeTypeUtil;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 1/2/2018.
 */

public class PortfolioItemModel extends SelectableItemModel {

    public final ObservableFloat swipeProgress = new ObservableFloat();
    public final ObservableBoolean swipeDirectionLeft = new ObservableBoolean();
    public final ObservableBoolean isEditable = new ObservableBoolean();
    public final ObservableBoolean isFavourite = new ObservableBoolean();

    public final ObservableString investedValue = new ObservableString();
    public final ObservableString currentValue = new ObservableString();
    public final ObservableString amount = new ObservableString();
    public final ObservableString avgPrice = new ObservableString();
    public final ObservableString profitLoss = new ObservableString();
    public final ObservableString profitLossAmount = new ObservableString();

    public final ObservableField<List<Integer>> exchangeIcons = new ObservableField<>();

    public final CoinItem coin;
    public final String marketPrice;

    public List<PortfolioItem> items;
    public double invested;
    public double current;
    public double currentAmount;

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

        isEditable.set(true);

        items = new ArrayList<>();
        items.add(item);
        List<Integer> icons = new ArrayList<>();

        icons.add(ExchangeTypeUtil.getIcon(item.exchange));

        exchangeIcons.set(icons);

        double unitPrice = StringHelper.isEmpty(coin.priceUsd) ? 0.0 : Double.parseDouble(coin.priceUsd);

        if (!(item.unitPrice > 0.0)) {
            item.unitPrice = unitPrice;
        }

        double profit = item.unitPrice > 0.0 ? unitPrice / item.unitPrice : 0.0;

        setUpModel(profit, item.unitPrice * item.amount, item.amount, item.unitPrice);
    }

    public void updateData(List<PortfolioItem> items) {

        isEditable.set(false);

        this.items = new ArrayList<>(items);

        double priceSum = 0.0;
        double amountSum = 0.0;
        double unitPrice = StringHelper.isEmpty(coin.priceUsd) ? 0.0 : Double.parseDouble(coin.priceUsd);

        double price = 0.0;
        double amount = 0.0;

        List<Integer> icons = new ArrayList<>();

        for (PortfolioItem item : items) {

            Integer icon = ExchangeTypeUtil.getIcon(item.exchange);
            if (!icons.contains(icon)) {
                icons.add(icon);
            }

            if (!(item.unitPrice > 0.0)) {
                item.unitPrice = unitPrice;
            }

            if (ExchangeTypeUtil.isPortfolioPriceAvailable(item.exchange)) {
                price += item.unitPrice * item.amount;
                amount += item.amount;
            }

            priceSum += item.unitPrice * item.amount;
            amountSum += item.amount;
        }

        exchangeIcons.set(icons);

        double averagePrice = amountSum > 0.0 ? priceSum / amountSum : 0.0;
        double profit = averagePrice > 0.0 ? unitPrice / averagePrice : 0.0;

        setUpModel(profit, priceSum, amountSum, averagePrice);

        if (amount > 0.0) {

            double avgProfit = unitPrice / (price / amount);
            double profit100 = getProfit100(avgProfit);

            profitLoss.set(ApiFormat.toDigitFormat(profit100) + "%");
        }
    }

    private void setUpModel(double profit, double priceSum, double amountSum, double averagePrice) {

        double profit100 = getProfit100(profit);
        double profitAmount = profit100 / 100.0 * priceSum;

        invested = priceSum;
        current = invested * profit;

        currentValue.set(ApiFormat.toPriceFormat(current) + " $");
        amount.set(ApiFormat.toPriceFormat(amountSum));
        currentAmount = amountSum;

        avgPrice.set(ApiFormat.toPriceFormat(averagePrice) + " $");

        if (Math.abs(profitAmount) < 0.1) {
            profitLoss.set("-");
            profitLossAmount.set("-");
            //investedValue.set("-");
        } else {
            profitLoss.set(ApiFormat.toDigitFormat(profit100) + "%");
            profitLossAmount.set((Math.abs(profitAmount) > 1.0 ? ApiFormat.toPriceFormat(profitAmount) : ApiFormat.toDigitFormat(profitAmount)) + " $");
            //investedValue.set(ApiFormat.toPriceFormat(invested) + " $");
        }

        investedValue.set(ApiFormat.toPriceFormat(amountSum * Double.parseDouble(coin.priceBtc)));
    }

    private double getProfit100(double profit) {

        if (profit > 1.0) {
            return profit * 100.0 - 100.0;
        } else {
            return -(1.0 - profit) * 100.0;
        }
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
