package com.walnut.odin.formation.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.entity.GroupTaskEntry;

public interface GroupTaskManipulator extends Pinenut {
    int insert( GroupTaskEntry task );

    GroupTaskEntry selectByGuid( GUID guid );

    GroupTaskEntry selectByFormationGuidAndTaskGuid( GUID formationGuid, GUID taskGuid );

    long countEnabledByFormationGuid( GUID formationGuid );

    List<GroupTaskEntry> fetchEnabledByFormationGuid( GUID formationGuid );

    long countByFormationGuid( GUID formationGuid, Boolean enable );

    List<GroupTaskEntry> listByFormationGuid( GUID formationGuid, Boolean enable );

    int updateEnable( GUID guid, boolean enable );

    int removeByGuid( GUID guid );
}
