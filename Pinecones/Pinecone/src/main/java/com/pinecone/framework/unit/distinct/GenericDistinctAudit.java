package com.pinecone.framework.unit.distinct;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.unit.Units;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;

public class GenericDistinctAudit<E> implements DistinctAudit<E> {
    protected Collection<Iterator<E > > mIterators;
    protected Collection<E>             mDistinctions;
    protected DistinctType              mDistinctType;
    protected Set<E>                    mCommonElements;
    protected Set<E>                    mDistinctElements;
    protected Collection<E >            mDuplicateElements;
    protected Class<? >                 mSetType;

    protected Set<E > newSet() {
        return Units.newInstance( this.mSetType );
    }

    public GenericDistinctAudit( Collection<Iterator<E>> iterators, Collection<E> distinctions, DistinctType distinctType, Class<? > setType ) {
        this.mIterators         = iterators;
        this.mDistinctions      = distinctions;
        this.mDistinctType      = distinctType;
        this.mSetType           = setType;
        this.mCommonElements    = this.newSet();
        this.mDistinctElements  = this.newSet();


        if( !( this.mDistinctions instanceof Set ) ) {
            this.mDuplicateElements = new ArrayList<>();
        }
    }

    public GenericDistinctAudit( Collection<Iterator<E>> iterators, Collection<E> distinctions, DistinctType distinctType ) {
        this( iterators, distinctions, distinctType, HashSet.class );
    }

    public GenericDistinctAudit( Collection<Iterator<E>> iterators, DistinctType distinctType ) {
        this( iterators, new ArrayList<>(), distinctType );
    }

    public GenericDistinctAudit( Collection<Collection<E > > collections, DistinctType distinctType, Collection<E> distinctions, Class<? > setType ) {
        this( DistinctAudit.toIterators(collections), distinctions, distinctType, setType );
    }

    public GenericDistinctAudit( Collection<Collection<E > > collections, DistinctType distinctType, Collection<E> distinctions ) {
        this( DistinctAudit.toIterators(collections), distinctions, distinctType );
    }

    public GenericDistinctAudit( DistinctType distinctType, Collection<Collection<E > > collections ) {
        this( collections, distinctType, new ArrayList<>() );
    }

    @Override
    public boolean hasOwnElement( E element ) {
        return this.mCommonElements.contains( element );
    }


    protected void addInnerSet( Iterator<E> iterator ) {
        Set<E > currentSet = this.newSet();
        while ( iterator.hasNext() ) {
            E elem = iterator.next();
            if( currentSet.contains( elem ) ) {
                if( this.mDuplicateElements != null ) {
                    this.mDuplicateElements.add( elem );
                }
                continue;
            }
            else {
                currentSet.add( elem );
            }

            if( this.mDistinctElements.contains( elem ) ) {
                this.mCommonElements.add( elem );
                this.mDistinctElements.remove( elem );
            }
            else if( !this.mCommonElements.contains( elem ) ){
                this.mDistinctElements.add( elem );
            }
//            else if( !this.mDistinctElements.contains( elem ) /*&& !this.mCommonElements.contains( elem )*/ ) {
//                this.mCommonElements.add( elem );
//            }
//            else {
//                this.mDistinctElements.add( elem );
//            }
        }
    }

    protected Collection<E> applyInnerSetToDistinctions() {
        if( this.mDuplicateElements == null ) {
            if ( this.mDistinctType == DistinctType.SymmetricDistinct ) {
                return this.mDistinctElements;
            }
            else if ( this.mDistinctType == DistinctType.SymmetricHomogeneity ) {
                return this.mCommonElements;
            }
        }
        else {
            if ( this.mDistinctType == DistinctType.SymmetricDistinct ) {
                this.mDistinctions.addAll( this.mDistinctElements );
            }
            else if ( this.mDistinctType == DistinctType.SymmetricHomogeneity ) {
                this.mDistinctions.addAll( this.mCommonElements );
            }

            for( E e : this.mDuplicateElements ) {
                if ( this.mDistinctElements.contains( e ) ) {
                    this.mDistinctions.add( e );
                }
                else if ( this.mCommonElements.contains( e ) ) {
                    this.mDistinctions.add( e );
                }
            }
        }

        return this.mDistinctions;
    }

    @Override
    public Collection<E> audit() {
        for ( Iterator<E> iterator : this.mIterators ) {
            this.addInnerSet( iterator );
        }

        return this.applyInnerSetToDistinctions();
    }

    @Override
    public Collection<E> audit( Iterator<E> neoIter, @Nullable Iterator<E> dummy ) {
        this.addInnerSet( neoIter );
        this.mDistinctions.clear();
        return this.applyInnerSetToDistinctions();
    }
}
