package com.pinecone.hydra.business.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.GenericProjectElement;
import com.pinecone.hydra.business.entity.ProjectElement;

public interface ProjectManipulator extends Pinenut {

    void insert( ProjectElement projectElement );

    void remove( GUID guid );

    GenericProjectElement get( GUID guid );

    void update( ProjectElement projectElement );

    List<GenericProjectElement > queryByProjectCode( String szProjectCode );
}
