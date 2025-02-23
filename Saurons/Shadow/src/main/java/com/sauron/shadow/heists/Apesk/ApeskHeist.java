package com.sauron.shadow.heists.Apesk;

import com.pinecone.ulf.rdb.mysql.MySQLExecutor;
import com.sauron.heist.heistron.Crew;
import com.sauron.heist.heistron.HTTPIndexHeist;
import com.pinecone.framework.util.config.JSONConfig;
import com.sauron.heist.heistron.Heistgram;

public class ApeskHeist extends HTTPIndexHeist {
    protected MySQLExecutor mysql;

    public ApeskHeist( Heistgram heistron ){
        super( heistron );
        this.init();
    }

    public ApeskHeist( Heistgram heistron, JSONConfig joConfig ){
        super( heistron, joConfig );
        this.init();
    }

    @Override
    protected void init() {
        super.init();

    }

    @Override
    public Crew newCrew( int nCrewId ) {
        return new ApeskReaver( this, nCrewId ) ;
    }

    @Override
    public String queryHrefById ( long id ) {
        return this.heistURL + this.getConfig().optString( "SubHref" ) + id;
    }

    @Override
    public void toRavage(){
        super.toRavage();
    }

    @Override
    public void toStalk(){
        ( new ApeskStalker( this, 0 ) ).toStalk();
    }

}
