package com.ankhrom.base.networking.volley;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.android.volley.Request;
import com.ankhrom.base.common.statics.StringHelper;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestBuilder {

    private String url;
    private int method;
    private byte[] body;
    private String contentType;
    private Map<String, String> header;
    private Map<String, String> params;
    private ResponseListener listener;
    private CacheType cache;

    private RequestBuilder() {

        contentType = "application/json; charset=utf-8";

        header = new LinkedHashMap<>();
        header.put("accept", "*/*");

        cache = CacheType.FORCE;
    }

    public static RequestBuilder get(String url) {

        return request(Request.Method.GET, url);
    }

    public static RequestBuilder post(String url) {

        return request(Request.Method.POST, url);
    }

    public static RequestBuilder put(String url) {

        return request(Request.Method.PUT, url);
    }

    public static RequestBuilder request(int method, String url) {

        RequestBuilder builder = new RequestBuilder();

        builder.url = url;
        builder.method = method;

        return builder;
    }

    public RequestBuilder body(byte[] body) {

        this.body = body;
        return this;
    }

    public RequestBuilder body(String body) {

        this.body = body.getBytes();
        return this;
    }

    public RequestBuilder body(Object body) {

        this.body = new Gson().toJson(body).getBytes();
        return this;
    }

    public RequestBuilder contentType(String contentType) {

        this.contentType = contentType;
        return this;
    }

    public RequestBuilder header(String key, String value) {

        header.put(key, value);
        return this;
    }

    public RequestBuilder authBasic(String key, String secret) {

        return header("Authorization", "Basic " + Base64.encodeToString((key + ":" + secret).getBytes(), Base64.NO_WRAP));
    }

    public RequestBuilder param(String key, String value) {

        if (params == null) {
            params = new LinkedHashMap<>();
        }

        params.put(key, value);

        return this;
    }

    public RequestBuilder cache(CacheType cache) {

        this.cache = cache;
        return this;
    }

    public <T> RequestBuilder listener(final ResponseListener<T> listener) {

        this.listener = listener;
        return this;
    }

    public RequestBuilder queryUrl() {

        url = getUrl(true);
        params = null;

        return this;
    }

    public String getUrl() {

        return getUrl(false);
    }

    public String getUrl(boolean queryParams) {

        if ((modifyRequired() || queryParams) && params != null) {
            StringBuilder builder = new StringBuilder(url);
            builder.append(url.contains("?") ? "&" : "?");
            for (Map.Entry<String, String> param : params.entrySet()) {
                builder.append(param.getKey())
                        .append("=")
                        .append(param.getValue())
                        .append("&");
            }
            builder.deleteCharAt(builder.length() - 1);

            return builder.toString();
        }

        return url;
    }

    public Map<String, String> getParams() {

        return modifyRequired() ? null : params;
    }

    public String queryParams() {

        String url = getUrl();

        return (params == null || !url.contains("?")) ? StringHelper.EMPTY : url.substring(url.indexOf("?") + 1);
    }

    private boolean isForm() {

        return contentType.contains("www-form-urlencoded");
    }

    private boolean modifyRequired() {

        return method == Request.Method.GET && !isForm();
    }

    @SuppressWarnings("unchecked")
    public <T> BaseVolleyRequest<T> asGson(@NonNull Type type) {

        return new GsonRequest<T>(type, method, getUrl(), contentType, header, getParams(), body, cache, listener);
    }

    @SuppressWarnings("unchecked")
    public BaseVolleyRequest<String> asString() {

        return new StringRequest(method, getUrl(), contentType, header, getParams(), body, cache, listener);
    }

    @SuppressWarnings("unchecked")
    public BaseVolleyRequest<byte[]> asBytes() {

        return new ByteRequest(method, getUrl(), contentType, header, getParams(), body, cache, listener);
    }
}
