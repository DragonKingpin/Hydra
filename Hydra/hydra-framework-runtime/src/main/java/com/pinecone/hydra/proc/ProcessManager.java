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

}
