package com.ankhrom.coinmarketcap.model.base;

import com.ankhrom.base.model.Model;
import com.ankhrom.coinmarketcap.BR;

/**
 * Created by R' on 1/27/2018.
 */

public class AppModel extends Model {

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
