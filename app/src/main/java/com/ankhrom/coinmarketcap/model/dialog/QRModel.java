package com.ankhrom.coinmarketcap.model.dialog;

import android.databinding.ObservableBoolean;

import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.coinmarketcap.model.base.AppModel;

/**
 * Created by R' on 2/1/2018.
 */

public class QRModel extends AppModel {

    public final ObservableBoolean showTooltip = new ObservableBoolean();
    public final ObservableString tooltip = new ObservableString();
}
