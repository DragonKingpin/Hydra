package com.pinecone.hydra.umct.husky.machinery;

import com.pinecone.framework.util.lang.ScopedPackage;

public interface MCTContextMachinery extends MCTTransformer {

    MCTContextMachinery addScope           ( String szPackageName );

    MCTContextMachinery addScope           ( ScopedPackage scope );

    MultiMappingLoader    getMultiMappingLoader();

}
