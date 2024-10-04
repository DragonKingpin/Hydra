package com.pinecone.hydra.registry;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;

public interface PathSelector extends RegistrySelector {
    GUID searchGUID( String[] parts );

    GUID searchGUID( String[] parts, @Nullable String[] lpResolvedPath );
}
