package com.ankhrom.coinmarketcap.viewmodel.dialog;

import android.view.View;

import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.databinding.QrScannerPageBinding;
import com.ankhrom.coinmarketcap.listener.OnQRHandledListener;
import com.ankhrom.coinmarketcap.model.dialog.QRModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by R' on 1/20/2018.
 */

public class QRViewModel extends AppViewModel<QrScannerPageBinding, QRModel> implements ZXingScannerView.ResultHandler {

    private boolean isStarted;

    private int requestCode;
    private OnQRHandledListener listener;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        Integer code = args.getArg(Integer.class);
        requestCode = code == null ? -1 : code;
        listener = args.getArg(OnQRHandledListener.class);

        setModel(new QRModel());

        model.showTooltip.set(true);

        if (requestCode < 0) {
            model.tooltip.set("convert wallet address or transaction ID to QR and then scan");
        } else {
            model.tooltip.set("convert text to QR code and then scan each field separately or at once with colon separator (key:secret)");
        }
    }

    public void setOnQRHandledListener(int requestCode, OnQRHandledListener listener) {

        this.requestCode = requestCode;
        this.listener = listener;
    }

    @Override
    protected void onCreateViewBinding(QrScannerPageBinding binding) {
        super.onCreateViewBinding(binding);

        startZXing();
    }

    public void onClosePressed(View view) {

        stopZXing();
        close();
    }

    private void startZXing() {

        if (isStarted || binding == null || binding.zxing == null) {
            return;
        }

        isStarted = true;
        binding.zxing.startCamera();
        binding.zxing.resumeCameraPreview(this);
    }

    private void stopZXing() {

        if (!isStarted || binding == null || binding.zxing == null) {
            return;
        }

        isStarted = false;
        binding.zxing.stopCamera();
    }

    private void close() {

        getNavigation().setPreviousViewModel();
        FragmentHelper.removePage(getContext(), this);
    }

    @Override
    public void handleResult(Result result) {

        String text = result.getText();

        if (StringHelper.isEmpty(text)) {
            return;
        }

        if (listener != null) {
            listener.onQRHandled(requestCode, text);
        }

        stopZXing();
        close();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.qr_scanner_page;
    }
}
