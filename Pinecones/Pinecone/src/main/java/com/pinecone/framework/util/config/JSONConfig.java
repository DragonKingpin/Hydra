package com.pinecone.framework.util.config;

import com.pinecone.framework.system.ErrorStrings;
import com.pinecone.framework.unit.MultiScopeMap;
import com.pinecone.framework.unit.MultiScopeMaptron;
import com.pinecone.framework.util.json.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class JSONConfig extends JSONMaptron implements Configson {
    protected MultiScopeMap<String, Object >     mScope;

    protected JPlusContext                       mThisContext;
    protected JSONConfig                         mRoot;
    protected JSONConfig                         mParent;


    public JSONConfig( JSONConfig parent ) {
        this( (Map<String, Object >) null, parent );
    }

    public JSONConfig( JSONObject thisScope, JSONConfig parent ) {
        this( thisScope.getMap(), parent );
    }

    public JSONConfig( JSONObject thisScope ) {
        this( thisScope.getMap(), null );
    }

    public JSONConfig( Map<String, Object > thisScope, JSONConfig parent ) {
        super();
        this.mParent      = parent;
        if( this.mParent != null ) {
            this.inherit( this.mParent );
        }
        else {
            this.mRoot        = this;
            this.mScope       = new MultiScopeMaptron<>();
            this.mThisContext = new JPlusContext();

            if( thisScope == null ) {
                thisScope = this.getMap();
            }
            this.mThisContext.asProgenitor( thisScope );
        }

        this.setThisScope( thisScope );
    }

    public JSONConfig() {
        this(null );
    }

    @Override
    public JSONConfig inherit( PatriarchalConfig parent ) {
        JSONConfig that = (JSONConfig) parent;
        this.mScope       = new MultiScopeMaptron<>();
        this.mThisContext = that.mThisContext.clone();
        this.mRoot        = that.mRoot;
        this.mParent      = that;

        this.mScope.setParents( that.mScope.getParents() );
        this.mScope.setName   ( that.mScope.getName() );
        this.mThisContext.setParent( that.mThisContext.thisScope() );
        this.setThisScope( this.getMap() );

        return this;
    }


    public JSONConfig addGlobalScope( Map<String, Object > scope ) {
        this.getContext().addGlobalScope( scope );
        if( scope instanceof MultiScopeMap ) {
            this.getScope().addParent( (MultiScopeMap<String, Object >)scope );
        }
        else {
            this.getScope().addParent( new MultiScopeMaptron<>( scope ) );
        }
        return this;
    }

    public JSONConfig addGlobalScope( Map<String, Object > scope, String name ) {
        this.getContext().addGlobalScope( scope );
        if( scope instanceof MultiScopeMap ) {
            this.getScope().addParent( ( (MultiScopeMap<String, Object >)scope ).setName( name ) );
        }
        else {
            this.getScope().addParent( ( new MultiScopeMaptron<>( scope ) ).setName( name ) );
        }
        return this;
    }

    public JSONConfig setThisScope( Map<String, Object > thisScope ) {
        if( thisScope != null ) {
            this.assimilate( thisScope );
            this.getContext().setThisScope( thisScope );
            this.getScope().setThisScope( thisScope );
        }
        return this;
    }

    public JSONConfig from( JSONObject prototype ) {
        this.setThisScope( prototype.getMap() );
        return this;
    }

    public JSONObject fromFile( File fConf ) throws IOException {
        if( this.parent() != null ) {
            return ( (JSONConfig) this.root() ).fromFile( fConf );
        }

        JPlusContext context = this.getContext().clone();
        context.asProgenitor( this );
        return new JSONMaptron( new JPlusCursorParser( new FileReader( fConf ), context ) );
    }

    public JSONObject fromFileNoException( File fConf ) {
        try {
            return this.fromFile( fConf );
        }
        catch ( IOException e ) {
            return null;
        }
    }

    public JSONObject fromPath( Path path ) throws IOException {
        try{
            return this.fromFile( path.toFile() );
        }
        catch ( IOException e ) {
            IOException ie = null;
            for( Path p : this.mThisContext.getParentPaths() ) {
                try{
                    return this.fromFile( path.resolve( p ).toFile() );
                }
                catch ( IOException e1 ) {
                    ie = e1;
                }
            }

            if( ie != null ) {
                throw new IOException( ErrorStrings.E_IRREDEEMABLE_NO_PATH_CONTEXT_MATCHED + "What-> '" + path + "'", ie );
            }
        }

        throw new IOException( ErrorStrings.E_IRREDEEMABLE_NO_PATH_CONTEXT_MATCHED + "What-> '" + path + "'" );
    }

    @Override
    public JSONConfig getChildFromPath( Path path ) throws IOException {
        JSONObject neo = this.fromPath( path );
        return new JSONConfig( neo, this );
    }

    @Override
    public Object get( Object key ) {
        return this.opt( key.toString() );
    }

    @Override
    public Object getOrDefault( Object key, Object def ) {
        Object o = this.get( key );
        if( o == null ) {
            return def;
        }
        return o;
    }

    @Override
    public Object opt( String key ) {
        return this.mScope.get( key );
    }

    @Override
    public JSONConfig getChild( Object key ) {
        JSONObject prototype = this.optJSONObject( key.toString() );
        if( prototype == null ) {
            return null;
        }

        return ( new JSONConfig( prototype, this ) );
    }

    public JSONConfig apply ( File fConf ) throws IOException {
        return this.from( this.fromFile( fConf ) );
    }

    @Override
    public JSONObject getProtoConfig() {
        return this.toJSONObject();
    }

    @Override
    public JSONConfig parent() {
        return this.mParent;
    }

    public MultiScopeMap<String, Object > getScope() {
        return this.mScope;
    }

    @Override
    public JSONConfig root() {
        return this.mRoot;
    }

    @Override
    public JSONConfig setParent ( Object parent ) {
        this.mParent = (JSONConfig) parent;
        return this;
    }


    public JSONConfig setRoot( Object root ) {
        this.mRoot = (JSONConfig) root;
        return this;
    }

    @Override
    public Path[] getParentPaths() {
        return this.getContext().getParentPaths();
    }

    @Override
    public JSONConfig setParentPaths( Path[] paths ) {
        this.getContext().setParentPaths( paths );
        return this;
    }

    public JSONConfig addParentPath( Path newPath ) {
        this.getContext().addParentPath( newPath );
        return this;
    }

    @Override
    public JPlusContext getContext() {
        return this.mThisContext;
    }

}

