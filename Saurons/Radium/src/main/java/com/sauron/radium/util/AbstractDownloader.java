package com.sauron.radium.util;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.selector.Html;

public abstract class AbstractDownloader implements PageDownloader {
    public AbstractDownloader() {
    }

    public Html download(String url) {
        return this.download(url, (String)null);
    }

    public Html download(String url, String charset) {
        Page page = this.download(new Request(url), Site.me().setCharset(charset).toTask());
        return page.getHtml();
    }

    /** @deprecated */
    @Deprecated
    protected void onSuccess(Request request) {
    }

    protected void onSuccess( Request request, Task task ) {
        this.onSuccess(request);
    }

    /** @deprecated */
    @Deprecated
    protected void onError(Request request) {
    }

    protected void onError(Request request, Task task, Throwable e) {
        this.onError(request);
    }
}
