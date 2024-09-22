package com.sauron.radium.util;

import com.sauron.radium.system.Saunut;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;

public interface PageDownloader extends Downloader, Saunut {
    @Override
    Page download( Request request, Task task );

    Page download( Request request, Task task, boolean bPooled );

    @Override
    void setThread( int threads );

    void reset();
}
