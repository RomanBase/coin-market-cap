package com.ankhrom.coinmarketcap.model.coin;

import android.databinding.ObservableField;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.model.Model;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.coincap.entity.CapHistoryItem;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.robinhood.spark.SparkAdapter;

/**
 * Created by R' on 1/10/2018.
 */

public class CoinDetailModel extends Model {

    public final CoinItem coin;
    public final String marketCap;
    public final String unitPrice;
    public final String bitcoinUnitValue;
    public final String change24h;
    public final String change1h;
    public final String change7d;
    public final String supplyCurrent;
    public final String supplyMax;

    public final ObservableString minPrice = new ObservableString();
    public final ObservableString midPrice = new ObservableString();
    public final ObservableString maxPrice = new ObservableString();
    public final ObservableString midTime = new ObservableString();

    public final ObservableField<SparkAdapter> adapter = new ObservableField<>();

    public double min;
    public double mid;
    public double max;

    public CoinDetailModel(CoinItem coin) {

        this.coin = coin;

        marketCap = ApiFormat.toShortFormat(coin.marketCap);
        unitPrice = ApiFormat.toPriceFormat(coin.priceUsd) + " $";
        bitcoinUnitValue = ApiFormat.toPriceFormat(coin.priceBtc);
        change24h = coin.percentChange24h + "%";
        change1h = coin.percentChange1h + "%";
        change7d = coin.percentChange7d + "%";

        supplyCurrent = ApiFormat.toShortFormat(coin.supply);
        supplyMax = StringHelper.isEmpty(coin.supplyMax) ? "∞" : ApiFormat.toShortFormat(coin.supplyMax);
    }

    public void setAdapterValues(final CapHistoryItem data) {

        adapter.set(new SparkAdapter() {
            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int index) {
                return data.get(index);
            }

            @Override
            public float getY(int index) {
                return (float) (double) data.get(index).get(1);
            }
        });
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
