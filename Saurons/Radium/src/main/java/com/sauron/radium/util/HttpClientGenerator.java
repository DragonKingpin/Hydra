package com.sauron.radium.util;

import com.sauron.radium.system.Saunut;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.CustomRedirectStrategy;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public interface HttpClientGenerator extends Saunut {
    GenericHttpClientGenerator setPoolSize( int poolSize ) ;

    int getPoolSize();

    CloseableHttpClient getClient( Site site );

    CloseableHttpClient getClient( Site site, boolean bPooled );

    void close();

    void clearPool();

    HttpClientConnectionManager getConnectionManager();
}
