package com.ankhrom.coinmarketcap.model.dialog;

import android.content.Context;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.coinmarketcap.BR;

import java.util.Collection;

/**
 * Created by R' on 1/27/2018.
 */

public class DonationAdapterModel extends AdapterModel<DonationWalletItemModel> {

    public DonationAdapterModel(Context context) {
        super(context);
    }

    public DonationAdapterModel(Context context, Collection<DonationWalletItemModel> collection) {
        super(context, collection);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
