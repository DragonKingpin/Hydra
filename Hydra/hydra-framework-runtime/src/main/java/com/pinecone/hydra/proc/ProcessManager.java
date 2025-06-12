package com.pinecone.hydra.proc;

import java.util.Collection;
import java.util.Map;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.regime.Regiment;
import com.pinecone.framework.system.regime.arch.Manager;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.ImageLoader;
import com.pinecone.hydra.system.ko.CascadeKernelObjectInstrument;

public interface ProcessManager extends CascadeKernelObjectInstrument, Regiment, Manager {

    Processum superiorProcess();

    UProcess getRootUProcess();

    ImageLoader getImageLoader();

    void applyRootUProcess( UProcess rootUProcess );

    RuntimeSystem superiorSystem();

    void applyGuidAllocator( GuidAllocator guidAllocator );

    long     getVitalizeCount();

    long     getFatalityCount();

    long     processCount();

    Collection<UProcess> fetchProcesses();

    // Object clearance rate, help load balance and dispatch. [e.g. Using priority queue.]
    default double getClearanceRate() {
        double nFatality = this.getFatalityCount();
        double nVitalize = this.getVitalizeCount();
        return nFatality / ( nVitalize + nFatality );
    }

    void register( UProcess that );

    void erase( UProcess that );

    boolean autopsy( UProcess that );

    LocalUProcess createLocalHostedProcess(
            ExecutionImage image, UProcess parent, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars
    );

    default LocalUProcess createLocalHostedProcess(
            ExecutionImage image, UProcess parent, Map<String, String[]> startupArgs
    ) {
        return this.createLocalHostedProcess( image, parent, startupArgs, null );
    }

}
