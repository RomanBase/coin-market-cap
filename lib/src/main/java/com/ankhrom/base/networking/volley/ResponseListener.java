package com.ankhrom.base.networking.volley;


import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

public interface ResponseListener<T> {

    void onResponse(@Nullable T response);

    void onErrorResponse(VolleyError error);
}
