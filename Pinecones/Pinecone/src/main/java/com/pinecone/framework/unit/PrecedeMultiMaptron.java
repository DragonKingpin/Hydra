package com.pinecone.framework.unit;

import java.util.*;

public class PrecedeMultiMaptron<K, V > extends MultiScopeMaptron<K, V > implements PrecedeMultiScopeMap<K, V > {

    protected MultiScopeMap<K, V > mPrecedeScope;

    public PrecedeMultiMaptron() {
        this( true, null );
    }

    public PrecedeMultiMaptron( String name ) {
        this( true, null );
        this.setName( name );
    }

    public PrecedeMultiMaptron( Map<K, V > thisMap, List<MultiScopeMap<K, V > > prototypes, String name, MultiScopeMap<K, V > precedeScope ){
        super( thisMap, prototypes, name );
        this.mPrecedeScope = precedeScope;
        if( this.mPrecedeScope == null ) {
            this.mPrecedeScope = new MultiScopeMaptron<>();
        }
    }

    public PrecedeMultiMaptron( Map<K, V > thisMap, List<MultiScopeMap<K, V > > prototypes, String name, Map<K, V > precedeScope ){
        super( thisMap, prototypes, name );
        this.mPrecedeScope = new MultiScopeMaptron<>( precedeScope );
    }

    public PrecedeMultiMaptron( Map<K, V > thisMap, List<MultiScopeMap<K, V > > prototypes, String name ){
        this( thisMap, prototypes, name, (MultiScopeMap<K, V > ) null );
    }

    public PrecedeMultiMaptron( Map<K, V > thisMap, List<MultiScopeMap<K, V > > prototypes ){
        this( thisMap, prototypes, "" );
    }

    public PrecedeMultiMaptron( boolean bLinked, List<MultiScopeMap<K, V > > prototypes ){
        this( bLinked ? new LinkedHashMap<>() : new HashMap<>(), prototypes );
    }

    public PrecedeMultiMaptron( Map<K, V > thisMap ){
        this( thisMap, null );
    }

    @Override
    public MultiScopeMap<K, V> getPrecedeScope() {
        return this.mPrecedeScope;
    }

    @Override
    public MultiScopeMap<K, V > setPrecedeScope  ( MultiScopeMap<K, V > that ){
        this.mPrecedeScope = that;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ScopeMap<K, V >[]    scopes     (){
        ArrayList<ScopeMap<K, V > > l = new ArrayList<>();
        l.add( this.getPrecedeScope() );
        l.add( this );
        ScopeTrees.groupByNodes( this, l );
        return l.toArray( (ScopeMap<K, V >[]) new MultiScopeMap[0] );
    }


    @Override
    public boolean containsKey( Object key ) {
        boolean result = this.getPrecedeScope().containsKey( key );
        if( !result ) {
            result = super.containsKey( key );
        }

        return result;
    }

    @Override
    public boolean containsValue( Object value ) {
        boolean result = this.getPrecedeScope().containsValue( value );
        if( !result ) {
            result = super.containsKey( value );
        }

        return result;
    }

    @Override
    public V get( Object key ) {
        V val = this.getPrecedeScope().get( key );
        if( val == null ) {
            val = super.get( key );
        }

        return val;
    }

    @Override
    public MultiScopeMap<K, V >  removeAll  ( Object key ) {
        this.getPrecedeScope().removeAll( key );
        return super.removeAll( key );
    }

    @Override
    public void                  purge() {
        this.getPrecedeScope().clear();
        super.purge();
    }

    @Override
    public void                  depurate() {
        this.getPrecedeScope().clear();
        super.depurate();
    }

    @Override
    public void                  overrideTo ( Map<K, V > neo ) {
        Map<K, V > p = this.getPrecedeScope();
        for ( Map.Entry<? extends K, ? extends V> e : p.entrySet() ){
            neo.putIfAbsent( e.getKey(), e.getValue() );
        }

        super.overrideTo( neo );
    }

    @Override
    public void                  onlyOverrideFamilyTo ( Map<K, V > neo ) {
        super.overrideTo( neo );
    }

    @Override
    public boolean               isScopeEmpty () {
        boolean b = this.getPrecedeScope().isScopeEmpty();
        if( b ) {
            return super.isScopeEmpty();
        }

        return b;
    }

    @Override
    public MultiScopeMap<K, V >  getAll        ( Object key, List<V > ret ) {
        V v = this.getPrecedeScope().get( key );
        if( v != null ) {
            ret.add( v );
        }
        super.getAll( key, ret );

        return this;
    }

    @Override
    public V                     query         ( Object key, String szParentNS ) {
        V v = this.getPrecedeScope().query( key, szParentNS );
        if ( v != null ) {
            return v;
        }

        return super.query( key, szParentNS );
    }

    @Override
    public MultiScopeMap<K, V >  getScopeByNS  ( String szParentNS ) {
        MultiScopeMap<K, V > s = this.getPrecedeScope().getScopeByNS( szParentNS );
        if ( s != null ) {
            return s;
        }

        return super.getScopeByNS( szParentNS );
    }
}
