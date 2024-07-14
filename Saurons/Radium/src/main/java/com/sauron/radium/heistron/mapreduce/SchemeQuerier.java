package com.sauron.radium.heistron.mapreduce;

import com.pinecone.slime.map.AlterableQuerier;

public interface SchemeQuerier<V > extends AlterableQuerier<V > {
    default boolean hasOwnProperty( Object k ) {
        return this.containsKey( k );
    }
}
