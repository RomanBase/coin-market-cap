package com.ankhrom.coinmarketcap.viewmodel.dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.custom.builder.ToastBuilder;
import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.databinding.DonationPageBinding;
import com.ankhrom.coinmarketcap.model.dialog.DonationAdapterModel;
import com.ankhrom.coinmarketcap.model.dialog.DonationWalletItemModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 1/27/2018.
 */

public class DonationViewModel extends AppViewModel<DonationPageBinding, DonationAdapterModel> implements OnItemSelectedListener<DonationWalletItemModel> {

    private DonationWalletItemModel expandedItem;

    @Override
    public void loadModel() {

        List<DonationWalletItemModel> items = new ArrayList<>();

        items.add(new DonationWalletItemModel(this, onItemExpandListener, "BTC", getContext().getString(R.string.btc_wallet)));
        items.add(new DonationWalletItemModel(this, onItemExpandListener, "BCH", getContext().getString(R.string.bch_wallet)));
        items.add(new DonationWalletItemModel(this, onItemExpandListener, "ETH", getContext().getString(R.string.eth_wallet)));
        items.add(new DonationWalletItemModel(this, onItemExpandListener, "LTC", getContext().getString(R.string.ltc_wallet)));

        setModel(new DonationAdapterModel(getContext(), items));
    }

    public void onClosePressed(View view) {

        getNavigation().setPreviousViewModel();
        FragmentHelper.removePage(getContext(), this);
    }

    @Override
    public void onItemSelected(View view, DonationWalletItemModel model) {

        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getContext().getString(R.string.app_name), model.address);

        if (clipboard == null) {
            return;
        }

        clipboard.setPrimaryClip(clip);

        ToastBuilder.with(getContext())
                .text(model.currency + " address copied to clipboard")
                .buildAndShow();
    }

    private final OnItemSelectedListener<DonationWalletItemModel> onItemExpandListener = new OnItemSelectedListener<DonationWalletItemModel>() {
        @Override
        public void onItemSelected(View view, DonationWalletItemModel model) {

            if (model.isExpanded.get()) {

                if (expandedItem != null) {
                    expandedItem.isExpanded.set(false);
                }

                expandedItem = model;
            } else {
                expandedItem = null;
            }
        }
    };

    @Override
    public int getLayoutResource() {
        return R.layout.donation_page;
    }
}
