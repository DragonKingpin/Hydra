package com.sauron.shadow.heists.Apesk;


import com.sauron.radium.heistron.HTTPIndexHeist;
import com.sauron.radium.heistron.MegaDOMIndexCrew;
import com.sauron.radium.heistron.Reaver;
import org.jsoup.nodes.Element;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

public class ApeskReaver extends MegaDOMIndexCrew implements Reaver {
    public ApeskReaver( HTTPIndexHeist heist, int id ){
        super( heist, id );
    }

    protected Page compressSoloMBTIArchive( Page page, Request request ) {
        Element typeInfo  = page.getHtml().getDocument().selectFirst(".results .type-info");
        Element segRow    = page.getHtml().getDocument().selectFirst(".results .row");
        String szRawPage  = "";
        if( typeInfo != null ) {
            szRawPage += "<div class='type-info'>" + typeInfo.html() + "</div>\n";
        }
        else {
            this.logger.info("NoTypeInfo");
        }
        if( segRow != null ) {
            szRawPage += "<div class='row'>" + segRow.html() + "</div>\n";
        }
        else {
            this.logger.info("NoSegRow");
        }

        int id = request.getExtra("id");
        szRawPage += this.fetchCompressSoloMBTIArchiveExRawPage( id );
        return this.parentHeist().extendPage( szRawPage, page );
    }

    protected String fetchCompressSoloMBTIArchiveExRawPage( long id ) {
        String newUrl = this.heistURL + "/mbti/submit_email_date_cx_m.asp?code=223.73.241.5&user=" + id;
        Request request = new Request( newUrl );
        request.setCharset( "gb2312" );
        Page page = this.queryHTTPPageSafe( request );
        Element rawInfo  = page.getHtml().getDocument().selectFirst("table[align='center'][border='0']");

        if( rawInfo != null ) {
            return  "<div class='raw-info'>" + ( rawInfo.html() ) + "</div>\n";
        }
        else {
            this.logger.info("NoRawInfo");
        }

        return "";
    }

    @Override
    protected Page afterPageFetched( Page page, Request request ){
        return this.compressSoloMBTIArchive( page, request );
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
