package com.walnut.odin.proc.server.transport.grpc;

import com.pinecone.framework.system.NotImplementedException;
import com.pinecone.framework.system.prototype.Pinenut;

public class GrpcRemoteProcessFrameMapper implements Pinenut {

    public Object toFrame( Object source ) {
        throw new NotImplementedException( "gRPC lifecycle frame mapper requires generated proto classes." );
    }

    public Object fromFrame( Object frame ) {
        throw new NotImplementedException( "gRPC lifecycle frame mapper requires generated proto classes." );
    }

}
