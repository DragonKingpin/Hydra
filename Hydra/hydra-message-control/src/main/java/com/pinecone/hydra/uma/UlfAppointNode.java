package com.pinecone.hydra.uma;

import com.pinecone.hydra.umct.husky.compiler.ProtoInterfacialCompiler;
import com.pinecone.hydra.umct.husky.machinery.PMCTContextMachinery;
import com.pinecone.ulf.util.protobuf.FieldProtobufDecoder;
import com.pinecone.ulf.util.protobuf.FieldProtobufEncoder;

public interface UlfAppointNode extends AppointNode {

    @Override
    PMCTContextMachinery getMCTTransformer();

    @Override
    ProtoInterfacialCompiler getInterfacialCompiler();

    default FieldProtobufEncoder getFieldProtobufEncoder() {
        return this.getInterfacialCompiler().getCompilerEncoder().getEncoder();
    }

    FieldProtobufDecoder getFieldProtobufDecoder();

}
