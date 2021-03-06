package com.ankhrom.coinmarketcap.listener;

import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.entity.PortfolioCoin;

import java.util.List;

/**
 * Created by R' on 1/2/2018.
 */

public interface OnExchangePortfolioChangedListener {

    void onPortfolioChanged(ExchangeType exchange, List<PortfolioCoin> portfolio);
}
