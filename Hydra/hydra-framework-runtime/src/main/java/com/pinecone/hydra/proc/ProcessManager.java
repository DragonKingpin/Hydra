package com.pinecone.hydra.proc;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.regime.Regiment;
import com.pinecone.framework.system.regime.arch.Manager;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.system.ko.CascadeKernelObjectInstrument;

public interface ProcessManager extends CascadeKernelObjectInstrument, Regiment, Manager {

    Processum superiorProcess();

    RuntimeSystem superiorSystem();

    void applyGuidAllocator( GuidAllocator guidAllocator );

    long     getVitalizeCount();

    long     getFatalityCount();

    // Object clearance rate, help load balance and dispatch. [e.g. Using priority queue.]
    default double getClearanceRate() {
        double nFatality = this.getFatalityCount();
        double nVitalize = this.getVitalizeCount();
        return nFatality / ( nVitalize + nFatality );
    }

    void erase( UProcess that );

    boolean autopsy( UProcess that );

}
