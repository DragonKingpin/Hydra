package com.sauron.shadow.heists.DeviantArt;

import com.sauron.radium.heistron.*;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.io.FileUtils;
import com.pinecone.framework.util.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.io.IOException;

public class DeviantArtStalker extends HTTPCrew implements Stalker {
    protected int mutualID;

    protected String mszQueryCookie = "";

    protected JSONObject mjoConfig      ;

    public DeviantArtStalker( HTTPIndexHeist heist, int id ){
        super( heist, id );
        this.mjoConfig      = this.parentHeist().getConfig();
        this.mszQueryCookie = this.mjoConfig.optString( "QueryCookie" );
    }

    @Override
    protected void tryConsumeById( long id ) throws LootRecoveredException, LootAbortException, IllegalStateException {
//        try{
//            Debug.trace( new String( this.getHTTPFile( "https://rednest.cn" ).getBytes(), "UTF8" ) );
//        }
//        catch ( exception e ) {
//
//        }
    }

    protected void stalk_inlet_index() {
        String szIndexPath   = this.parentHeist().getIndexPath();
        String szGZIndexPath = szIndexPath + "/RawGZ/";
        String szInletSMLocal = szIndexPath + this.mjoConfig.optString( "InletSitemap" );
        try{
            Page page = this.parentHeist().extendPage( FileUtils.readAll( szInletSMLocal ), new Request("") );
            Elements elements = page.getHtml().getDocument().select( "loc" );
            String szMajorHref = this.heistURL + "/sitemaps/";

            for ( int i = 0; i < elements.size(); i++ ) {
                Element loc = elements.get(i);
                String szLocHref = loc.text();
                String[] debris = szLocHref.split( szMajorHref );
                String szGZFN = debris[1];

                String szGZLocalPath = szGZIndexPath + szGZFN;
                this.queryHTTPPage( new Request( szLocHref ), szGZLocalPath );

                Debug.trace( szGZLocalPath, i );
            }
        }
        catch ( IOException e ) {
            this.handleKillException( e );
        }
    }

    protected void profileSiteMap() {
        this.stalk_inlet_index();
    }

    @Override
    public void toStalk() {
        this.profileSiteMap();
    }
}