package com.ankhrom.coinmarketcap.viewmodel;

import com.android.volley.VolleyError;
import com.ankhrom.base.Base;
import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.interfaces.ObjectConverter;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.base.viewmodel.BaseViewModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.ApiParam;
import com.ankhrom.coinmarketcap.api.ApiUrl;
import com.ankhrom.coinmarketcap.api.CoinItem;
import com.ankhrom.coinmarketcap.model.CoinItemModel;
import com.ankhrom.coinmarketcap.model.DashboardModel;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by R' on 12/30/2017.
 */

public class DashboardViewModel extends BaseViewModel implements ResponseListener<List<CoinItem>> {

    @Override
    public void onInit() {
        super.onInit();

        requestData();
    }

    private void requestData() {

        UserPrefs prefs = getFactory().get(UserPrefs.class);

        if (prefs == null) {
            return;
        }

        RequestBuilder.get(ApiUrl.BASE_URL)
                .param(ApiParam.CURRENCY, prefs.getCurrency())
                .param(ApiParam.COUNT, ApiParam.COUNT_VALUE)
                .listener(this)
                .asGson(new TypeToken<List<CoinItem>>() {
                }.getType())
                .queue(getRequestQueue());
    }

    @Override
    public void onResponse(List<CoinItem> response) {

        if (response == null || response.isEmpty()) {
            onErrorResponse(new VolleyError());
            return;
        }

        setModel(new DashboardModel(getContext(), convertResponseItems(response)));
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Base.logE(error.getMessage());
    }

    private List<CoinItemModel> convertResponseItems(List<CoinItem> response) {

        return ObjectHelper.convert(response, new ObjectConverter<CoinItemModel, CoinItem>() {
            @Override
            public CoinItemModel convert(CoinItem object) {
                return new CoinItemModel(object);
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dashboard_page;
    }

    @Override
    public int getBindingResource() {
        return BR.VM;
    }
}
