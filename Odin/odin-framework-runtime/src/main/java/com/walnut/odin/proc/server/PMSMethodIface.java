package com.walnut.odin.proc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.umct.stereotype.Iface;

import java.util.Map;

@Iface
public interface PMSMethodIface extends Pinenut {
    void start( String processId );

    void createProcess(ExecutionImage image, UProcess parent, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars );
}
