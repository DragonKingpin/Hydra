package com.sauron.shadow.heists.Apesk;


import com.pinecone.framework.util.json.JSONObject;
import com.sauron.radium.heistron.*;

public class ApeskStalker extends HTTPCrew implements Stalker {
    protected int mutualID;

    protected String mszQueryCookie = "";

    protected JSONObject mjoConfig      ;

    public ApeskStalker( HTTPIndexHeist heist, int id ){
        super( heist, id );
        this.mjoConfig      = this.parentHeist().getConfig();
        this.mszQueryCookie = this.mjoConfig.optString( "QueryCookie" );
    }

    @Override
    protected void tryConsumeById( long index ) throws LootRecoveredException, LootAbortException, IllegalStateException {
//        try{
//            Debug.trace( new String( this.getHTTPFile( "https://rednest.cn" ).getBytes(), "UTF8" ) );
//        }
//        catch ( exception e ) {
//
//        }
    }


    @Override
    public void toStalk() {

    }
}