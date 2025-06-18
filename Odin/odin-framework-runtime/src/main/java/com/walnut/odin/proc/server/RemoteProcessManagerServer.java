package com.walnut.odin.proc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.proc.RavenRemoteProcess;
import com.walnut.odin.proc.dto.UProcessDTO;

import java.io.IOException;
import java.util.Map;

public interface RemoteProcessManagerServer extends Pinenut {
    void registerProcess( long pmcId, UProcessDTO processDTO );

    void start( GUID processId ) throws IOException;
}
