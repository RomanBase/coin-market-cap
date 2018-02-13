package com.ankhrom.coinmarketcap.viewmodel.coin;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.android.volley.VolleyError;
import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.interfaces.viewmodel.CloseableViewModel;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.coincap.CapHistoryTimeFrame;
import com.ankhrom.coincap.CoinCap;
import com.ankhrom.coincap.entity.CapHistory;
import com.ankhrom.coincap.entity.CapHistoryItem;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiFormat;
import com.ankhrom.coinmarketcap.api.ApiUrl;
import com.ankhrom.coinmarketcap.databinding.CoinDetailPageBinding;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.listener.SelectableItem;
import com.ankhrom.coinmarketcap.model.coin.CoinDetailModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.robinhood.spark.SparkView;

import java.util.List;

/**
 * Created by R' on 1/10/2018.
 */

public class CoinDetailViewModel extends AppViewModel<CoinDetailPageBinding, CoinDetailModel> implements ResponseListener<CapHistory>, SparkView.OnScrubListener, CloseableViewModel {

    private CoinItem coin;
    private int timeframe = -1;

    private SelectableItem activeItem;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        coin = args.getArg(CoinItem.class);
        headerTitle.set(coin.symbol + " - " + coin.name + " (" + coin.rank + ")");
        headerSubTitle.set(ApiFormat.toTimeFormat(coin.timestamp + "000"));
        headerInfo.set(ApiFormat.toShortFormat(coin.marketCap));
        headerSubInfo.set(ApiFormat.toShortFormat(coin.volumeUsd));
    }

    @Override
    protected void onCreateViewBinding(CoinDetailPageBinding binding) {
        super.onCreateViewBinding(binding);

        activeItem = binding.selectedButton;
    }

    @Override
    public void loadModel() {

        isLoading.set(true);

        CoinCap.init(getContext()).getHistory(coin.symbol, CapHistoryTimeFrame.D_1, this);

        setModel(new CoinDetailModel(coin));
    }

    @Override
    public void onResponse(@Nullable CapHistory response) {

        if (response == null) {
            isLoading.set(false);
            error.set(true);
            return;
        }

        int count = response.price.size();
        int offset = 0;

        if (timeframe > 0) {
            offset = count - timeframe * 12 - 3;
        }

        List<List<Double>> sublist = response.price.subList(offset, count - 1);

        response.price = new CapHistoryItem();
        response.price.addAll(sublist);

        model.min = Double.MAX_VALUE;
        model.mid = 0.0;
        model.max = 0.0;
        model.midTime.set("mid");

        response.price.iterate(new CapHistoryItem.Iterator() {

            @Override
            protected void onNext(double timestamp, double value) {

                if (value > model.max) {
                    model.max = value;
                } else if (value < model.min) {
                    model.min = value;
                }

                model.mid += value;
            }
        });

        model.minPrice.set(ApiFormat.toPriceFormat(model.min) + " $");
        model.maxPrice.set(ApiFormat.toPriceFormat(model.max) + " $");
        model.midPrice.set(ApiFormat.toPriceFormat(model.mid / (double) response.price.size()) + " $");
        model.setAdapterValues(response.price);

        isLoading.set(false);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();

        isLoading.set(false);
        super.error.set(true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onScrubbed(Object value) {

        if (isModelAvailable() && value != null) {
            List<Double> values = (List<Double>) value;

            model.midPrice.set(ApiFormat.toPriceFormat(values.get(1)) + " $");
            model.midTime.set(ApiFormat.toTimeShortFormat((long) (double) values.get(0)));
        }
    }

    private void showCoinCapPage() {

        getNavigation().setPreviousViewModel();
        FragmentHelper.removePage(getContext(), this);

        try {
            new CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                    .setShowTitle(true)
                    .build()
                    .launchUrl(getContext(), Uri.parse(ApiUrl.COIN_MARKET_CAP_CURRECY + model.coin.id));

            addViewModel(CoinDetailViewModel.class, model.coin);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPressed3H(View view) {

        if (isLoading.get()) {
            return;
        }

        isLoading.set(true);
        timeframe = 3;
        toggleItem(view);

        CoinCap.init(getContext()).getHistory(coin.symbol, CapHistoryTimeFrame.D_1, this);
    }

    public void onPressed12H(View view) {

        if (isLoading.get()) {
            return;
        }

        isLoading.set(true);
        timeframe = 12;
        toggleItem(view);

        CoinCap.init(getContext()).getHistory(coin.symbol, CapHistoryTimeFrame.D_1, this);
    }

    public void onPressed24H(View view) {

        if (isLoading.get()) {
            return;
        }

        isLoading.set(true);
        timeframe = -1;
        toggleItem(view);

        CoinCap.init(getContext()).getHistory(coin.symbol, CapHistoryTimeFrame.D_1, this);
    }

    public void onPressed7D(View view) {

        if (isLoading.get()) {
            return;
        }

        isLoading.set(true);
        timeframe = -1;
        toggleItem(view);

        CoinCap.init(getContext()).getHistory(coin.symbol, CapHistoryTimeFrame.D_7, this);
    }

    public void onPressed30D(View view) {

        if (isLoading.get()) {
            return;
        }

        isLoading.set(true);
        timeframe = -1;
        toggleItem(view);

        CoinCap.init(getContext()).getHistory(coin.symbol, CapHistoryTimeFrame.D_30, this);
    }

    public void onPressed90D(View view) {

        if (isLoading.get()) {
            return;
        }

        isLoading.set(true);
        timeframe = -1;
        toggleItem(view);

        CoinCap.init(getContext()).getHistory(coin.symbol, CapHistoryTimeFrame.D_90, this);
    }

    public void onPressed180D(View view) {

        if (isLoading.get()) {
            return;
        }

        isLoading.set(true);
        timeframe = -1;
        toggleItem(view);

        CoinCap.init(getContext()).getHistory(coin.symbol, CapHistoryTimeFrame.D_180, this);
    }

    public void onPressed1Y(View view) {

        if (isLoading.get()) {
            return;
        }

        isLoading.set(true);
        timeframe = -1;
        toggleItem(view);

        CoinCap.init(getContext()).getHistory(coin.symbol, CapHistoryTimeFrame.Y_1, this);
    }

    private void toggleItem(View view) {

        if (activeItem != null) {
            activeItem.setItemSelected(false);
            activeItem = null;
        }

        if (view instanceof SelectableItem) {
            activeItem = (SelectableItem) view;
        }

        if (activeItem != null) {
            activeItem.setItemSelected(true);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.coin_detail_page;
    }

    @Override
    public boolean isCloseable() {
        return true;
    }
}
