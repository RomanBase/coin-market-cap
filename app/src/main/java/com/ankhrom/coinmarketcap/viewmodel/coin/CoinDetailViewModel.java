package com.ankhrom.coinmarketcap.viewmodel.coin;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.android.volley.VolleyError;
import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.custom.args.InitArgs;
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
import com.ankhrom.coinmarketcap.model.coin.CoinDetailModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.robinhood.spark.SparkAdapter;
import com.robinhood.spark.SparkView;

import java.util.Date;

/**
 * Created by R' on 1/10/2018.
 */

public class CoinDetailViewModel extends AppViewModel<CoinDetailPageBinding, CoinDetailModel> implements ResponseListener<CapHistory> {

    private CoinItem coin;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        isLoading.set(true);

        coin = args.getArg(CoinItem.class);
        headerTitle.set(coin.symbol + " - " + coin.name);
        headerSubTitle.set(new Date(Long.parseLong(coin.timestamp) * 1000).toLocaleString());
        headerInfo.set(ApiFormat.toShortFormat(coin.marketCap));
        headerSubInfo.set(ApiFormat.toShortFormat(coin.volumeUsd));
    }

    @Override
    public void loadModel() {

        CoinCap.init(getContext()).getHistory(coin.symbol, CapHistoryTimeFrame.Y_1, this);
    }

    @Override
    public void onResponse(@Nullable CapHistory response) {

        if (response == null) {
            return;
        }

        final float[] prices = new float[response.price.size()];

        response.price.iterate(new CapHistoryItem.Iterator() {

            int i = 0;

            @Override
            protected void onNext(double timestamp, double value) {
                prices[i++] = (float) value;
            }
        });

        binding.sparkline.setAdapter(new SparkAdapter() {

            @Override
            public int getCount() {
                return prices.length;
            }

            @Override
            public Object getItem(int index) {
                return prices[index];
            }

            @Override
            public float getY(int index) {
                return prices[index];
            }
        });

        binding.sparkline.setScrubListener(new SparkView.OnScrubListener() {
            @Override
            public void onScrubbed(Object value) {

            }
        });

        isLoading.set(false);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        isLoading.set(false);
        showCoinCapPage();
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

    @Override
    public int getLayoutResource() {
        return R.layout.coin_detail_page;
    }


}
