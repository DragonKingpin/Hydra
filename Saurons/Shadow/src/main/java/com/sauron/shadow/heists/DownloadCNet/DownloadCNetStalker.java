package com.sauron.shadow.heists.DownloadCNet;

import com.pinecone.framework.util.json.JSONMaptron;
import com.sauron.radium.heistron.*;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.io.FileUtils;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.rdb.MappedSQLSplicer;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DownloadCNetStalker extends HTTPCrew implements Stalker {
    protected int mutualID;

    public DownloadCNetStalker(HTTPIndexHeist heist, int id ){
        super( heist, id );
    }

    @Override
    protected void tryConsumeById( long id ) throws LootRecoveredException, LootAbortException, IllegalStateException {}

    protected void stalk_inlet_index() {
        JSONObject joSiteMaps = this.parentHeist().getConfig().optJSONObject( "SiteMaps" );
        this.mutualID = 1;
        this.stalk_sub_site_map( "products", joSiteMaps.optJSONObject("products") );
    }

    protected void stalk_sub_site_map( String szSeg, JSONObject jo ) {
        String szSegFileDir = this.parentHeist().getIndexPath() + szSeg;
        File fSegFileDir = new File( szSegFileDir );
        boolean mkdir = fSegFileDir.mkdir();

        String szSegFile = szSegFileDir + "\\" + szSeg + "_main.xml";
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
                    String szHref = subEles.get(j).text();
                    if( szHref.length() > 333 ) {
                        continue;
                    }

                    JSONObject thisSQLMap = new JSONMaptron();
                    thisSQLMap.put( "heist", this.crewName() );
                    thisSQLMap.put( "href", szHref );
                    thisSQLMap.put( "mutual_id", this.mutualID );
                    thisSQLMap.put( "topic", szSeg );
                    thisSQLMap.put( "topic_id", topicId );

                    sqlBuf.append( sqlSplicer.spliceInsertSQL( "nona_download_cnet_idx", thisSQLMap.getMap(), false ) );
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