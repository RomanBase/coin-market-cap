package com.ankhrom.coinmarketcap.listener;

import com.ankhrom.coinmarketcap.data.DataHolder;

/**
 * Created by R' on 12/31/2017.
 */

public interface DataLoadingListener {

    void onDataLoading(boolean isLoading, DataHolder holder);

    void onDataLoadingFailed(boolean isLoading, DataHolder holder);
}
