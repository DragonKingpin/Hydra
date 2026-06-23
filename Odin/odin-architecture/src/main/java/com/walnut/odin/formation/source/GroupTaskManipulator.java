package com.walnut.odin.formation.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.entity.GroupTaskEntry;

public interface GroupTaskManipulator extends Pinenut {
    int insert( GroupTaskEntry task );

    long countEnabledByFormationGuid( GUID formationGuid );

    List<GroupTaskEntry> fetchEnabledByFormationGuid( GUID formationGuid );
}
