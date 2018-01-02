package com.ankhrom.coinmarketcap.model;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableFloat;
import android.view.View;

import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.model.SelectableItemModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.entity.CoinItem;

/**
 * Created by R' on 12/30/2017.
 */

public class CoinItemModel extends SelectableItemModel {

    public final CoinItem coin;

    public final String price;
    public final String supply;
    public final String change1h;
    public final String change24h;
    public final String marketCap;
    public final String volume;

    public final ObservableBoolean isFavourite = new ObservableBoolean();
    public final ObservableFloat swipeProgress = new ObservableFloat();
    public final ObservableBoolean swipeDirectionLeft = new ObservableBoolean();

    private OnItemSelectedListener<CoinItemModel> itemSelectedLongListener;

    public CoinItemModel(CoinItem item) {

        coin = item;

        price = ApiFormat.toPriceFormat(coin.priceUsd) + " $";
        supply = ApiFormat.toShortFormat(coin.supply);

        change1h = coin.percentChange1h + "%";
        change24h = coin.percentChange24h + "%";

        marketCap = ApiFormat.toShortFormat(coin.marketCap);
        volume = ApiFormat.toShortFormat(coin.volumeUsd);
    }

    public void setOnItemSelectedLongListener(OnItemSelectedListener<CoinItemModel> itemSelectedLongListener) {
        this.itemSelectedLongListener = itemSelectedLongListener;
    }

    public boolean onItemLongSelected(View view) {

        if (itemSelectedLongListener != null) {
            itemSelectedLongListener.onItemSelected(view, this);
            return true;
        }

        return false;
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.coin_item;
    }
}
