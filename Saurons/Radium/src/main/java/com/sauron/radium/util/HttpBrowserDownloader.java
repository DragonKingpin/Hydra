package com.sauron.radium.util;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.Debug;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;

import us.codecraft.webmagic.downloader.HttpClientRequestContext;
import us.codecraft.webmagic.downloader.HttpUriRequestConverter;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.CharsetUtils;
import us.codecraft.webmagic.utils.HttpClientUtils;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class HttpBrowserDownloader extends AbstractDownloader {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final Map<String, CloseableHttpClient> httpClients = new HashMap<>();
    protected HttpClientGenerator httpClientGenerator = new GenericHttpClientGenerator();
    protected HttpUriRequestConverter httpUriRequestConverter = new HttpUriRequestConverter();
    protected ProxyProvider proxyProvider;
    protected boolean responseHeader = true;

    protected Task parentTask = null;

    public HttpBrowserDownloader() {
    }

    public HttpBrowserDownloader( Task task ) {
        this.parentTask = task;
    }

    public void setHttpUriRequestConverter( HttpUriRequestConverter httpUriRequestConverter) {
        this.httpUriRequestConverter = httpUriRequestConverter;
    }

    public void setProxyProvider( ProxyProvider proxyProvider ) {
        this.proxyProvider = proxyProvider;
    }

    protected CloseableHttpClient getHttpClient( Site site, boolean bPooled ) {
        if( !bPooled ) {
            // Explicit using false.
            return this.httpClientGenerator.getClient( site, false );
        }

        if ( site == null ) {
            return this.httpClientGenerator.getClient((Site)null);
        }
        else {
            String domain = site.getDomain();
            CloseableHttpClient httpClient = (CloseableHttpClient)this.httpClients.get(domain);
            if ( httpClient == null ) {
                synchronized(this) {
                    httpClient = (CloseableHttpClient)this.httpClients.get(domain);
                    if (httpClient == null) {
                        httpClient = this.httpClientGenerator.getClient(site);
                        this.httpClients.put(domain, httpClient);
                    }
                }
            }
            return httpClient;
        }
    }

    @Override
    public Page download( Request request, Task task ) {
        return this.download( request, task, true );
    }

    @Override
    public Page download( Request request, Task task, boolean bPooled ) {
        if ( task != null && task.getSite() != null ) {
            CloseableHttpResponse httpResponse = null;
            CloseableHttpClient httpClient = this.getHttpClient( task.getSite(), bPooled );
            Proxy proxy = this.proxyProvider != null ? this.proxyProvider.getProxy(task) : null;
            HttpClientRequestContext requestContext = this.httpUriRequestConverter.convert(request, task.getSite(), proxy);
            Page page = Page.fail();

            Page pa;
            try {
                httpResponse = httpClient.execute(requestContext.getHttpUriRequest(), requestContext.getHttpClientContext());
                page = this.handleResponse(request, request.getCharset() != null ? request.getCharset() : task.getSite().getCharset(), httpResponse, task);
                this.onSuccess(request, task);
                this.logger.info("downloading page success {}", request.getUrl());

                if( !bPooled ) {
                    try{
                        httpClient.close();
                    }
                    catch ( IOException e ) {
                        throw new ProxyProvokeHandleException( e );
                    }
                }
                return page;
            }
            catch ( SSLException sse ) {
                throw new ProxyProvokeHandleException( sse );
            }
            catch ( IOException e ) {
                this.onError( request, task, e );
                this.logger.info( "download page {} error", request.getUrl(), e );
                pa = page;
            }
            finally {
                if (httpResponse != null) {
                    EntityUtils.consumeQuietly(httpResponse.getEntity());
                }

                if ( this.proxyProvider != null && proxy != null ) {
                    this.proxyProvider.returnProxy(proxy, page, task);
                }
            }

            return pa;
        }
        else {
            throw new NullPointerException("task or site can not be null");
        }
    }

    @Override
    public void setThread( int threads ) {
        this.httpClientGenerator.setPoolSize( threads );
    }

    protected Page handleResponse(Request request, String charset, HttpResponse httpResponse, Task task) throws IOException {
        byte[] bytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
        String contentType = httpResponse.getEntity().getContentType() == null ? "" : httpResponse.getEntity().getContentType().getValue();
        Page page = new Page();
        page.setBytes(bytes);
        if (!request.isBinaryContent()) {
            if (charset == null) {
                charset = this.getHtmlCharset(contentType, bytes);
            }

            page.setCharset(charset);
            page.setRawText(new String(bytes, charset));
        }

        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        page.setDownloadSuccess(true);
        if (this.responseHeader) {
            page.setHeaders(HttpClientUtils.convertHeaders(httpResponse.getAllHeaders()));
        }

        return page;
    }

    private String getHtmlCharset(String contentType, byte[] contentBytes) throws IOException {
        String charset = CharsetUtils.detectCharset(contentType, contentBytes);
        if (charset == null) {
            charset = Charset.defaultCharset().name();
            this.logger.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
        }

        return charset;
    }

    public Logger getLogger() {
        return this.logger;
    }
}
