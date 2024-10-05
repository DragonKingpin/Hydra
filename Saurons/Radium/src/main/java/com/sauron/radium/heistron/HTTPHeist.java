package com.sauron.radium.heistron;

import com.pinecone.framework.system.NonNull;
import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.config.JSONConfig;
import com.sauron.radium.util.HttpBrowserConf;
import com.sauron.radium.util.HttpBrowserDownloader;
import com.pinecone.framework.util.json.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.PlainText;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class HTTPHeist extends Heist {
    protected final String           defUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.48";

    protected String                 heistURL;     // 爬虫的主链接

    protected Site                   site;
    protected HttpBrowserConf        browserConf;
    protected Spider                 majorSpider;
    protected CrewPageProcessor      pageProcessor;
    protected HttpBrowserDownloader  httpBrowser;
    protected ReentrantReadWriteLock requestLock = new ReentrantReadWriteLock();

    public HTTPHeist( Heistgram heistron ){
        super( heistron );
    }

    public HTTPHeist( Heistgram heistron, JSONConfig joConfig ){
        super( heistron, joConfig );
    }

    public HTTPHeist( Heistgram heistron, @Nullable CascadeHeist parent, @NonNull String szChildName ) {
        super( heistron, parent, szChildName );
    }

    protected void init() {
        this.site                 = Site.me().setRetryTimes( 3 );
        this.pageProcessor        = new CrewPageProcessor( this );
        this.majorSpider          = Spider.create( this.pageProcessor );
        this.httpBrowser          = new HttpBrowserDownloader();
    }

    protected void loadSiteConf() {
        if( this.getConfig() != null ) {
            if( this.browserConf.enableRandomDelay ) {
                this.site.setSleepTime( this.browserConf.randomDelayMin );
                this.site.setRetrySleepTime( this.browserConf.randomDelayMin );
            }
            else {
                this.site.setSleepTime( 0 );
                this.site.setRetrySleepTime( 100 );
            }

            this.site.setUserAgent( defUserAgent ) // TODO
                    .setCharset( this.browserConf.charset )
                    .setTimeOut( this.browserConf.socketTimeout );
        }
    }

    protected void loadProxyConf() {
        if( this.getConfig() != null ) {
            switch ( this.browserConf.proxyStrategy ) {
                case NoProxy: {
                    this.httpBrowser.setProxyProvider( null );
                    break;
                }
                case SystemOnly: {
                    this.httpBrowser.setProxyProvider(
                            SimpleProxyProvider.from( new Proxy(
                                    this.browserConf.systemProxy.optString("host"),
                                    this.browserConf.systemProxy.optInt("port")
                            ) )
                    );
                    break;
                }
                case ProxyGroup: {
                    List<Proxy> proxies = new ArrayList<>();
                    for ( int i = 0; i < this.browserConf.proxyGroup.size(); ++i ) {
                        JSONObject jo = this.browserConf.proxyGroup.optJSONObject( i );
                        proxies.add( new Proxy( jo.optString("host"), jo.optInt("port") ) );
                    }

                    this.httpBrowser.setProxyProvider(
                            SimpleProxyProvider.from( (Proxy[]) proxies.toArray() )
                    );
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    @Override
    protected void loadConfig() {
        this.init();
        super.loadConfig();
        this.browserConf = new HttpBrowserConf( this );
        this.loadSiteConf();
        this.loadProxyConf();
        this.heistURL = this.getConfig().optString( HeistConfigConstants.Heistum.KeyHeistURL );
    }

    public Site getSite() {
        return this.site;
    }

    public HttpBrowserConf getBrowserConf() {
        return this.browserConf;
    }

    public CrewPageProcessor getPageProcessor() {
        return this.pageProcessor;
    }

    public HttpBrowserDownloader getHttpBrowser() {
        return this.httpBrowser;
    }

    public Page queryHTTPPage( Request request ) {
        return this.queryHTTPPage( request, true );
    }

    protected Page queryHTTPPageOnly( Request request, boolean bPooled ) {
        this.requestLock.readLock().lock();
        try{
            return this.httpBrowser.download( request, this.majorSpider, bPooled );
        }
        finally {
            this.requestLock.readLock().unlock();
        }
    }

    public Page queryHTTPPage( Request request, boolean bPooled ) {
        try{
            return this.queryHTTPPageOnly( request, bPooled );
        }
        catch ( ProxyProvokeHandleException e ) {
            if( e.getCause() instanceof IOException ) {
                this.tracer().warn( "[queryHTTPPage:Warning] [What: IOException, " + e.getMessage() + "]" );
                // Fixed: CloseableHttpClient SSL exception using none pooled.
                try{
                    return this.queryHTTPPageOnly( request, bPooled );
                }
                catch ( ProxyProvokeHandleException e1 ) {
                    if ( e.getCause() instanceof IOException ) {
                        this.tracer().warn("[queryHTTPPage:Warning:ResetPool] [What: IOException, " + e.getMessage() + "]");
                        this.requestLock.writeLock().lock();
                        try{
                            this.httpBrowser.reset();
                        }
                        finally {
                            this.requestLock.writeLock().unlock();
                        }
                        return this.queryHTTPPageOnly( request, bPooled );
                    }
                    throw e1;
                }
            }
            throw e;
        }
    }

    public Page getHTTPPage( String szHref, boolean bPooled ) {
        Request request = new Request( szHref );
        request.putExtra("requestType", "HeistDefault");
        request.setMethod( "GET" );

        return this.queryHTTPPage( request, bPooled );
    }

    public Page getHTTPPage( String szHref ) {
        return this.getHTTPPage( szHref, true );
    }

    public String getHTTPFile( String szHref, boolean bPooled ) {
        return this.getHTTPPage( szHref, bPooled ).getHtml().toString();
    }

    public String getHTTPFile( String szHref ) {
        return this.getHTTPFile( szHref, true );
    }


    protected Page initDefaultPage( Page page, Request request ) {
        page.setStatusCode( 200 );
        page.setRequest   ( request );
        page.setCharset   ( this.getSite().getCharset() );
        page.setUrl       ( new PlainText( request.getUrl() ) );
        page.setDownloadSuccess( true );

        return page;
    }

    public Page extendPage( byte[] pageCache, Request request ) {
        Page page = new Page();
        page.setBytes     ( pageCache );
        try{
            page.setRawText( new String( pageCache, this.getSite().getCharset() ) );
        }
        catch ( UnsupportedEncodingException e1 ) {
            page.setRawText( null );
        }

        return this.initDefaultPage( page, request );
    }

    public Page extendPage( String szPageCache, Request request ) {
        Page page = new Page();
        page.setRawText   ( szPageCache );
        page.setBytes     ( szPageCache.getBytes() );

        return this.initDefaultPage( page, request );
    }

    public Page extendPage( String szNeoPageCache, Page that ) {
        Page neoPage = new Page();
        neoPage.setRawText   ( szNeoPageCache );
        neoPage.setBytes     ( szNeoPageCache.getBytes() );
        neoPage.setStatusCode( that.getStatusCode());
        neoPage.setRequest   ( that.getRequest());
        neoPage.setCharset   ( that.getCharset());
        neoPage.setUrl       ( that.getUrl());
        neoPage.setDownloadSuccess( that.isDownloadSuccess() );

        return neoPage;
    }
}
