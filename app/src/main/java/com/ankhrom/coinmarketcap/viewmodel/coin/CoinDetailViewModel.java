package com.ankhrom.coinmarketcap.viewmodel.coin;

import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.databinding.CoinDetailPageBinding;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.model.coin.CoinDetailModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

/**
 * Created by R' on 1/10/2018.
 */

public class CoinDetailViewModel extends AppViewModel<CoinDetailPageBinding, CoinDetailModel> {

    private CoinItem coin;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        coin = args.getArg(CoinItem.class);
    }

    @Override
    public void loadModel() {

        setModel(new CoinDetailModel(coin));
    }

    @Override
    public int getLayoutResource() {
        return R.layout.coin_detail_page;
    }
}
