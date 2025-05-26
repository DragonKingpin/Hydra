package com.walnut.archcraft.ender.system;

import java.util.Map;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.config.StartupCommandParser;
import com.pinecone.hydra.proc.ArchUProcess;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.GenericClassImage;
import com.pinecone.hydra.proc.ns.GenericSegregationSpace;
import com.pinecone.hydra.proc.ns.ProcSpace;
import com.pinecone.hydra.system.component.LogStatuses;

public class Hydroxy extends ArchUProcess {

    public Hydroxy(
            HydraEmpire hostedSystem,
            UProcess parent, ExecutionImage image, ProcSpace procSpace,
            Map<String, String[]> startupArgs, Map<String, String[]> environmentVars
    ) {
        super( hostedSystem, parent, hostedSystem.processManager(), image, procSpace, startupArgs, environmentVars );

        this.revealNearestSystem().infoLifecycle(
                "HydraSystemProcess [UProcessProxy] [Name: `" + this.getName() + "`]",
                LogStatuses.StatusStandby
        );
        this.revealNearestSystem().infoLifecycle( "HydraSystemProcess Initialization", LogStatuses.StatusDone );
    }

    public Hydroxy( HydraEmpire hostedSystem ) {
        this(
                hostedSystem, null,
                new HydroxyImage( hostedSystem ), new GenericSegregationSpace(),
                hostedSystem.getStartupCommandMap(), Map.of()
        );

        Debug.greenfs( StartupCommandParser.DefaultParser.parse( System.getenv() ) );
    }

    @Override
    public RuntimeSystem parentSystem() {
        return super.parentSystem();
    }

    @Override
    public HydraEmpire revealNearestSystem() {
        return (HydraEmpire) super.revealNearestSystem();
    }
}
