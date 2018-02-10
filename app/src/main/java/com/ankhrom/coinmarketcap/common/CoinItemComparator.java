package com.ankhrom.coinmarketcap.common;

import com.ankhrom.coinmarketcap.model.base.SortableCoinItemModel;
import com.ankhrom.coinmarketcap.viewmodel.dashboard.MarketViewModel;

import java.util.Comparator;

/**
 * Created by R' on 2/10/2018.
 */

public class CoinItemComparator {

    public static Comparator<SortableCoinItemModel> sort(final SortAction action) {

        return new Comparator<SortableCoinItemModel>() {
            @Override
            public int compare(SortableCoinItemModel a, SortableCoinItemModel b) {

                if (a.isSortable) {
                    if (b.isSortable) {
                        return action.sort(a, b);
                    } else {
                        return -1;
                    }
                } else if (b.isSortable) {
                    return 1;
                }

                return 0;
            }
        };
    }

    public static Comparator<SortableCoinItemModel> getSorter(MarketViewModel.SortState state) {

        switch (state) {
            case PRICE_UP:
                return sort(new SortAction() {
                    @Override
                    public int sort(SortableCoinItemModel a, SortableCoinItemModel b) {
                        return a.itemPrice == b.itemPrice ? 0 : a.itemPrice > b.itemPrice ? -1 : 1;
                    }
                });
            case PRICE_DOWN:
                return sort(new SortAction() {
                    @Override
                    public int sort(SortableCoinItemModel a, SortableCoinItemModel b) {
                        return a.itemPrice == b.itemPrice ? 0 : a.itemPrice < b.itemPrice ? -1 : 1;
                    }
                });
            case CHANGE_UP:
                return sort(new SortAction() {
                    @Override
                    public int sort(SortableCoinItemModel a, SortableCoinItemModel b) {
                        return a.itemChange24h == b.itemChange24h ? 0 : a.itemChange24h > b.itemChange24h ? -1 : 1;
                    }
                });
            case CHANGE_DOWN:
                return sort(new SortAction() {
                    @Override
                    public int sort(SortableCoinItemModel a, SortableCoinItemModel b) {
                        return a.itemChange24h == b.itemChange24h ? 0 : a.itemChange24h < b.itemChange24h ? -1 : 1;
                    }
                });
            default:
                return sort(new SortAction() {
                    @Override
                    public int sort(SortableCoinItemModel a, SortableCoinItemModel b) {
                        return a.itemRank == b.itemRank ? 0 : a.itemRank < b.itemRank ? -1 : 1;
                    }
                });
        }
    }

    public interface SortAction {

        int sort(SortableCoinItemModel a, SortableCoinItemModel b);
    }
}
