package com.pinecone.hydra.proc;

import java.util.Map;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.ns.ProcSpace;

public class LocalHostedProcess extends ArchUProcess implements LocalUProcess {
    public LocalHostedProcess(
            @Nullable Processum localSystemProc, GUID guid, String szName,
            UProcess parent, ProcessManager processManager, ExecutionImage image, ProcSpace procSpace,
            Map<String, String[]> startupArgs, Map<String, String[]> environmentVars
    ) {
        super( localSystemProc, guid, szName, parent, processManager, image, procSpace, startupArgs, environmentVars );
    }

    public LocalHostedProcess(
            @Nullable Processum localSystemProc, String szName,
            UProcess parent, ProcessManager processManager, ExecutionImage image, ProcSpace procSpace,
            Map<String, String[]> startupArgs, Map<String, String[]> environmentVars
    ) {
        this( localSystemProc, processManager.getGuidAllocator().nextGUID(), szName, parent, processManager, image, procSpace, startupArgs, environmentVars );
    }


}
