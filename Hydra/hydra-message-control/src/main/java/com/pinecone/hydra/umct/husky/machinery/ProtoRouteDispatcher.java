package com.pinecone.hydra.umct.husky.machinery;

import com.pinecone.hydra.umct.husky.compiler.ProtoInterfacialCompiler;

public interface ProtoRouteDispatcher extends RouteDispatcher {

    @Override
    PMCTContextMachinery getContextMachinery();

    @Override
    ProtoInterfacialCompiler getInterfacialCompiler();

}
