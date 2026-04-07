package com.pinecone.hydra.umct.husky.machinery;

import com.pinecone.hydra.umct.husky.compiler.ProtoInterfacialCompiler;
import com.pinecone.hydra.umct.mapping.ControllerInspector;
import com.pinecone.ulf.util.protobuf.FieldProtobufDecoder;
import com.pinecone.ulf.util.protobuf.FieldProtobufEncoder;

public class HuskyTransformer extends DigestTransformer implements PMCTTransformer {

    protected FieldProtobufEncoder         mFieldProtobufEncoder;

    protected FieldProtobufDecoder         mFieldProtobufDecoder;

    public HuskyTransformer( ProtoInterfacialCompiler compiler, ControllerInspector controllerInspector, FieldProtobufDecoder decoder ) {
        super( compiler, controllerInspector );

        this.mFieldProtobufEncoder  = compiler.getCompilerEncoder().getEncoder();
        this.mFieldProtobufDecoder  = decoder;
    }

    @Override
    public ProtoInterfacialCompiler getInterfacialCompiler() {
        return (ProtoInterfacialCompiler) super.getInterfacialCompiler();
    }

    @Override
    public FieldProtobufEncoder getFieldProtobufEncoder() {
        return this.mFieldProtobufEncoder;
    }

    @Override
    public FieldProtobufDecoder getFieldProtobufDecoder() {
        return this.mFieldProtobufDecoder;
    }

}
