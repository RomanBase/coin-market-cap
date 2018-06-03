package com.ankhrom.coinmarketcap.model.coin;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableFloat;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.api.ApiUrl;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.model.base.SortableCoinItemModel;

/**
 * Created by R' on 12/30/2017.
 */

public class CoinItemModel extends SortableCoinItemModel {

    public final CoinItem coin;

    public final String price;
    public final String supply;
    public final String change1h;
    public final String change24h;
    public final String change7d;
    public final String marketCap;
    public final String volume;

    public final Uri icon;

    public final ObservableBoolean isFavourite = new ObservableBoolean();
    public final ObservableFloat swipeProgress = new ObservableFloat();
    public final ObservableBoolean swipeDirectionLeft = new ObservableBoolean();

    private OnItemSelectedListener<CoinItemModel> itemSelectedLongListener;

    public CoinItemModel(@NonNull CoinItem item) {

        coin = item;

        isSortable = true;
        itemRank = Integer.parseInt(coin.rank);
        itemPrice = Double.parseDouble(coin.priceUsd);

        if (!StringHelper.isEmpty(coin.percentChange24h)) {
            itemChange24h = Double.parseDouble(coin.percentChange24h);
        } else {
            itemChange24h = 0.0;
        }

        price = ApiFormat.toPriceFormat(coin.priceUsd) + " $";
        supply = ApiFormat.toShortFormat(coin.supply);

        change1h = coin.percentChange1h + "%";
        change24h = coin.percentChange24h + "%";
        change7d = coin.percentChange7d + "%";

        marketCap = ApiFormat.toShortFormat(coin.marketCap);
        volume = ApiFormat.toShortFormat(coin.volumeUsd);

        icon = ApiUrl.icon(coin);
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
    public int getLayoutResource() {
        return R.layout.coin_item;
    }
}
