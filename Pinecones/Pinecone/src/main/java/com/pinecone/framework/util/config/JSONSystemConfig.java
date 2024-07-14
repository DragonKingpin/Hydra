package com.pinecone.framework.util.config;

import com.pinecone.framework.system.Pinecore;
import com.pinecone.framework.util.json.JSONObject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class JSONSystemConfig extends JSONConfig implements SysConfigson {
    protected Pinecore mSystem;

    public JSONSystemConfig ( Map<String, Object > map, JSONConfig parent, Pinecore system ) {
        super( map, parent );
        this.setSystem( system );
    }

    public JSONSystemConfig ( JSONConfig parent, Pinecore system ) {
        this( null, parent, system );
    }

    public JSONSystemConfig ( Pinecore system ) {
        this(null , system );
    }

    @Override
    public JSONSystemConfig getChild( Object key ) {
        JSONObject prototype = this.optJSONObject( key.toString() );
        if( prototype == null ) {
            return null;
        }

        return new JSONSystemConfig( prototype, this, this.getSystem() );
    }

    @Override
    public Pinecore getSystem() {
        return this.mSystem;
    }

    public JSONSystemConfig setSystem( Pinecore system ) {
        this.mSystem = system;
        if( this.parent() != null && ((JSONSystemConfig)this.parent() ).getSystem() != this.mSystem ) {
            this.getContext().addParentPath( Path.of( this.getSystem().getRuntimePath() ) );
        }
        return this;
    }

    @Override
    public JSONSystemConfig getChildFromPath( Path path ) throws IOException {
        JSONObject neo = this.fromPath( path );
        return new JSONSystemConfig( neo,this, this.getSystem() );
    }
}
