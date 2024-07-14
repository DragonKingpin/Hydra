package com.sauron.shadow.heists.PubChem;

import com.pinecone.framework.util.json.JSONMaptron;
import com.sauron.radium.heistron.*;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.io.FileUtils;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.rdb.MappedSQLSplicer;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class PubChemStalker extends HTTPCrew implements Stalker {
    protected int mutualID;

    public PubChemStalker ( HTTPIndexHeist heist, int id ){
        super( heist, id );
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
        String szSegFileDir = this.parentHeist().getIndexPath() + szSeg;
        File fSegFileDir = new File( szSegFileDir );
        fSegFileDir.mkdir();

        String szSegFile = szSegFileDir + "/" + szSeg + "_main.xml";
        Page cachePage;
        File fSegFile = new File( szSegFile );
        try {
            String href = jo.optString( "href" );
            if( !fSegFile.exists() ) {
                cachePage = this.getHTTPPage( href );
                FileWriter fw = new FileWriter( fSegFile );
                fw.write( cachePage.getRawText() );
                fw.close();
            }
            else {
                String cache = FileUtils.readAll( szSegFile );
                cachePage = this.parentHeist().extendPage( cache, new Request( href ) );
            }

            Elements elements = cachePage.getHtml().getDocument().select( "loc" );

            File fSQLIndex = new File( szSegFileDir + "/" + szSeg + ".sql" );
            FileWriter fSQL = new FileWriter( fSQLIndex );
            MappedSQLSplicer sqlSplicer = new MappedSQLSplicer();

            int topicId = 1;
            for ( int i = 0; i < elements.size(); i++ ) {
                String szFN = String.format( "%s/%s_%d.xml", szSegFileDir, szSeg, i );
                cachePage = this.getHTTPPage( elements.get(i).text(), szFN );
                Elements subEles = cachePage.getHtml().getDocument().select( "loc" );
                StringBuilder sqlBuf = new StringBuilder();
                for ( int j = 0; j < subEles.size(); j++ ) {
                    JSONObject thisSQLMap = new JSONMaptron();
                    thisSQLMap.put( "heist", this.crewName() );
                    thisSQLMap.put( "href", StringUtils.addSlashes( subEles.get(j).text() ) );
                    thisSQLMap.put( "mutual_id", this.mutualID );
                    thisSQLMap.put( "topic", szSeg );
                    thisSQLMap.put( "topic_id", topicId );

                    sqlBuf.append( sqlSplicer.spliceInsertSQL( "nona_pubchem_sitemap_idx", thisSQLMap.getMap(), false ) );
                    sqlBuf.append( ";\n" );
                    ++topicId;
                    ++this.mutualID;
                }
                fSQL.write( sqlBuf.toString() );


                Debug.trace( i );
            }
            fSQL.close();
        }
        catch ( IOException e ){
            e.printStackTrace();
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