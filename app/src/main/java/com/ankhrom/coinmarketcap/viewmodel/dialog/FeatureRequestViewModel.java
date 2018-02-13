package com.ankhrom.coinmarketcap.viewmodel.dialog;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.View;

import com.ankhrom.base.GlobalCode;
import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.common.statics.ScreenHelper;
import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.custom.builder.ToastBuilder;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.common.CameraRequest;
import com.ankhrom.coinmarketcap.databinding.FeatureRequestPageBinding;
import com.ankhrom.coinmarketcap.entity.FeatureRequest;
import com.ankhrom.coinmarketcap.listener.OnQRHandledListener;
import com.ankhrom.coinmarketcap.model.dialog.FeatureRequestModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by R' on 1/27/2018.
 */

public class FeatureRequestViewModel extends AppViewModel<FeatureRequestPageBinding, FeatureRequestModel> implements OnQRHandledListener {

    @Override
    public void loadModel() {

        setModel(new FeatureRequestModel());
    }

    public void onCameraPressed(View view) {

        if (!CameraRequest.isAvailable(getContext())) {
            return;
        }

        addViewModel(QRViewModel.class, this);
    }

    public void onSendPressed(View view) {

        if (StringHelper.isEmpty(model.message.get())) {
            return;
        }

        FeatureRequest data = new FeatureRequest();
        data.name = model.name.get();
        data.mail = model.mail.get();
        data.address = model.donation.get();
        data.message = model.message.get();
        data.timestamp = System.currentTimeMillis();

        FirebaseDatabase.getInstance()
                .getReference("feature_requests")
                .push()
                .setValue(data);

        ToastBuilder.with(getContext())
                .text("We received you message. Thanks.")
                .buildAndShow();

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == GlobalCode.CAMERA_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (getView() != null) {
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            onCameraPressed(null);
                        }
                    });
                }
            }
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.feature_request_page;
    }


}
