package com.walnut.sparta.services.nodes;

import javax.annotation.Resource;

import com.pinecone.hydra.service.tree.source.ApplicationMetaManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulators;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.hydra.service.tree.source.ServiceMetaManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.summer.prototype.Component;

@Component
public class DefaultMetaNodeManipulatorsImpl implements DefaultMetaNodeManipulators {
    @Resource
    private ScopeTreeManipulator           scopeTreeManipulator;
    @Resource
    private CommonDataManipulator          commonDataManipulator;

    @Resource
    private ApplicationNodeManipulator     applicationNodeManipulator;
    @Resource
    private ApplicationMetaManipulator     applicationMetaManipulator;

    @Resource
    private ServiceNodeManipulator         serviceNodeManipulator;
    @Resource
    private ServiceMetaManipulator         serviceMetaManipulator;

    @Resource
    private ClassifNodeManipulator         classifNodeManipulator;



    @Resource
    public ScopeTreeManipulator getScopeTreeManipulator() {
        return this.scopeTreeManipulator;
    }

    @Resource
    public CommonDataManipulator getCommonDataManipulator() {
        return this.commonDataManipulator;
    }

    @Resource
    public ApplicationNodeManipulator getApplicationNodeManipulator() {
        return this.applicationNodeManipulator;
    }

    @Resource
    public ApplicationMetaManipulator getApplicationMetaManipulator() {
        return this.applicationMetaManipulator;
    }

    @Resource
    public ServiceNodeManipulator getServiceNodeManipulator() {
        return this.serviceNodeManipulator;
    }

    @Resource
    public ServiceMetaManipulator getServiceMetaManipulator() {
        return this.serviceMetaManipulator;
    }

    @Resource
    public ClassifNodeManipulator getClassifNodeManipulator() {
        return this.classifNodeManipulator;
    }

    @Resource
    public ScopeTreeManipulator getServiceTreeMapper() {
        return this.scopeTreeManipulator;
    }

}
