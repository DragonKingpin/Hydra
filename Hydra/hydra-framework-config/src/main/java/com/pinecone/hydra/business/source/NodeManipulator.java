package com.pinecone.hydra.business.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.ElementNode;
import com.pinecone.hydra.business.entity.GenericElementNode;

public interface NodeManipulator extends Pinenut {

    void insert( ElementNode node );

    void remove( GUID guid );

    GenericElementNode get( GUID guid );

    void update( ElementNode node );

    List<GenericElementNode > fetchByType( String szType );

    List<GUID > getGuidsByName( String szName );

    List<GUID > queryGuidsByCode( String szCode );
}
