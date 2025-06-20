package com.walnut.odin.proc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.walnut.odin.proc.RavenRemoteProcess;
import com.walnut.odin.proc.dto.UProcessDTO;

import java.util.Map;

public interface LocalProcessManagerClient extends Pinenut {

    UProcess createProcess( ExecutionImage image, UProcess parent, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars );

    void start(String processId);

    void stopProcess( GUID processId );

    void test();
}
