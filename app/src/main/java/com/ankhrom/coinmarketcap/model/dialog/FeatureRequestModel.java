package com.ankhrom.coinmarketcap.model.dialog;

import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.coinmarketcap.model.base.AppModel;

/**
 * Created by R' on 1/27/2018.
 */

public class FeatureRequestModel extends AppModel {

    public final EditTextObservable name = new EditTextObservable();
    public final EditTextObservable mail = new EditTextObservable();
    public final EditTextObservable donation = new EditTextObservable();
    public final EditTextObservable message = new EditTextObservable();

}
