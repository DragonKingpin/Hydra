package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;

public interface DefaultMetaNodeManipulators extends Pinenut {
    ScopeTreeManipulator getScopeTreeManipulator() ;

    CommonDataManipulator getCommonDataManipulator();

    ApplicationNodeManipulator getApplicationNodeManipulator();

    ApplicationMetaManipulator getApplicationMetaManipulator();

    ServiceNodeManipulator getServiceNodeManipulator();

    ServiceMetaManipulator getServiceMetaManipulator();

    ClassifNodeManipulator getClassifNodeManipulator();

    ScopeTreeManipulator getServiceTreeMapper();
}
