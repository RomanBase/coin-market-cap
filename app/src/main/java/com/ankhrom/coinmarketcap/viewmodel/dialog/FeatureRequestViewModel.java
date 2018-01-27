package com.ankhrom.coinmarketcap.viewmodel.dialog;

import android.view.View;

import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.common.statics.ScreenHelper;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.databinding.FeatureRequestPageBinding;
import com.ankhrom.coinmarketcap.listener.OnQRHandledListener;
import com.ankhrom.coinmarketcap.model.dialog.FeatureRequestModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

/**
 * Created by R' on 1/27/2018.
 */

public class FeatureRequestViewModel extends AppViewModel<FeatureRequestPageBinding, FeatureRequestModel> implements OnQRHandledListener {

    @Override
    public void loadModel() {

        setModel(new FeatureRequestModel());
    }

    public void onCameraPressed(View view) {

        addViewModel(QRViewModel.class, this);
    }

    public void onSendPressed(View view) {

        close();
    }

    public void onClosePressed(View view) {

        close();
    }

    private void close() {

        ScreenHelper.hideSoftKeyboard(getBaseActivity());

        getNavigation().setPreviousViewModel();
        FragmentHelper.removePage(getContext(), this);
    }

    @Override
    public void onQRHandled(int requestCode, String result) {

        model.donation.set(result);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.feature_request_page;
    }


}
