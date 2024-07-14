package com.sauron.shadow.heists.ArtStation;

import com.pinecone.framework.util.json.JSONMaptron;
import com.sauron.radium.heistron.*;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONObject;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.io.File;
import java.util.Map;

public class ArtStationStalker extends HTTPCrew implements Stalker {
    protected int mutualID;

    protected String mszQueryCookie = "";

    public ArtStationStalker( HTTPIndexHeist heist, int id ){
        super( heist, id );
        this.mszQueryCookie = this.parentHeist().getConfig().optString( "QueryCookie" );
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
        JSONObject joSiteMaps = this.parentHeist().getConfig().optJSONObject( "SiteMaps" );
        this.mutualID = 1;

        for( Object ok : joSiteMaps.entrySet() ) {
            Map.Entry k = (Map.Entry) ok;
            this.stalk_sub_site_map( k.getKey().toString(), (JSONObject) k.getValue() );
        }
        //this.stalk_sub_site_map( "annotation", joSiteMaps.optJSONObject("annotation") );
    }

    protected void stalk_sub_site_map( String szSeg, JSONObject jo ) {
        String szIndexPath = this.parentHeist().getIndexPath();
        File fSegFileDir = new File( szIndexPath );
        fSegFileDir.mkdir();

        String szSegFile = szIndexPath + "/" + szSeg + ".xml";
        Page cachePage;
        String href = jo.optString( "href" );
        cachePage = this.queryHTTPPage( new Request(href).addHeader( "Cookie", this.mszQueryCookie ), szSegFile );

        Elements elements = cachePage.getHtml().getDocument().select( "loc" );

        JSONObject joIndexList = new JSONMaptron();
        for ( int i = 0; i < elements.size(); i++ ) {
            String szItemHref = elements.get(i).text();
            String[] debris   = szItemHref.split( this.heistURL + "/" );
            String szItemFN   = debris[1];
            String szSegment  = "artists";
            if( szItemFN.contains( "artists" ) ) {
                szSegment = "artists";
            }
            else if( szItemFN.contains( "artworks" ) ) {
                szSegment = "artworks";
            }
            else {
                continue;
            }
            joIndexList.affirmArray( szSegment ).put( szItemFN );

            String szLocalPath = szIndexPath + szItemFN;
            cachePage = this.queryHTTPPage( new Request(szItemHref).addHeader( "Cookie", this.mszQueryCookie ), szLocalPath );
            if( cachePage.getStatusCode() != 200 ) {
                this.logger.error( "<FetchIndexError:{}, {}, {}>", i, szItemHref, cachePage.getStatusCode() );
            }
            else {
                this.logger.info( "<FetchIndexDone:{}, {}, {}>", i, szItemHref, cachePage.getBytes().length );
            }
        }

        Debug.trace( joIndexList.size() );
    }

    protected void profileSiteMap() {
        this.stalk_inlet_index();
    }

    @Override
    public void toStalk() {
        this.profileSiteMap();
    }
}