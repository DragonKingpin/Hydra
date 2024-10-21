package com.pinecone.hydra.file.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.entity.ElementNode;
import com.pinecone.hydra.file.entity.LocalFrame;
import com.pinecone.hydra.file.entity.RemoteFrame;

import java.util.List;

public interface RemoteFrameManipulator extends Pinenut {
    RemoteFrame getRemoteFrame(GUID guid, ElementNode element);
    void insert( RemoteFrame remoteFrame );
    void remove( GUID guid );
    RemoteFrame getRemoteFrameByGuid(GUID guid);
    List< RemoteFrame > getRemoteFrameByFileGuid( GUID guid );
    RemoteFrame getLastFrame( GUID guid );
}