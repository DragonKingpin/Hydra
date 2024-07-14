package com.sauron.shadow.heists.DownloadCNet;

import com.sauron.radium.heistron.HTTPIndexHeist;
import com.sauron.radium.heistron.MegaDOMIndexCrew;
import com.sauron.radium.heistron.Reaver;
import org.jsoup.nodes.Element;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

public class DownloadCNetReaver extends MegaDOMIndexCrew implements Reaver {

    public DownloadCNetReaver(HTTPIndexHeist heist, int id ){
        super( heist, id );
    }

    protected Page compressSoloArchive( Page page, Request request ) {
        Element infoJSON  = page.getHtml().getDocument().selectFirst("script[data-hid='ld+json']");
        Element mainPage  = page.getHtml().getDocument().selectFirst(".c-layoutDefault_page .c-scrollPercent");
        Element megaJSON  = page.getHtml().getDocument().selectFirst("body script:nth-child(2)");
        String szRawPage  = "";
        if( infoJSON != null ) {
            szRawPage += "<script id='baseInfoJson'>" + infoJSON.html() + "</script>\n";
        }
        else {
            this.logger.info("NoFirstJSON");
        }
        if( mainPage != null ) {
            szRawPage += "<div id='mainPage'>" + mainPage.html() + "</div>\n";
        }
        else {
            this.logger.info("NoMainPage");
        }
        if( megaJSON != null ) {
            szRawPage += "<script id='megaInfoJson'>" + megaJSON.html() + "</script>\n";
        }
        else {
            this.logger.info("NoMegaJSON");
        }

        return this.parentHeist().extendPage( szRawPage, page );
    }

    @Override
    protected Page afterPageFetched( Page page, Request request ){
        return this.compressSoloArchive( page, request );
    }

    @Override
    public String querySpoilStoragePath( long id ) {
        return this.querySpoilStorageDir( id ) + "page_" + id + ".html";
    }


    @Override
    public void toRavage() {
        this.startBatchTask();
    }


}
