package com.sauron.heist.util;

import com.pinecone.radium.system.Saunut;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.*;

import us.codecraft.webmagic.Site;

public interface HttpClientGenerator extends Saunut {
    GenericHttpClientGenerator setPoolSize( int poolSize ) ;

    int getPoolSize();

    CloseableHttpClient getClient( Site site );

    CloseableHttpClient getClient( Site site, boolean bPooled );

    void close();

    void clearPool();

    HttpClientConnectionManager getConnectionManager();
}
