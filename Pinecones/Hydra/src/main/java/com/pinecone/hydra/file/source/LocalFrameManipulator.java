package com.pinecone.hydra.file.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.entity.ElementNode;
import com.pinecone.hydra.file.entity.FolderMeta;
import com.pinecone.hydra.file.entity.LocalFrame;

import java.util.List;
import java.util.Map;

public interface LocalFrameManipulator extends Pinenut {
    LocalFrame getLocalFrame(GUID guid, ElementNode element);
    void insert( LocalFrame localFrame );
    void remove( GUID guid );
    LocalFrame getLocalFrameByGuid(GUID guid);
    List<LocalFrame> getLocalFrameByFileGuid( GUID guid );
}
