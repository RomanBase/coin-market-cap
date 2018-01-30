package com.ankhrom.base.networking.volley;

import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.ankhrom.base.common.statics.StringHelper;

import java.util.Map;

public abstract class BaseVolleyRequest<T> extends Request<T> {

    private final ResponseListener<T> listener;
    private final String contentType;
    private final Map<String, String> header;
    private final Map<String, String> params;
    private final byte[] body;
    private final CacheType cacheType;

    private RequestQueue queue;

    public BaseVolleyRequest(int method, String url, String contentType, Map<String, String> header, Map<String, String> params, byte[] body, CacheType cache, ResponseListener<T> listener) {
        super(method, url, null);

        this.contentType = contentType;
        this.header = header;
        this.params = params;
        this.body = body;
        this.listener = listener;
        this.cacheType = cache;

        if (cache == CacheType.NONE) {
            setShouldCache(false);
        }
    }

    protected abstract T parseCache(Cache.Entry cache);

    @Override
    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    @Override
    public byte[] getPostBody() throws AuthFailureError {
        return getBody();
    }

    @Override
    protected Map<String, String> getPostParams() throws AuthFailureError {
        return getParams();
    }

    @Override
    public String getBodyContentType() {
        return !StringHelper.isEmpty(contentType) ? contentType : super.getBodyContentType();
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return header != null ? header : super.getHeaders();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return body != null ? body : super.getBody();
    }

    @Override
    protected void deliverResponse(T response) {

        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deliverError(VolleyError error) {

        if (listener == null) {
            return;
        }

        if (shouldCache()) {
            Cache.Entry cache = getCache(queue);

            if (cache != null) {

                if (listener instanceof ResponseCacheListener) {
                    if (((ResponseCacheListener) listener).onCacheResponse(cache, getCacheData(cache))) {
                        return;
                    }
                } else if (cacheType == CacheType.FORCE) {

                    T data = getCacheData(cache);

                    if (data != null) {
                        listener.onResponse(data);
                        return;
                    }
                }
            }
        }

        listener.onErrorResponse(error);
    }

    @Nullable
    public T getCacheData(RequestQueue queue) {

        return getCacheData(getCache(queue));
    }

    @Nullable
    public T getCacheData(@Nullable Cache.Entry entry) {

        T cache = null;

        try {
            if (entry != null && entry.data != null) {
                cache = parseCache(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cache;
    }

    public Cache.Entry getCache(RequestQueue queue) {

        return queue.getCache().get(getUrl());
    }

    public void cacheAndQueue(RequestQueue queue) {

        this.queue = queue;

        if (listener != null) {

            Cache.Entry cache = getCache(queue);

            if (cache != null) {
                if (listener instanceof ResponseCacheListener) {
                    ((ResponseCacheListener<T>) listener).onCacheResponse(cache, getCacheData(cache));
                } else {
                    listener.onResponse(getCacheData(cache));
                }
            }
        }

        queue.add(this);
    }

    public void queue(RequestQueue queue) {

        this.queue = queue;
        queue.add(this);
    }
}
