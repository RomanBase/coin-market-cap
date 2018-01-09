package com.ankhrom.hitbtc;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.ankhrom.base.Base;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;

/**
 * Created by romanhornak on 1/9/18.
 */

public class HitApiTrading extends HitApi {

    public HitApiTrading(RequestQueue queue) {
        super(queue);
    }

    public void order() {

        RequestBuilder.get(HitApiUrl.BASE_URL + "history/trades")
                .authBasic(apiKey, apiSecret)
                .listener(new ResponseListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Base.log(response);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
                .asString()
                .queue(requestQueue);
    }
}
