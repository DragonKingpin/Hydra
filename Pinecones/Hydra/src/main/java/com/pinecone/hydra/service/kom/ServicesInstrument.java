package com.pinecone.hydra.service.kom;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.MetaNodeWideEntity;
import com.pinecone.hydra.service.kom.nodes.ServiceTreeNode;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;

public interface ServicesInstrument extends KOMInstrument {

    ServiceConfig KernelServiceConfig = new KernelServiceConfig();

}
