package com.sauron.radium.heistron;

import com.pinecone.framework.system.NonNull;
import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.config.JSONConfig;
import com.sauron.radium.heistron.mapreduce.SchemeQuerier;

public abstract class HTTPIndexHeist extends HTTPHeist {
    protected SchemeQuerier<Object > mSchemeQuerier;

    public HTTPIndexHeist( Heistgram heistron ){
        super(heistron);
    }

    public HTTPIndexHeist( Heistgram heistron, JSONConfig joConfig ){
        super( heistron, joConfig );
    }

    public HTTPIndexHeist( Heistgram heistron, @Nullable CascadeHeist parent, @NonNull String szChildName ) {
        super( heistron, parent, szChildName );
    }

    public SchemeQuerier<Object > getSchemeQuerier() {
        return this.mSchemeQuerier;
    }

    public String queryHrefById ( long id ) {
        Object scheme = this.mSchemeQuerier.get( id );

        if( scheme instanceof String ) {
            return (String) scheme;
        }

        return null; // TODO
    }

}
