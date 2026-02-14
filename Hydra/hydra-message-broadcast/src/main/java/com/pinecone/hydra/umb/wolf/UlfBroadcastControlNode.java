package com.pinecone.hydra.umb.wolf;

import com.pinecone.hydra.umb.broadcast.BroadcastControlNode;
import com.pinecone.hydra.umb.broadcast.UMCBroadcastNode;
import com.pinecone.hydra.umct.UMCTExpress;
import com.pinecone.hydra.umct.husky.compiler.ProtoInterfacialCompiler;
import com.pinecone.hydra.umct.husky.machinery.PMCTContextMachinery;
import com.pinecone.hydra.umct.husky.machinery.RouteDispatcher;
import com.pinecone.ulf.util.protobuf.FieldProtobufDecoder;
import com.pinecone.ulf.util.protobuf.FieldProtobufEncoder;

public interface UlfBroadcastControlNode extends BroadcastControlNode {

    UMCTExpress createUlfExpress( String name ) ;

    RouteDispatcher createHuskyRoute() ;

    RouteDispatcher createHuskyRoute( UMCTExpress express ) ;

    @Override
    PMCTContextMachinery getMCTTransformer();

    @Override
    ProtoInterfacialCompiler getInterfacialCompiler();

    default FieldProtobufEncoder getFieldProtobufEncoder() {
        return this.getInterfacialCompiler().getCompilerEncoder().getEncoder();
    }

    FieldProtobufDecoder getFieldProtobufDecoder();


}
