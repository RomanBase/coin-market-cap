package com.ankhrom.coinmarketcap.viewmodel.auth;

import android.view.View;

import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.common.statics.ScreenHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.databinding.ThirdPartyLoginBinding;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.ankhrom.coinmarketcap.model.auth.ThirdPartyLoginModel;
import com.ankhrom.coinmarketcap.prefs.ExchangePrefs;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

/**
 * Created by romanhornak on 1/4/18.
 */

public class ThirdPartyLoginViewModel extends AppViewModel<ThirdPartyLoginBinding, ThirdPartyLoginModel> {

    private ExchangeType type;
    private AuthCredentials credentials;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        type = args.getArg(ExchangeType.class, ExchangeType.HIT_BTC);
    }

    @Override
    public void onInit() {
        super.onInit();

        ThirdPartyLoginModel model = new ThirdPartyLoginModel();

        credentials = getFactory().get(ExchangePrefs.class).getAuth(type);

        if (credentials.isValid()) {
            model.presetEdit(credentials.key, credentials.secret);
        }

        setModel(model);
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

        if (relogin) {
            getFactory().get(ExchangePrefs.class).setAuth(type, credentials);
        }

        close();
    }

    public void onCancelPressed(View view) {

        if (model.edit.get()) {
            getFactory().get(ExchangePrefs.class).setAuth(type, null);
        }

        close();
    }

    private void close() {

        ScreenHelper.hideSoftKeyboard(getBaseActivity());

        FragmentHelper.removePage(getContext(), this);
        getNavigation().setPreviousViewModel();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.third_party_login;
    }
}
