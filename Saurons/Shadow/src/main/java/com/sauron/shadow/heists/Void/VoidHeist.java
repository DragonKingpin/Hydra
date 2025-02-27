package com.sauron.shadow.heists.Void;

import com.pinecone.framework.system.NonNull;
import com.pinecone.framework.system.Nullable;
import com.sauron.heist.heistron.CascadeHeist;
import com.sauron.heist.heistron.Crew;
import com.sauron.heist.heistron.HTTPIndexHeist;
import com.sauron.heist.heistron.Heistgram;
import com.pinecone.framework.util.config.JSONConfig;

//@Heistlet( "Void" )
public class VoidHeist extends HTTPIndexHeist {
    public VoidHeist( Heistgram heistron ){
        super( heistron );
    }

    public VoidHeist( Heistgram heistron, JSONConfig joConfig ){
        super( heistron, joConfig );
    }

    public VoidHeist( Heistgram heistron, @Nullable CascadeHeist parent, @NonNull String szChildName ) {
        super( heistron, parent, szChildName );
    }

    @Override
    public Crew newCrew( int nCrewId ) {
        VoidReaver reaver = new VoidReaver( this, nCrewId );
        //this.heistPool.submit( reaver );
        return reaver;
    }

    @Override
    public void toRavage(){
        super.toRavage();
    }

    @Override
    public void toStalk(){

    }
}
