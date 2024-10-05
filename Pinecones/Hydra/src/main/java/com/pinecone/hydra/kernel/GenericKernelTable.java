package com.pinecone.hydra.kernel;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.kernel.entity.KernelObject;
import com.pinecone.hydra.kernel.entity.KernelObjectMeta;
import com.pinecone.hydra.kernel.source.KernelObjectManipulator;
import com.pinecone.hydra.kernel.source.KernelObjectMasterManipulator;
import com.pinecone.hydra.kernel.source.KernelObjectMetaManipulator;

import java.util.List;

public class GenericKernelTable implements KernelTable{
    private KernelObjectManipulator         kernelObjectManipulator;
    private KernelObjectMetaManipulator     kernelObjectMetaManipulator;
    private KernelObjectMasterManipulator   MasterManipulator;

    public GenericKernelTable(KernelObjectMasterManipulator masterManipulator){
        this.MasterManipulator              =   masterManipulator;
        this.kernelObjectManipulator        =   masterManipulator.getKernelObjectManipulator();
        this.kernelObjectMetaManipulator    =   masterManipulator.getKernelObjectMetaManipulator();
    }

    @Override
    public KernelObjectMeta getKOByGuid(GUID guid) {
        return this.kernelObjectMetaManipulator.get(guid);
    }

    @Override
    public List<KernelObject> ListKOByName(String name) {
        KernelObjectMeta komByName = this.kernelObjectMetaManipulator.getKOMByName(name);
        return this.ListKOByGuid(komByName.getGuid());
    }

    @Override
    public List<KernelObject> ListKOByGuid(GUID guid) {
        return this.kernelObjectManipulator.getAll(guid);
    }
}
