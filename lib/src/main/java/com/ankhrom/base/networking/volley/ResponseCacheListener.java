package com.ankhrom.base.networking.volley;

import android.support.annotation.Nullable;

import com.android.volley.Cache;

/**
 * Created by R' on 1/30/2018.
 */

public interface ResponseCacheListener<T> extends ResponseListener<T> {

    boolean onCacheResponse(Cache.Entry cache, @Nullable T response);
}
