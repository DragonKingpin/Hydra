package com.pinecone.hydra.umb.wolf;

import com.pinecone.hydra.umb.broadcast.BroadcastControlAgent;
import com.pinecone.hydra.umct.husky.compiler.ProtoInterfacialCompiler;
import com.pinecone.hydra.umct.husky.machinery.PMCTContextMachinery;
import com.pinecone.ulf.util.protobuf.FieldProtobufDecoder;
import com.pinecone.ulf.util.protobuf.FieldProtobufEncoder;

public interface UlfBroadcastControlAgent extends BroadcastControlAgent {

    @Override
    PMCTContextMachinery getMCTTransformer();

    @Override
    ProtoInterfacialCompiler getInterfacialCompiler();

    default FieldProtobufEncoder getFieldProtobufEncoder() {
        return this.getInterfacialCompiler().getCompilerEncoder().getEncoder();
    }

    FieldProtobufDecoder getFieldProtobufDecoder();

}
