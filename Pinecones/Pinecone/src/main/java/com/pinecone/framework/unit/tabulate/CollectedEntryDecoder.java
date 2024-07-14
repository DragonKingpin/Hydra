package com.pinecone.framework.unit.tabulate;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.Collection;
import java.util.Map;

public interface CollectedEntryDecoder<V > extends Pinenut {
    Map<?, V > decode( Collection<Map.Entry<?, V > > collection ) ;

    Map<?, V > evolve( Map<?, V > regressed ) ;

    Class<? > getListClass();
    CollectedEntryDecoder setListClass( Class<?> listClass );

    Class<? > getMapClass();
    CollectedEntryDecoder setMapClass( Class<?> mapClass );
}
