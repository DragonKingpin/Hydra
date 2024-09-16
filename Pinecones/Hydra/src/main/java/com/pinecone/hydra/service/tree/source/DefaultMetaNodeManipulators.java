package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TriePathManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;

public interface DefaultMetaNodeManipulators extends Pinenut {
    TrieTreeManipulator getTrieTreeManipulator() ;

    CommonDataManipulator getCommonDataManipulator();

    ApplicationNodeManipulator getApplicationNodeManipulator();

    ApplicationMetaManipulator getApplicationMetaManipulator();

    ServiceNodeManipulator getServiceNodeManipulator();

    ServiceMetaManipulator getServiceMetaManipulator();

    ClassifNodeManipulator getClassifNodeManipulator();

    ClassifRulesManipulator getClassifRulesManipulator();

    TrieTreeManipulator getServiceTreeMapper();

    ServiceFamilyTreeManipulator getServiceFamilyTreeManipulator();
    TireOwnerManipulator getTireOwnerManipulator();

    TriePathManipulator getTriePathManipulator();
}
