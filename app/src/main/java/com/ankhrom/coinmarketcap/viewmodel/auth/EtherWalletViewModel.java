package com.ankhrom.coinmarketcap.viewmodel.auth;

import android.content.pm.PackageManager;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.View;

import com.ankhrom.base.GlobalCode;
import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.common.statics.ScreenHelper;
import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.custom.listener.ImeActionDoneListener;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.common.CameraRequest;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.ankhrom.coinmarketcap.listener.OnQRHandledListener;
import com.ankhrom.coinmarketcap.model.auth.EtherContractItemModel;
import com.ankhrom.coinmarketcap.model.auth.EtherWalletLoginModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.ankhrom.coinmarketcap.viewmodel.dialog.QRViewModel;

/**
 * Created by R' on 6/8/2018.
 */
public class EtherWalletViewModel extends AppViewModel<ViewDataBinding, EtherWalletLoginModel> implements OnQRHandledListener {

    public static final int QR_ADDRESS = 10;
    public static final int QR_CONTRACT = 11;

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

        final EtherWalletLoginModel model = new EtherWalletLoginModel(type);

        if (credentials.isValid()) {
            model.edit.set(true);
            model.address.set(credentials.key);

            String[] contracts = splitContracts(credentials.pass);

            if (contracts != null) {
                for (String contract : contracts) {
                    model.contracts.add(new EtherContractItemModel(contract));
                }
            }
        }

        model.contract.setImeActionListener(new ImeActionDoneListener() {
            @Override
            protected void onDone(String value) {
                addContract(value);
            }
        });

        setModel(model);
    }

    @Override
    public void onQRHandled(int requestCode, String result) {

        result = result.replace(" ", "");

        if (result.contains(":")) {

            String[] data = result.split(":");
        }

        switch (requestCode) {
            case QR_ADDRESS:
                model.address.set(result);
                break;
            case QR_CONTRACT:
                addContract(result);
                break;
        }
    }

    private void addContract(String contract) {

        model.contract.set(null);

        EtherContractItemModel item = new EtherContractItemModel(contract);

        model.contracts.add(item);

        ScreenHelper.hideSoftKeyboard(getActivity());

        // TODO: 6/9/2018 check contract
    }

    private void openCamera(int qrField) {

        ScreenHelper.hideSoftKeyboard(getActivity());

        this.qrField = qrField;

        if (!CameraRequest.isAvailable(getContext())) {
            return;
        }

        addViewModel(QRViewModel.class, qrField, type, this);
    }

    public void onAddressCameraPressed(View view) {

        openCamera(QR_ADDRESS);
    }

    public void onContractCameraPressed(View view) {

        if (model.contractEdit.get()) {
            addContract(model.contract.get());
            return;
        }

        openCamera(QR_CONTRACT);
    }

    public void onLoginPressed(View view) {

        if (model.address.isEmpty()) {
            return;
        }

        credentials.key = model.address.get();
        credentials.secret = getString(R.string.etherscan_key);
        credentials.pass = mergeContracts();

        credentials.persist = !model.dontStore.get();

        getExchangePrefs().setAuth(type, credentials);

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

    private String mergeContracts() {

        int count = model.contracts.size();

        if (count == 0) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            builder.append(model.contracts.get(i).contract);
            builder.append(":");
        }

        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }

    private String[] splitContracts(String input) {

        if (StringHelper.isEmpty(input)) {
            return null;
        }

        if (!input.contains(":")) {
            return new String[]{input};
        }

        return input.split(":");
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
        return R.layout.ether_wallet;
    }
}
