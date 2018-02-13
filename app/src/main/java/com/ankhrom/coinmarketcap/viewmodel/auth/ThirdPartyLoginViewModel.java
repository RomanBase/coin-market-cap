package com.ankhrom.coinmarketcap.viewmodel.auth;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.View;

import com.ankhrom.base.GlobalCode;
import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.common.statics.ScreenHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.common.CameraRequest;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.common.ExchangeTypeUtil;
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

    public static final int QR_KEY = 1;
    public static final int QR_SECRET = 2;
    public static final int QR_PASS = 3;

    private ExchangeType type;
    private AuthCredentials credentials;

    private int qrField;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        type = args.getArg(ExchangeType.class, ExchangeType.NONE);
    }

    @Override
    public void onInit() {
        super.onInit();

        credentials = getExchangePrefs().getAuth(type);

        ThirdPartyLoginModel model = new ThirdPartyLoginModel(type);

        if (credentials.isValid()) {
            model.presetEdit(credentials.key, credentials.secret, credentials.pass);
        }

        model.isPassRequired.set(ExchangeTypeUtil.isPassRequired(type));

        setModel(model);
    }

    @Override
    public void onQRHandled(int requestCode, String result) {

        result = result.replace(" ", "");

        if (result.contains(":")) {

            String[] data = result.split(":");

            model.key.setValue(data[0]);
            model.secret.setValue(data[1]);

            if (data.length > 2) {
                model.pass.setValue(data[2]);
            }

            return;
        }

        switch (requestCode) {
            case QR_KEY:
                model.key.setValue(result);
                break;
            case QR_SECRET:
                model.secret.setValue(result);
                break;
            case QR_PASS:
                model.pass.setValue(result);
                break;
        }
    }

    private void openCamera(int qrField) {

        this.qrField = qrField;

        if (!CameraRequest.isAvailable(getContext())) {
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

    public void onPassCameraPressed(View view) {

        openCamera(QR_PASS);
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

        if (model.isPassRequired.get()) {
            if (!model.pass.isValid()) {
                return;
            }

            if (!model.pass.get().equals(credentials.pass)) {
                credentials.pass = model.pass.get();
                relogin = true;
            }
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
                if (getView() != null) {
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            openCamera(qrField);
                        }
                    });
                }
            }
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.third_party_login;
    }
}
