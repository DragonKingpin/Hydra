package com.pinecone.framework.unit.tabulate;

import com.pinecone.framework.unit.Units;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;

public class GenericCollectedEntryDecoder<V > implements CollectedEntryDecoder<V > {
    protected String          mszSeparator;
    protected String          mszListType;
    protected String          mszSetType;
    protected String          mszTypeIndicator;
    protected Class<?>        mListClass;
    protected Class<?>        mMapClass;

    public GenericCollectedEntryDecoder( String separator, String typeIndicator, String listType, String setType, Class<?> listClass, Class<?> mapClass ) {
        this.mszSeparator     = separator;
        this.mszTypeIndicator = typeIndicator;
        this.mszListType      = typeIndicator + listType;
        this.mszSetType       = typeIndicator + setType;
        this.mListClass       = listClass;
        this.mMapClass        = mapClass;
    }

    public GenericCollectedEntryDecoder( String separator, String typeIndicator, String listType, String setType ) {
        this( separator, typeIndicator, listType, setType, ArrayList.class, LinkedHashMap.class );
    }

    public GenericCollectedEntryDecoder( String separator, String typeIndicator ) {
        this( separator, typeIndicator, "list", "set" );
    }

    public GenericCollectedEntryDecoder() {
        this( "::", "$" );
    }

    @Override
    public Class<? > getListClass() {
        return this.mListClass;
    }

    @Override
    public CollectedEntryDecoder setListClass( Class<?> listClass ) {
        this.mListClass = listClass;
        return this;
    }

    @Override
    public Class<? > getMapClass() {
        return this.mMapClass;
    }

    @Override
    public CollectedEntryDecoder setMapClass(Class<?> mapClass) {
        this.mMapClass = mapClass;
        return this;
    }

    protected Map<?, V > newMap() {
        return Units.newInstance( this.mMapClass );
    }

    protected List<V > newList() {
        return Units.newInstance( this.mListClass );
    }


    @Override
    public Map<?, V > decode( Collection<Map.Entry<?, V > > collection ) {
        Map<?, V > result = this.newMap();
        for ( Map.Entry<?, V> entry : collection ) {
            Object  key = entry.getKey();
            V     value = entry.getValue(); // Ignored parent-type case scenarios.
            this.addAndBootstrap( result, key, value );
        }
        return result;
    }

    @Override
    public Map<?, V > evolve( Map<?, V > regressed ) {
        Map<?, V > result = this.newMap();
        for ( Map.Entry<?, V> entry : regressed.entrySet() ) {
            Object  key = entry.getKey();
            V     value = entry.getValue(); // Ignored parent-type case scenarios.
            this.addAndBootstrap( result, key, value );
        }
        return result;
    }

    // Ignored parent-type case scenarios.
    // `V` is for unit`s elements type.
    @SuppressWarnings( "unchecked" )
    protected void addAndBootstrap( Map<?, V > result, Object key, V value ) {
        String szKey = key.toString();
        String[] debris = szKey.split( this.mszSeparator );
        Object current = result;
        for ( int i = 1; i < debris.length - 1; ++i ) {
            String part = debris[i];
            if ( part.endsWith( this.mszListType ) ) {
                part = part.substring( 0, part.length() - this.mszListType.length() );
                current = this.affirmListExists( current, part );
            }
            else if ( part.endsWith( this.mszSetType ) ) {
                part = part.substring( 0, part.length() - this.mszSetType.length() );
                current = this.affirmSetExists( current, part );
            }
            else {
                current = this.affirmMapExists( current, part );
            }
        }
        
        String lastPart = debris[ debris.length - 1 ];
        if ( lastPart.endsWith( this.mszListType ) ) {
            lastPart = lastPart.substring( 0, lastPart.length() - this.mszListType.length() );
            current = this.affirmListExists( current, lastPart );
            ((List) current).add( value );
        }
        else if ( lastPart.endsWith( this.mszSetType ) ) {
            lastPart = lastPart.substring( 0, lastPart.length() - this.mszSetType.length() );
            current = this.affirmSetExists( current, lastPart );
            ((Set) current).add( value );
        }
        else {
            if ( current instanceof Map ) {
                ((Map) current).put( lastPart, value );
            }
            else if ( current instanceof List ) {
                ((List) current).add( value );
            }
            else if ( current instanceof Set ) {
                ((Set) current).add( value );
            }
        }
    }

    protected Object affirmLastList( Collection collection, Object last ) {
        if ( collection.isEmpty() || !( last instanceof List ) ) {
            List neo = this.newList();
            collection.add( neo );
            return neo;
        }
        return last;
    }

    protected Object affirmListExists( Object current, String part ) {
        if ( current instanceof Map ) {
            Map map = (Map) current;
            if ( !map.containsKey(part) ) {
                List neo = this.newList();
                map.put( part, neo );
                return neo;
            }
            return map.get( part );
        }
        else if ( current instanceof List ) {
            List list = (List) current;
            return this.affirmLastList( list, list.get( list.size() - 1 ) );
        }
        else if ( current instanceof Set ) {
            Set set = (Set) current;
            return this.affirmLastList( set, this.setLastElement( set ) );
        }
        return null;
    }

    protected Object setLastElement( Set that ) {
        Object lastElement = null;
        Iterator iterator = that.iterator();
        while ( iterator.hasNext() ) {
            lastElement = iterator.next();
        }
        return lastElement;
    }

    // Set must be Linked.
    protected Object affirmLastSet( Collection collection, Object last ) {
        if ( collection.isEmpty() || !( last instanceof Set ) ) {
            Set newSet = new LinkedHashSet<>();
            collection.add( newSet );
            return newSet;
        }
        return last;
    }

    protected Object affirmSetExists( Object current, String part ) {
        if ( current instanceof Map ) {
            Map map = (Map) current;
            if ( !map.containsKey( part ) ) {
                Set neo = new LinkedHashSet<>();
                map.put( part, neo );
                return neo;
            }
            return map.get( part );
        }
        else if ( current instanceof List ) {
            List list = (List) current;
            return this.affirmLastSet( list, list.get( list.size() - 1 ) );
        }
        else if ( current instanceof Set ) {
            Set set = (Set) current;
            return this.affirmLastSet( set, this.setLastElement( set ) );
        }
        return null;
    }

    protected Object affirmLastMap( Collection collection, Object last ) {
        if ( collection.isEmpty() || !( last instanceof Map) ) {
            Map neo = this.newMap();
            collection.add( neo );
            return neo;
        }
        return last;
    }

    protected Object affirmMapExists( Object current, String part ) {
        if ( current instanceof Map ) {
            Map map = (Map) current;
            if ( !map.containsKey( part ) ) {
                Map neo = this.newMap();
                map.put( part, neo );
                return neo;
            }
            return map.get( part );
        }
        else if ( current instanceof List ) {
            List list = (List) current;
            return this.affirmLastMap( list, list.get( list.size() - 1 ) );
        }
        else if ( current instanceof Set ) {
            Set set = (Set) current;
            return this.affirmLastMap( set, this.setLastElement( set ) );
        }
        return null;
    }
}
