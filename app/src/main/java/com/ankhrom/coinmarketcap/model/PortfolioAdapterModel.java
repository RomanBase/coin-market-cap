package com.ankhrom.coinmarketcap.model;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.base.model.ItemModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.model.coin.CoinAdapterFooterItemModel;

import java.util.Collection;

/**
 * Created by R' on 1/2/2018.
 */

public class PortfolioAdapterModel extends AdapterModel<ItemModel> {

    public ObservableBoolean isEmpty = new ObservableBoolean(true);

    public final String change1h;
    public final String change24h;
    public final String change7d;
    public final String marketCap;
    public final String unitPrice;
    public final String bitcoinUnitValue;

    private OnItemSelectedListener listener;

    public PortfolioAdapterModel(Context context) {
        this(context, null);
    }

    public PortfolioAdapterModel(Context context, CoinItem coin) {
        super(context);

        if (coin != null) {
            change1h = coin.percentChange1h + "%";
            change24h = coin.percentChange24h + "%";
            change7d = coin.percentChange7d + "%";
            marketCap = ApiFormat.toShortFormat(coin.marketCap);
            unitPrice = ApiFormat.toPriceFormat(coin.priceUsd) + " $";
            bitcoinUnitValue = ApiFormat.toPriceFormat(coin.priceBtc);
        } else {
            change1h = change24h = change7d = marketCap = unitPrice = bitcoinUnitValue = null;
        }
    }

    public void setOnAddItemPressedListener(OnItemSelectedListener listener) {

        this.listener = listener;
    }

    public void replace(@NonNull Collection<? extends ItemModel> collection) {

        replace(collection, true);
    }

    public void replace(@NonNull Collection<? extends ItemModel> collection, boolean addFooter) {

        isEmpty.set(collection.size() == 0);
        adapter.replace(collection);

        if(addFooter) {
            adapter.add(new CoinAdapterFooterItemModel(listener));
        }
    }

    public void checkEmptiness() {

        isEmpty.set(adapter.getItemCount() <= 1/*footer*/);
    }

    public void checkFooter() {

        if (adapter.getItemCount() == 0 || !(adapter.get(adapter.getItemCount() - 1) instanceof CoinAdapterFooterItemModel)) {
            adapter.add(new CoinAdapterFooterItemModel(listener));
        }
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
