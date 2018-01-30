package com.ankhrom.base.networking.volley;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class StringRequest extends BaseVolleyRequest<String> {

    public StringRequest(int method, String url, String contentType, Map<String, String> header, Map<String, String> params, byte[] body, CacheType cache, ResponseListener<String> listener) {
        super(method, url, contentType, header, params, body,cache, listener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        try {
            String string = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(string, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected String parseCache(Cache.Entry cache) {
        return new String(cache.data);
    }
}
