package com.pinecone.hydra.system.ko.dao;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface GUIDNameManipulator extends Pinenut {
    List<GUID > getGuidsByName( String name );
}
