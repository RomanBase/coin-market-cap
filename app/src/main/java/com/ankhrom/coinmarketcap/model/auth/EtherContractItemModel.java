package com.ankhrom.coinmarketcap.model.auth;

import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.model.base.AppSelectableItemModel;

/**
 * Created by R' on 6/8/2018.
 */
public class EtherContractItemModel extends AppSelectableItemModel {

    public final String contract;

    public EtherContractItemModel(String contract) {
        this.contract = contract;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.ether_contract_item;
    }
}
