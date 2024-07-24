package com.sauron.radium.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.CustomRedirectStrategy;


public class GenericHttpClientGenerator implements HttpClientGenerator {
    private transient Logger                           logger = LoggerFactory.getLogger( this.getClass() );
    private PoolingHttpClientConnectionManager         connectionManager;
    private final ReentrantReadWriteLock               generatorLock = new ReentrantReadWriteLock();
    private final Registry<ConnectionSocketFactory >   registry;

    public GenericHttpClientGenerator() {
        this.registry = RegistryBuilder.<ConnectionSocketFactory >create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", this.buildSSLConnectionSocketFactory()).build();
        this.initConnectionManager();
    }

    protected void initConnectionManager(){
        this.connectionManager = new PoolingHttpClientConnectionManager( this.registry );
        this.connectionManager.setDefaultMaxPerRoute(100);
        this.connectionManager.setValidateAfterInactivity(10000);
    }

    @Override
    public HttpClientConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

    private SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
        try {
            SSLContext sslContext = this.createIgnoreVerifySSL();
            String[] supportedProtocols;
            if (SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_11)) {
                supportedProtocols = new String[]{"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3"};
            } else {
                supportedProtocols = new String[]{"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"};
            }

            this.logger.debug("supportedProtocols: {}", String.join(", ", supportedProtocols));
            return new SSLConnectionSocketFactory(sslContext, supportedProtocols, (String[])null, new DefaultHostnameVerifier());
        }
        catch (KeyManagementException e) {
            this.logger.error("ssl connection fail", e);
        }
        catch (NoSuchAlgorithmException e2) {
            this.logger.error("ssl connection fail", e2);
        }

        return SSLConnectionSocketFactory.getSocketFactory();
    }

    private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init((KeyManager[])null, new TrustManager[]{trustManager}, (SecureRandom)null);
        return sc;
    }

    @Override
    public GenericHttpClientGenerator setPoolSize( int poolSize ) {
        this.connectionManager.setMaxTotal(poolSize);
        return this;
    }

    @Override
    public int getPoolSize(){
        return this.connectionManager.getMaxTotal();
    }

    @Override
    public CloseableHttpClient getClient( Site site ) {
        return this.generateClient( site, true );
    }

    @Override
    public CloseableHttpClient getClient( Site site, boolean bPooled ) {
        return this.generateClient( site, bPooled );
    }

    protected CloseableHttpClient generateClient( Site site, boolean bPooled ) {
        this.generatorLock.readLock().lock();
        try{
            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            if( bPooled ) {
                httpClientBuilder.setConnectionManager( this.connectionManager );
            }
            if ( site.getUserAgent() != null ) {
                httpClientBuilder.setUserAgent(site.getUserAgent());
            }
            else {
                httpClientBuilder.setUserAgent("");
            }

            if (site.isUseGzip()) {
                httpClientBuilder.addInterceptorFirst(new HttpRequestInterceptor() {
                    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
                        if (!request.containsHeader("Accept-Encoding")) {
                            request.addHeader("Accept-Encoding", "gzip");
                        }

                    }
                });
            }

            httpClientBuilder.setRedirectStrategy(new CustomRedirectStrategy());
            SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
            socketConfigBuilder.setSoKeepAlive(true).setTcpNoDelay(true);
            socketConfigBuilder.setSoTimeout(site.getTimeOut());

            if( bPooled ) {
                SocketConfig socketConfig = socketConfigBuilder.build();
                httpClientBuilder.setDefaultSocketConfig(socketConfig);
                this.connectionManager.setDefaultSocketConfig( socketConfig );
            }

            httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(site.getRetryTimes(), true));
            this.generateCookie(httpClientBuilder, site);
            return httpClientBuilder.build();
        }
        finally {
            this.generatorLock.readLock().unlock();
        }
    }

    private void generateCookie( HttpClientBuilder httpClientBuilder, Site site ) {
        if ( site.isDisableCookieManagement() ) {
            httpClientBuilder.disableCookieManagement();
        }
        else {
            CookieStore cookieStore = new BasicCookieStore();
            Iterator iterator = site.getCookies().entrySet().iterator();

            Map.Entry domainEntry;
            while( iterator.hasNext() ) {
                domainEntry = (Map.Entry)iterator.next();
                BasicClientCookie cookie = new BasicClientCookie((String)domainEntry.getKey(), (String)domainEntry.getValue());
                cookie.setDomain(site.getDomain());
                cookieStore.addCookie(cookie);
            }

            iterator = site.getAllCookies().entrySet().iterator();

            while( iterator.hasNext() ) {
                domainEntry = (Map.Entry)iterator.next();
                Iterator it = ((Map)domainEntry.getValue()).entrySet().iterator();

                while( it.hasNext() ) {
                    Map.Entry<String, String> cookieEntry = (Map.Entry)it.next();
                    BasicClientCookie cookie = new BasicClientCookie((String)cookieEntry.getKey(), (String)cookieEntry.getValue());
                    cookie.setDomain((String)domainEntry.getKey());
                    cookieStore.addCookie(cookie);
                }
            }

            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }
    }

    @Override
    public void close() {
        this.generatorLock.writeLock().lock();
        try{
            this.connectionManager.close();
        }
        finally {
            this.generatorLock.writeLock().unlock();
        }
    }

    @Override
    public void clearPool() {
        if( this.connectionManager != null ) {
            this.generatorLock.writeLock().lock();
            try{
                this.connectionManager.close();
                this.initConnectionManager();
            }
            finally {
                this.generatorLock.writeLock().unlock();
            }
        }
    }
}