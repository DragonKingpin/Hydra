package com.pinecone.hydra.business.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.GenericIdeaElement;
import com.pinecone.hydra.business.entity.IdeaElement;

public interface IdeaManipulator extends Pinenut {

    void insert( IdeaElement ideaElement );

    void remove( GUID guid );

    GenericIdeaElement get( GUID guid );

    void update( IdeaElement ideaElement );

    List<GenericIdeaElement > queryByIdeaCode( String szIdeaCode );

    List<GenericIdeaElement > queryByRedMineGuid( GUID redMineGuid );
}
