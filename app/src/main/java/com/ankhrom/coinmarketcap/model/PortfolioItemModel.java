package com.ankhrom.coinmarketcap.model;

import com.ankhrom.base.model.ItemModel;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;

import java.util.List;

/**
 * Created by R' on 1/2/2018.
 */

public class PortfolioItemModel extends ItemModel {

    public final ObservableString price = new ObservableString();
    public final ObservableString amount = new ObservableString();
    public final ObservableString avgPrice = new ObservableString();
    public final ObservableString profitLoss = new ObservableString();

    public final CoinItem coin;
    public final String marketPrice;

    public PortfolioItemModel(CoinItem coin) {

        this.coin = coin;

        marketPrice = ApiFormat.toPriceFormat(coin.priceUsd);
    }

    public PortfolioItemModel(CoinItem coin, List<PortfolioItem> items) {
        this(coin);

        updateData(items);
    }

    public void updateData(List<PortfolioItem> items) {

        double priceSum = priceSum(items);
        double amountSum = amountSum(items);
        double averagePrice = priceSum / amountSum;
        double profit = averagePrice / Double.parseDouble(coin.priceUsd);

        if (profit > 1.0f) {
            profit *= 100.0;
        } else {
            profit = -(1.0f - profit) * 100.0;
        }

        price.set(String.valueOf(priceSum));
        amount.set(String.valueOf(amountSum));

        avgPrice.set(String.valueOf(averagePrice));
        profitLoss.set(String.valueOf(profit) + "%");
    }

    private double priceSum(List<PortfolioItem> items) {

        double sum = 0.0f;

        for (PortfolioItem item : items) {
            sum += item.price;
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
        return BR.M;
    }
}
