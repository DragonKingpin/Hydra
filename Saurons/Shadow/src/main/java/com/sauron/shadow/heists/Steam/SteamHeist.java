package com.sauron.shadow.heists.Steam;

import com.pinecone.framework.util.config.JSONConfig;
import com.sauron.radium.heistron.*;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONException;
import com.pinecone.framework.util.mysql.MySQLExecutor;
import com.pinecone.framework.util.mysql.MySQLHost;

import java.sql.SQLException;

public class SteamHeist extends HTTPIndexHeist {
    protected MySQLExecutor mysql;

    public SteamHeist( Heistotron heistron ){
        super( heistron );
        this.init();
    }

    public SteamHeist(Heistotron heistron, JSONConfig joConfig ){
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
        return new SteamReaver( this, nCrewId );
    }

    protected String queryInletHref( long id ) {
        return this.heistURL + "/search/?ndl=1&ignore_preferences=1&page=" + id;
    }

    @Override
    public String queryHrefById( long id ) {
        if( this.getInstanceName().equals( "FetchInletList" ) ) {
            return this.queryInletHref( id );
        }
        else {
            try {
                JSONArray ja = this.mysql.fetch( "SELECT href FROM nona_steam_game_idx WHERE mutual_id =" + id );
                return ja.getJSONObject( 0 ).getString( "href" );
            }
            catch ( SQLException | JSONException e ) {
                this.handleAliveException( e );
            }
        }
        return "";
    }

    @Override
    public void toRavage(){
        super.toRavage();
    }

    @Override
    public void toStalk(){
        //( new SteamStalker( this ) ).toStalk();
    }
}
