package com.pinecone.hydra.umct.husky.machinery;

import com.pinecone.framework.util.lang.ScopedPackage;

public interface PMCTContextMachinery extends PMCTTransformer, MCTContextMachinery {

    @Override
    PMCTContextMachinery addScope           ( String szPackageName );

    @Override
    PMCTContextMachinery addScope           ( ScopedPackage scope );

    @Override
    MultiMappingLoader    getMultiMappingLoader();

}
