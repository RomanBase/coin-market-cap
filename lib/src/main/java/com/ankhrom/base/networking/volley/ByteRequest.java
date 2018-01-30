package com.ankhrom.base.networking.volley;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

public class ByteRequest extends BaseVolleyRequest<byte[]> {

    public ByteRequest(int method, String url, String contentType, Map<String, String> header, Map<String, String> params, byte[] body, CacheType cache, ResponseListener<byte[]> listener) {
        super(method, url, contentType, header, params, body, cache, listener);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected byte[] parseCache(Cache.Entry cache) {
        return cache.data;
    }
}
