package com.pinecone.hydra.proc;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.proc.entity.ProcessElement;
import com.pinecone.hydra.system.ko.entity.ObjectTable;

public interface UProcess extends Processum, ProcessElement {

    UProcess parentProcess();

    ObjectTable getObjectTable();

}
