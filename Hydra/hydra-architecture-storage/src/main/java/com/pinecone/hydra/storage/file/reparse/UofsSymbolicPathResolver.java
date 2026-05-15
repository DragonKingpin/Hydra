package com.pinecone.hydra.storage.file.reparse;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.function.Function;

public interface UofsSymbolicPathResolver extends Pinenut {
    UofsSymbolicResolveResult resolve( String path, Function<String, GUID> directPathResolver );
}
