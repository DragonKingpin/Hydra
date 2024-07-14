package com.sauron.shadow.heists.DownloadCNet;

import com.pinecone.framework.util.config.JSONConfig;
import com.sauron.radium.heistron.Crew;
import com.sauron.radium.heistron.HTTPIndexHeist;
import com.sauron.radium.heistron.Heistotron;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONException;
import com.pinecone.framework.util.mysql.MySQLExecutor;
import com.pinecone.framework.util.mysql.MySQLHost;

import java.sql.SQLException;

public class DownloadCNetHeist extends HTTPIndexHeist {
    protected MySQLExecutor mysql;

    public DownloadCNetHeist(Heistotron heistron ){
        super( heistron );
        this.init();
    }

    public DownloadCNetHeist(Heistotron heistron, JSONConfig joConfig ){
        super( heistron, joConfig );
        this.init();
    }

    @Override
    protected void init() {
        super.init();
        try{
            this.mysql = new MySQLExecutor( new MySQLHost(
                    "node1.nutgit.com:13393/nonaron",
                    "root",
                    "root"
            ));
        }
        catch ( SQLException e ) {
            this.handleKillException( e );
        }
    }

    @Override
    public Crew newCrew( int nCrewId ) {
        return new DownloadCNetReaver( this, nCrewId );
    }

    @Override
    public String queryHrefById ( long id ) {
        try {
            JSONArray ja = this.mysql.fetch( "SELECT href FROM nona_download_cnet_idx WHERE mutual_id =" + id );
            return ja.getJSONObject( 0 ).getString( "href" );
        }
        catch ( SQLException | JSONException e ) {
            this.handleAliveException( e );
        }
        return "";
    }

    @Override
    public void toRavage(){
        super.toRavage();
    }

    @Override
    public void toStalk(){
        ( new DownloadCNetStalker( this, 0 ) ).toStalk();
    }
}
