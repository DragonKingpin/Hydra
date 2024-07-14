package com.pinecone.hydra.config;

import com.pinecone.framework.unit.MultiScopeMap;
import com.pinecone.framework.unit.PrecedeMultiMaptron;
import com.pinecone.framework.unit.PrecedeMultiScopeMap;
import com.pinecone.framework.unit.MultiScopeMaptron;
import com.pinecone.framework.util.template.TemplateParser;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.TreeMap;

public class ScopedMapConfigReinterpreter implements MapConfigReinterpreter {
    protected MultiScopeMap<String, Object >   mPrimaryScope;
    protected Set<String >                     mExcludeKeys;
    protected String                           mszKeyWordsToken;

    public ScopedMapConfigReinterpreter( MultiScopeMap<String, Object > scopeMap, String szKeyWordsToken ) {
        this.mPrimaryScope    = scopeMap;
        this.mExcludeKeys     = new TreeSet<>();
        this.mszKeyWordsToken = szKeyWordsToken;
    }

    public ScopedMapConfigReinterpreter( MultiScopeMap<String, Object > scopeMap ) {
        this( scopeMap, "KeyWords" );
    }


    @Override
    public MultiScopeMap<String, Object > getPrimaryScope() {
        return this.mPrimaryScope;
    }

    @Override
    public void setPrimaryScope( MultiScopeMap<String, Object> scope ) {
        this.mPrimaryScope = scope;
    }

    @Override
    public Collection<String > getExcludeKeys() {
        return this.mExcludeKeys;
    }

    @Override
    public void addExcludeKey( String szKey ) {
        this.mExcludeKeys.add( szKey );
    }

    @Override
    public void addExcludeKeys( Collection<String> keys ) {
        this.mExcludeKeys.addAll( keys );
    }

    @Override
    public void removeExcludeKey( String szKey ) {
        this.mExcludeKeys.remove( szKey );
    }

    @Override
    public String getKeyWordsToken(){
        return this.mszKeyWordsToken;
    }

    @Override
    public void setKeyWordsToken( String szToken ){
        this.mszKeyWordsToken = szToken;
    }


    @SuppressWarnings( "unchecked" )
    protected Object reinterpretVal  ( Object key, Object val, Map scope ) {
        if( val instanceof String ) {
            String szVal = (String) val;
            TemplateParser parser = new TemplateParser( szVal, scope );
            return parser.eval();
        }
        else if( val instanceof Map || val instanceof List) {
            Map    previousThisScope  = null;
            Object previousSupper     = null;
            Object previousThis       = null;
            MultiScopeMap kwFields    = null;
            if( scope instanceof MultiScopeMap ) { // Retrieving keyword fields
                MultiScopeMap ms   = (MultiScopeMap) scope;
                previousThisScope  = ((MultiScopeMap) scope).thisScope();
                if( ms instanceof PrecedeMultiScopeMap) {
                    kwFields       = ((PrecedeMultiScopeMap) ms).getPrecedeScope();
                }
                else {
                    kwFields       = ms.getScopeByNS( this.mszKeyWordsToken );
                }

                if( kwFields != null ) {
                    previousSupper     = kwFields.get( "super" );
                    previousThis       = kwFields.get( "this"  );

                    kwFields.put( "this" , val          );
                    kwFields.put( "super", previousThis );
                }
            }

            if( val instanceof Map ) {
                if( scope instanceof MultiScopeMap ) {
                    ( (MultiScopeMap) scope ).setThisScope( (Map)val      );
                }

                this.reinterpretObject( (Map<String, Object >)val, scope );
            }
            else {
                this.reinterpretList( (List<Object >)val, scope );
            }

            // Restoring previous scope.
            if( scope instanceof MultiScopeMap && kwFields != null ) {
                MultiScopeMap ms   = (MultiScopeMap) scope;
                kwFields.put( "super", previousSupper );
                kwFields.put( "this" , previousThis   );
                ms.setThisScope( previousThisScope );
            }
        }

        return null;
    }

    protected void reinterpretList   ( List<Object > that, Map scope ) {
        int idx = 0;
        for( Object val : that ) {
            Object nv = this.reinterpretVal( idx, val, scope );
            if( nv != null ) {
                that.set( idx, nv );
            }
            ++idx;
        }
    }

    protected void reinterpretObject ( Map<String, Object > that, Map scope ) {
        for( Map.Entry<String, Object > kv : that.entrySet() ) {
            if( this.mExcludeKeys.contains( kv.getKey() ) ) {
                continue;
            }
            Object nv = this.reinterpretVal( kv.getKey(), kv.getValue(), scope );
            if( nv != null ) {
                that.put( kv.getKey(), nv );
            }
        }
    }

    @Override
    public void reinterpret( Map<String, Object> that ) {
        this.reinterpret( that, this.mPrimaryScope );
    }

    @Override
    public void reinterpret( Map<String, Object> that, MultiScopeMap<String, Object> scope ) {
        this.reinterpretObject( that, scope );
    }

    @Override
    public void reinterpretByBasicKeyWordsScope( Map<String, Object> that, MultiScopeMap<String, Object> keyWordsScope ) {
        // Keyword fields, keyword has should the most highest priority.
        PrecedeMultiMaptron<String, Object > scope = new PrecedeMultiMaptron<>( that );
        //scope.addParent( keyWords.setName( "KeyWords" ) );
        scope.setPrecedeScope( keyWordsScope.setName( this.mszKeyWordsToken ) );
        scope.addParent( this.getPrimaryScope()                    );
        keyWordsScope.put( "__scope__" , (Object) scope            );

        this.reinterpretObject( that, scope );
    }

    @Override
    public void reinterpretByLineage( Map<String, Object> that, Object parent ) {
        MultiScopeMap<String, Object > keyWords = new MultiScopeMaptron<>( new TreeMap<>() );
        keyWords.put( "this"      , that      );
        keyWords.put( "super"     , parent    );

        this.reinterpretByBasicKeyWordsScope( that, keyWords );
    }
}
