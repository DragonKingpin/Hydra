package com.pinecone.hydra.system.ko.kom;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.entity.ReparseLinkNode;

public interface ReparseKOMTree extends KOMTreeInstrument {
    void newLinkTag( String originalPath, String dirPath, String tagName );

    void removeReparseLink( GUID guid );

    void affirmOwnedNode( GUID parentGuid, GUID childGuid );

    void newHardLink( GUID sourceGuid, GUID targetGuid );

    void newLinkTag( GUID originalGuid, GUID dirGuid, String tagName );

    void updateLinkTag( GUID tagGuid, String tagName );

    ReparseLinkNode queryReparseLinkByNS(String path, String szBadSep, String szTargetSep );

    /** ReparseLinkNode or GUID **/
    Object queryEntityHandleByNS( String path, String szBadSep, String szTargetSep );

    ReparseLinkNode queryReparseLink( String path );
}
