package com.pinecone.hydra.task.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.CommonMeta;
import com.pinecone.hydra.task.kom.entity.Namespace;

public interface CommonDataManipulator extends Pinenut {
    void insert( TaskFamilyNode node );

    void insertNS( Namespace node );

    void remove( GUID guid );

    CommonMeta getNodeCommonData(GUID guid );

    void update( TaskFamilyNode node );
}
