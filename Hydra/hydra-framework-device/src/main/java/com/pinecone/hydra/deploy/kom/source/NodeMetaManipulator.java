package com.pinecone.hydra.deploy.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployFamilyNode;
import com.pinecone.hydra.deploy.kom.entity.CommonMeta;
import com.pinecone.hydra.deploy.kom.entity.Namespace;

public interface NodeMetaManipulator extends Pinenut {

    void insert(DeployFamilyNode node);

    void insertNS(Namespace node);

    void remove(GUID guid);

    CommonMeta getNodeCommonMeta(GUID guid);

    void update(DeployFamilyNode node);

}
