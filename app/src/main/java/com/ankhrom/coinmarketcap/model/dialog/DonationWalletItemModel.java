package com.ankhrom.coinmarketcap.model.dialog;

import android.databinding.ObservableBoolean;
import android.view.View;

import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.model.base.AppSelectableItemModel;

/**
 * Created by R' on 1/27/2018.
 */

public class DonationWalletItemModel extends AppSelectableItemModel {

    public final String currency;
    public final String address;
    public final int qr;

    public ObservableBoolean isExpanded = new ObservableBoolean();

    private OnItemSelectedListener expandListener;

    public DonationWalletItemModel(OnItemSelectedListener listener, OnItemSelectedListener expandListener, String currency, String address, int qr) {
        super(listener);
        setExpandListener(expandListener);

        this.currency = currency;
        this.address = address;
        this.qr = qr;
    }

    public void setExpandListener(OnItemSelectedListener expandListener) {
        this.expandListener = expandListener;
    }

    @SuppressWarnings("unchecked")
    public void onExpandPressed(View view) {

        isExpanded.set(!isExpanded.get());

        if (expandListener != null) {
            expandListener.onItemSelected(view, this);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.donation_wallet_item;
    }
}
