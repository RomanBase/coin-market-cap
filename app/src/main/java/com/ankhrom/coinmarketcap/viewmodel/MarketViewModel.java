package com.ankhrom.coinmarketcap.viewmodel;

import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.interfaces.ObjectConverter;
import com.ankhrom.base.viewmodel.BaseViewModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.CoinItem;
import com.ankhrom.coinmarketcap.api.MarketData;
import com.ankhrom.coinmarketcap.data.DataHolder;
import com.ankhrom.coinmarketcap.data.DataLoadingListener;
import com.ankhrom.coinmarketcap.databinding.MarketPageBinding;
import com.ankhrom.coinmarketcap.model.CoinItemModel;
import com.ankhrom.coinmarketcap.model.CoinsAdapterModel;

import java.util.List;

/**
 * Created by R' on 12/30/2017.
 */

public class MarketViewModel extends BaseViewModel<MarketPageBinding, CoinsAdapterModel> implements DataLoadingListener {

    @Override
    public void onInit() {
        super.onInit();

        requestData();
    }

    private void requestData() {

        DataHolder holder = getFactory().get(DataHolder.class);
        holder.getFetcher().addListener(this);
    }

    private void updateModel(List<CoinItemModel> items, MarketData market) {

        setModel(new CoinsAdapterModel(getContext(), items, market));
    }

    private List<CoinItemModel> convertCoinItems(List<CoinItem> coins) {

        return ObjectHelper.convert(coins, new ObjectConverter<CoinItemModel, CoinItem>() {
            @Override
            public CoinItemModel convert(CoinItem object) {
                return new CoinItemModel(object);
            }
        });
    }

    @Override
    public void onDataLoading(boolean isLoading, DataHolder holder) {

        this.isLoading.set(isLoading);

        if (!isLoading) {
            updateModel(convertCoinItems(holder.getCoins()), holder.getMarket());
        }
    }

    @Override
    public void onDataLoadingFailed(boolean isLoading, DataHolder holder) {

        this.isLoading.set(isLoading);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.market_page;
    }

    @Override
    public int getBindingResource() {
        return BR.VM;
    }
}
