package com.pinecone.hydra.storage.file.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.query.FileChildQuery;

public interface FileChildManipulator extends Pinenut {
    List<GUID> fetchChildren( FileChildQuery query );

    long countChildren( FileChildQuery query );

    List<GUID> fetchRootChildren( FileChildQuery query );

    long countRoot( FileChildQuery query );
}
