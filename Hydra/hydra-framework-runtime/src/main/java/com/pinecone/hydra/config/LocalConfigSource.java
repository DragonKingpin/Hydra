package com.pinecone.hydra.config;

import com.pinecone.hydra.servgram.Servgram;
import com.pinecone.framework.system.ErrorStrings;
import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.util.config.PatriarchalConfig;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LocalConfigSource implements ConfigSource {
    protected PatriarchalConfig  mSearchScopeConf;
    protected List<String >      mPathScopes;
    protected List<String >      mFileExtends;
    protected Servgram           mParentGram;
    protected RuntimeSystem      mSystem;

    @SuppressWarnings("unchecked")
    public LocalConfigSource( Servgram gram, PatriarchalConfig setupScope, PatriarchalConfig searchScope ) {
        this.mParentGram        = gram;
        this.mSystem            = this.mParentGram.getSystem();
        this.mSearchScopeConf   = searchScope;

        Object t = setupScope.get( "PathScopes" );
        if( t instanceof List<?> ) {
            this.mPathScopes      = ( List<String > ) t;
        }
        else {
            this.mPathScopes      = new ArrayList<>();
        }

        t = setupScope.get( "FileExtends" );
        if( t instanceof List<?> ) {
            this.mFileExtends    = ( List<String > ) t;
        }
        else {
            this.mFileExtends    = new ArrayList<>();
        }
    }

    @Override
    public RuntimeSystem getSystem() {
        return this.mSystem;
    }

    @Override
    public PatriarchalConfig getSearchScopeConfig() {
        return this.mSearchScopeConf;
    }

    @Override
    public PatriarchalConfig loadConfig( URI path ) throws IOException {
        String szPath = path.getPath();
        Path lp = Path.of( szPath );
        if( lp.isAbsolute() ) {
            return this.getSearchScopeConfig().getChildFromPath( lp );
        }

        return this.loadConfig( lp );
    }

    @Override
    public PatriarchalConfig loadConfig( Object dyPath ) throws IOException {
        if( dyPath instanceof Path ) {
            return this.loadConfig( (Path) dyPath );
        }
        else if( dyPath instanceof URI ) {
            return this.loadConfig( (URI) dyPath );
        }
        else if( dyPath instanceof String ) {
            return this.loadConfig( Path.of( (String) dyPath ) );
        }

        return this.loadConfig( Path.of( dyPath.toString() ) );
    }

    public PatriarchalConfig loadConfig( Path path ) throws IOException {
        try{
            return this.getSearchScopeConfig().getChildFromPath( path );
        }
        catch ( IOException e ) {
            IOException ie = null;
            for( String sp : this.mPathScopes ) {
                try{
                    return this.getSearchScopeConfig().getChildFromPath( Path.of( sp ).resolve( path ) );
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
    public PatriarchalConfig loadConfigBySegmentName ( String szSegName ) throws IOException {
        IOException ie = null;
        for( String sfe : this.mFileExtends ) {
            try{
                return this.loadConfig( Path.of( szSegName + "." + sfe ) );
            }
            catch ( IOException e1 ) {
                ie = e1;
            }
        }

        throw new IOException( ErrorStrings.E_IRREDEEMABLE_NO_PATH_CONTEXT_MATCHED + "Segment-> '" + szSegName + "'", ie );
    }
}
