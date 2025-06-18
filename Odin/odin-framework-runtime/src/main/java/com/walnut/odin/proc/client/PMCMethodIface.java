package com.walnut.odin.proc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.odin.proc.RavenRemoteProcess;
import com.walnut.odin.proc.dto.UProcessDTO;

@Iface
public interface PMCMethodIface extends Pinenut {
    void test();

    void createProcess( long pmcId, UProcessDTO processDTO );
}
