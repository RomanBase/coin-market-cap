package com.ankhrom.coinmarketcap.data;

/**
 * Created by R' on 12/31/2017.
 */

public interface DataLoadingListener {

    void onDataLoading(boolean isLoading, DataHolder holder);

    void onDataLoadingFailed(boolean isLoading, DataHolder holder);
}
