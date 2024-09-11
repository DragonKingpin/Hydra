package com.pinecone.hydra.config.distribute.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface ConfNodePathManipulator extends Pinenut {
    void insert(GUID guid,String path);

    void remove(GUID guid);

    String getPath(GUID guid);

    GUID getNode(String path);


}
