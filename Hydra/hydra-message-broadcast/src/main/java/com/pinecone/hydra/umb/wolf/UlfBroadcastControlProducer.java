package com.pinecone.hydra.umb.wolf;

import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umct.husky.compiler.ProtoInterfacialCompiler;
import com.pinecone.hydra.umct.husky.machinery.PMCTContextMachinery;

public interface UlfBroadcastControlProducer extends BroadcastControlProducer {

    @Override
    PMCTContextMachinery getMCTTransformer();

    @Override
    ProtoInterfacialCompiler getInterfacialCompiler();

}
