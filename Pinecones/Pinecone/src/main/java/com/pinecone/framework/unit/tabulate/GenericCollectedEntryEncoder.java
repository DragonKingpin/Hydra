package com.pinecone.framework.unit.tabulate;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.unit.Units;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class GenericCollectedEntryEncoder<V > implements CollectedEntryEncoder<V > {
    protected FamilyIterator<V >                        mFamilyIterator;
    protected FamilyEntryNameEncoder                    mFamilyEntryNameEncoder;
    protected Class<Collection<Map.Entry<?, V > > >     mStereotypedClass;

    public GenericCollectedEntryEncoder( FamilyIterator<V >  iterator, FamilyEntryNameEncoder encoder, Class<Collection<Map.Entry<?, V > > > stereotypedClass ) {
        this.mFamilyIterator         = iterator;
        this.mFamilyEntryNameEncoder = encoder;
        this.mStereotypedClass       = stereotypedClass;
    }

    @SuppressWarnings( "unchecked" )
    public GenericCollectedEntryEncoder( FamilyIterator<V >  iterator, FamilyEntryNameEncoder encoder ) {
        this( iterator, encoder, (Class) ArrayList.class );
    }

    public GenericCollectedEntryEncoder( FamilyIterator<V >  iterator ) {
        this( iterator, new TypedNamespaceFamilyEntryNameEncoder( true ) );
    }

    @Override
    public Collection<Map.Entry<?, V > > encode() {
        Collection<Map.Entry<?, V > > collection;
        try{
            collection = Units.newInstance( this.mStereotypedClass );
        }
        catch ( IllegalArgumentException e ) {
            collection = new ArrayList<>();
        }

        while( this.mFamilyIterator.hasNext() ) {
            UnitFamilyNode<Object, V > node = this.mFamilyIterator.next();

            String k = this.mFamilyEntryNameEncoder.encode( node );
            collection.add( new KeyValue<>( k, node.getEntry().getValue() ));
        }

        return collection;
    }


    @Override
    public Map<?, V > regress( Class<? extends Map > stereotypedClass ) {
        Map<Object, V >  map;
        try{
            map = Units.newInstance( stereotypedClass );
        }
        catch ( IllegalArgumentException e ) {
            map = new LinkedHashMap<>();
        }

        while( this.mFamilyIterator.hasNext() ) {
            UnitFamilyNode<Object, V > node = this.mFamilyIterator.next();

            String k = this.mFamilyEntryNameEncoder.encode( node );
            map.put( k, node.getEntry().getValue() );
        }

        return map;
    }
}
