package com.walnut.archcraft.ender.system;

import java.util.Map;

import com.pinecone.hydra.proc.ArchUProcess;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.ns.ProcSpace;

public class Hydroxy extends ArchUProcess {

    public Hydroxy(
            HydraEmpire hostedSystem,
            UProcess parent, ExecutionImage image, ProcSpace procSpace,
            Map<String, String[]> startupArgs, Map<String, String[]> environmentVars
    ) {
        super( hostedSystem, parent, hostedSystem.processManager(), image, procSpace, startupArgs, environmentVars );
    }

}
