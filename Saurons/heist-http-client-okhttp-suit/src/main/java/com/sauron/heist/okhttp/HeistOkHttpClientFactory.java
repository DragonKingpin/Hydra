package com.sauron.heist.okhttp;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.pinecone.framework.util.json.JSONObject;
import com.sauron.heist.heistron.Heistum;
import com.sauron.heist.http.HttpBrowserConf;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

public class HeistOkHttpClientFactory implements OkHttpFactory {
    protected Heistum heistum;
    protected HttpBrowserConf conf;

    public HeistOkHttpClientFactory( Heistum heistum ) {
        this.heistum = heistum;
        this.conf = new HttpBrowserConf( this.heistum );
    }

    @Override
    public List<OkHttpClient> make() {
        return this.make( this.conf );
    }

    @Override
    public List<OkHttpClient> make( HttpBrowserConf conf ) {
        List<OkClientConstructionScheme> schemes = this.makeScheme( conf );
        List<OkHttpClient> clients = new ArrayList<>();
        for ( OkClientConstructionScheme scheme : schemes ) {
            OkHttpClient client = scheme.getBuilder().build();
            clients.add(client);
        }
        return clients;
    }

    @Override
    public List<OkClientConstructionScheme> makeScheme( HttpBrowserConf conf ) {
        List<OkClientConstructionScheme> schemes = new ArrayList<>();

        switch ( conf.proxyStrategy ) {
            case SystemOnly: {
                OkHttpClient.Builder builder = this.createBaseBuilder(conf);
                builder.proxySelector(ProxySelector.getDefault());

                OkClientConstructionScheme pair = new OkClientConstructionScheme(
                        builder, conf, conf.systemProxy, null
                );

                schemes.add(pair);
                break;
            }
            case ProxyGroup: {
                if ( conf.proxyGroup != null ) {
                    for ( int i = 0; i < conf.proxyGroup.length(); ++i ) {
                        JSONObject proxyConf = conf.proxyGroup.optJSONObject(i);
                        if (proxyConf == null) {
                            continue;
                        }

                        Proxy proxy = this.buildProxyFromConf(proxyConf);
                        if (proxy == null) {
                            continue;
                        }

                        OkHttpClient.Builder builder = this.createBaseBuilder(conf);
                        builder.proxy(proxy);

                        OkClientConstructionScheme pair = new OkClientConstructionScheme(
                                builder, conf, proxyConf, proxy
                        );

                        schemes.add(pair);
                    }
                }
                break;
            }
            case NoProxy:
            default: {
                OkHttpClient.Builder builder = this.createBaseBuilder(conf);
                builder.proxy(Proxy.NO_PROXY);

                OkClientConstructionScheme pair = new OkClientConstructionScheme(
                        builder, conf, null, Proxy.NO_PROXY
                );

                schemes.add(pair);
                break;
            }
        }

        return schemes;
    }

    protected OkHttpClient.Builder createBaseBuilder( HttpBrowserConf conf ) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(conf.connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(conf.readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(conf.writeTimeout, TimeUnit.MILLISECONDS)
                .followRedirects(conf.followRedirects)
                .followSslRedirects(conf.followSslRedirects)
                .retryOnConnectionFailure(conf.retryOnConnectionFailure);

        if ( conf.enableCookieJar ) {
            builder.cookieJar(new InMemoryCookieJar());
        }

        // === 连接池配置（可关闭，默认开启）===
        if ( conf.enableConnectionPool ) {
            ConnectionPool connectionPool = new ConnectionPool(
                    conf.maxIdleConnections,
                    conf.keepAliveSeconds,
                    TimeUnit.SECONDS
            );
            builder.connectionPool(connectionPool);
        }

        if ( conf.enableRequestLimit ) {
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(conf.maxRequests);
            dispatcher.setMaxRequestsPerHost(conf.maxRequestsPerHost);
            builder.dispatcher(dispatcher);
        }

        if ( conf.enableRandomDelay ) {
            builder.addInterceptor(chain -> {
                this.applyRandomDelay(conf);
                return chain.proceed(chain.request());
            });
        }

        return builder;
    }

    protected Proxy buildProxyFromConf( JSONObject proxyConf ) {
        String host = proxyConf.optString("host", null);
        int port = proxyConf.optInt("port", -1);

        if (host == null || port <= 0) {
            return null;
        }

        return new Proxy(
                Proxy.Type.HTTP,
                new InetSocketAddress(host, port)
        );
    }

    protected void applyRandomDelay( HttpBrowserConf conf ) {
        int min = conf.randomDelayMin;
        int max = conf.randomDelayMax;

        if (max <= min || min < 0) {
            return;
        }

        int delay = ThreadLocalRandom.current().nextInt(min, max + 1);
        try {
            Thread.sleep( delay );
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
