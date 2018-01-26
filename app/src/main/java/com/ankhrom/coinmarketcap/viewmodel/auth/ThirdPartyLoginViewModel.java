package com.ankhrom.coinmarketcap.viewmodel.auth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.View;

import com.ankhrom.base.GlobalCode;
import com.ankhrom.base.common.BaseCamera;
import com.ankhrom.base.common.BasePermission;
import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.common.statics.ScreenHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.databinding.ThirdPartyLoginBinding;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.ankhrom.coinmarketcap.listener.OnQRHandledListener;
import com.ankhrom.coinmarketcap.model.auth.ThirdPartyLoginModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dialog.QRViewModel;

/**
 * Created by romanhornak on 1/4/18.
 */

public class ThirdPartyLoginViewModel extends AppViewModel<ThirdPartyLoginBinding, ThirdPartyLoginModel> implements OnQRHandledListener {

    private static final int QR_KEY = 1;
    private static final int QR_SECRET = 2;

    private ExchangeType type;
    private AuthCredentials credentials;

    private int qrField;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        type = args.getArg(ExchangeType.class, ExchangeType.HIT_BTC);
    }

    @Override
    public void onInit() {
        super.onInit();

        credentials = getExchangePrefs().getAuth(type);

        ThirdPartyLoginModel model = new ThirdPartyLoginModel(type);

        if (credentials.isValid()) {
            model.presetEdit(credentials.key, credentials.secret);
        }

        setModel(model);
    }

    @Override
    public void onQRHandled(int requestCode, String result) {

        result = result.replace(" ", "");

        if (result.contains(":")) {

            String[] data = result.split(":");
            model.key.setValue(data[0]);
            model.secret.setValue(data[1]);

            return;
        }

        switch (requestCode) {
            case QR_KEY:
                model.key.setValue(result);
                break;
            case QR_SECRET:
                model.secret.setValue(result);
                break;
        }
    }

    private boolean isCameraAvailable() {

        if (BaseCamera.isCameraAvailable(getContext())) {
            if (BasePermission.isAvailable(getContext(), Manifest.permission.CAMERA)) {
                return true;
            } else {
                BasePermission.with(getContext())
                        .requestCode(GlobalCode.CAMERA_REQUEST)
                        .require(Manifest.permission.CAMERA);
            }
        }

        return false;
    }

    private void openCamera(int qrField) {

        this.qrField = qrField;

        if (!isCameraAvailable()) {
            return;
        }

        addViewModel(QRViewModel.class, qrField, this);
    }

    public void onKeyCameraPressed(View view) {

        openCamera(QR_KEY);
    }

    public void onSecretCameraPressed(View view) {

        openCamera(QR_SECRET);
    }

    public void onLoginPressed(View view) {

        if (!(model.key.isValid() && model.secret.isValid())) {
            return;
        }

        boolean relogin = false;

        if (!model.key.get().equals(credentials.key)) {
            credentials.key = model.key.get();
            relogin = true;
        }

        if (!model.secret.get().equals(credentials.secret)) {
            credentials.secret = model.secret.get();
            relogin = true;
        }

        if (model.dontStore.get()) {
            relogin = true;
        }

        credentials.persist = !model.dontStore.get();

        if (relogin) {
            getExchangePrefs().setAuth(type, credentials);
        }

        close();
    }

    public void onCancelPressed(View view) {

        if (model.edit.get()) {
            getExchangePrefs().setAuth(type, null);
        }

        close();
    }

    public void onClosePressed(View view) {

        close();
    }

    private void close() {

        ScreenHelper.hideSoftKeyboard(getBaseActivity());

        FragmentHelper.removePage(getContext(), this);
        getNavigation().setPreviousViewModel();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == GlobalCode.CAMERA_REQUEST) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && qrField > 0) {
                openCamera(qrField);
            }
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.third_party_login;
    }
}
