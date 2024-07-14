package com.pinecone.framework.unit.distinct;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

public class GenericPrototypeDistinctAudit<E> extends GenericDistinctAudit<E > {
    protected Iterator<E> mMasterProtoIterator;

    public GenericPrototypeDistinctAudit( Iterator<E> masterProtoIterator, Collection<Iterator<E>> iterators, Collection<E> distinctions, DistinctType distinctType, Class<? > setType ) {
        super( iterators, distinctions, distinctType, setType );

        this.mMasterProtoIterator = masterProtoIterator;
        while ( this.mMasterProtoIterator.hasNext() ) {
            E elem = this.mMasterProtoIterator.next();
            this.mCommonElements.add( elem );
        }
    }

    public GenericPrototypeDistinctAudit( Iterator<E> masterProtoIterator, Collection<Iterator<E>> iterators, Collection<E> distinctions, DistinctType distinctType ) {
        this( masterProtoIterator, iterators, distinctions, distinctType, HashSet.class );
    }

    public GenericPrototypeDistinctAudit( Iterator<E> masterProtoIterator, Collection<Iterator<E>> iterators, DistinctType distinctType ) {
        this( masterProtoIterator, iterators, new ArrayList<>(), distinctType );
    }

    public GenericPrototypeDistinctAudit( Iterator<E> masterProtoIterator, Collection<Collection<E > > collections, DistinctType distinctType, Collection<E> distinctions, Class<? > setType ) {
        this( masterProtoIterator, DistinctAudit.toIterators(collections), distinctions, distinctType, setType );
    }

    public GenericPrototypeDistinctAudit( Iterator<E> masterProtoIterator, Collection<Collection<E > > collections, DistinctType distinctType, Collection<E> distinctions ) {
        this( masterProtoIterator, DistinctAudit.toIterators(collections), distinctions, distinctType );
    }

    public GenericPrototypeDistinctAudit( Iterator<E> masterProtoIterator, DistinctType distinctType, Collection<Collection<E > > collections ) {
        this( masterProtoIterator, collections, distinctType, new ArrayList<>() );
    }

}