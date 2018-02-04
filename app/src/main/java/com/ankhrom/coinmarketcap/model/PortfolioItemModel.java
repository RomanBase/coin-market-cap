package com.ankhrom.coinmarketcap.model;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableFloat;
import android.databinding.ObservableList;

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

    public final ObservableString investedValue = new ObservableString();
    public final ObservableString currentValue = new ObservableString();
    public final ObservableString amount = new ObservableString();
    public final ObservableString avgPrice = new ObservableString();
    public final ObservableString profitLoss = new ObservableString();
    public final ObservableString profitLossAmount = new ObservableString();

    public final ObservableList<Integer> exchangeIcons = new ObservableArrayList<>();

    public final CoinItem coin;
    public final String marketPrice;

    public List<PortfolioItem> items;
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

        items = new ArrayList<>();
        items.add(item);
        exchangeIcons.clear();

        exchangeIcons.add(ExchangeTypeUtil.getIcon(item.exchange));

        double unitPrice = Double.parseDouble(coin.priceUsd);

        if (!(item.unitPrice > 0.0)) {
            item.unitPrice = unitPrice;
        }

        double profit = unitPrice / item.unitPrice;

        setUpModel(profit, item.unitPrice * item.amount, item.amount, item.unitPrice);
    }

    public void updateData(List<PortfolioItem> items) {

        this.items = items;
        exchangeIcons.clear();

        double priceSum = 0.0;
        double amountSum = 0.0;
        double unitPrice = Double.parseDouble(coin.priceUsd);

        double price = 0.0;
        double amount = 0.0;

        for (PortfolioItem item : items) {

            Integer icon = ExchangeTypeUtil.getIcon(item.exchange);
            if (!exchangeIcons.contains(icon)) {
                exchangeIcons.add(icon);
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

        double averagePrice = priceSum / amountSum;
        double profit = unitPrice / averagePrice;

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

        investedValue.set(ApiFormat.toPriceFormat(invested) + " $");
        currentValue.set(ApiFormat.toPriceFormat(current) + " $");
        amount.set(ApiFormat.toPriceFormat(amountSum));

        avgPrice.set(ApiFormat.toPriceFormat(averagePrice) + " $");

        if (Math.abs(profitAmount) < 0.1) {
            profitLoss.set("-");
            profitLossAmount.set("-");
        } else {
            profitLoss.set(ApiFormat.toDigitFormat(profit100) + "%");
            profitLossAmount.set((Math.abs(profitAmount) > 1.0 ? ApiFormat.toPriceFormat(profitAmount) : ApiFormat.toDigitFormat(profitAmount)) + " $");
        }
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
