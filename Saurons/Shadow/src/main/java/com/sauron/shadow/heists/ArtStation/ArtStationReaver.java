package com.sauron.shadow.heists.ArtStation;

import com.sauron.radium.heistron.*;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

public class ArtStationReaver extends MegaDOMIndexCrew implements Reaver {
    public ArtStationReaver( HTTPIndexHeist heist, int id ){
        super( heist, id );
    }

    @Override
    protected Page afterPageFetched( Page page, Request request ){
        Document document = page.getHtml().getDocument();
        String id  = document.select( "meta[name='ncbi_pubchem_cid']" ).attr( "content" );
        String seg = document.select( "meta[name='pubchem_uid_name']" ).attr( "content" );

        String newUrl = this.heistURL + "/rest/pug_view/data/"+ seg +"/" + id + "/JSON/";
        return this.queryHTTPPageSafe( ( new Request( newUrl ) ).putExtra( "id", id ) );
    }

    @Override
    public String querySpoilStoragePath( long id ) {
        return this.querySpoilStorageDir( id ) + "page_" + id + ".json";
    }


    @Override
    public void toRavage() {
        this.startBatchTask();
    }
}
