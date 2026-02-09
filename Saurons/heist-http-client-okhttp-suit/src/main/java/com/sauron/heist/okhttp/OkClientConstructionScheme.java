package com.sauron.heist.okhttp;

import java.net.Proxy;

import com.pinecone.framework.util.json.JSONObject;
import com.sauron.heist.http.HttpBrowserConf;

import okhttp3.OkHttpClient;

public final class OkClientConstructionScheme {

    private OkHttpClient.Builder builder;
    private HttpBrowserConf httpBrowserConf;
    private JSONObject proxyConf;
    private Proxy proxy;

    public OkClientConstructionScheme(
            OkHttpClient.Builder builder,
            HttpBrowserConf httpBrowserConf,
            JSONObject proxyConf,
            Proxy proxy
    ) {
        this.builder = builder;
        this.httpBrowserConf = httpBrowserConf;
        this.proxyConf = proxyConf;
        this.proxy = proxy;
    }

    public OkHttpClient.Builder getBuilder() {
        return this.builder;
    }

    public void setBuilder(OkHttpClient.Builder builder) {
        this.builder = builder;
    }

    public HttpBrowserConf getHttpBrowserConf() {
        return this.httpBrowserConf;
    }

    public void setHttpBrowserConf(HttpBrowserConf httpBrowserConf) {
        this.httpBrowserConf = httpBrowserConf;
    }

    public JSONObject getProxyConf() {
        return this.proxyConf;
    }

    public void setProxyConf(JSONObject proxyConf) {
        this.proxyConf = proxyConf;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

}
