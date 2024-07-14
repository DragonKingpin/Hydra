package com.pinecone.slime.map;

import java.util.List;

public interface Querier<V > extends Mapper<V > {
    default List<? > query( Object statement ) {
        return List.of( this.get( statement ) );
    }

    default List<V > queryVal( Object statement ) {
        return List.of( this.get( statement ) );
    }

    default V queryValFirst( Object statement ) {
        List<V > l = this.queryVal( statement );

        if( l != null && !l.isEmpty() ) {
            return l.get( 0 );
        }
        return null;
    }

    @Override
    default boolean hasOwnProperty( Object elm ) {
        return this.containsKey( elm );
    }
}
