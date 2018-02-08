package com.ankhrom.coinmarketcap.listener;

import com.ankhrom.coinmarketcap.entity.PortfolioItem;

/**
 * Created by R' on 2/8/2018.
 */

public interface OnPortfolioChangedListener {

    void onPortfolioItemAdded(PortfolioItem item);

    void onPortfolioItemRemoved(PortfolioItem item);
}
