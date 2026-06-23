package com.walnut.odin.formation.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.entity.GroupEntry;

public interface GroupManipulator extends Pinenut {
    GroupEntry selectByGuid( GUID guid );

    GroupEntry selectByIdentifier( String identifier );

    int insert( GroupEntry group );
}
