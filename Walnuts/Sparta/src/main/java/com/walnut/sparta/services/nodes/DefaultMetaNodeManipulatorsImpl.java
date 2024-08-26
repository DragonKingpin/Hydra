package com.walnut.sparta.services.nodes;

import javax.annotation.Resource;

import com.pinecone.hydra.service.tree.source.ApplicationMetaManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifRulesManipulator;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulators;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.hydra.service.tree.source.ServiceMetaManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import org.springframework.stereotype.Component;


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
    private ClassifRulesManipulator        classifRulesManipulator;



    @Override
    public ScopeTreeManipulator getScopeTreeManipulator() {
        return this.scopeTreeManipulator;
    }

    @Override
    public CommonDataManipulator getCommonDataManipulator() {
        return this.commonDataManipulator;
    }

    @Override
    public ApplicationNodeManipulator getApplicationNodeManipulator() {
        return this.applicationNodeManipulator;
    }

    @Override
    public ApplicationMetaManipulator getApplicationMetaManipulator() {
        return this.applicationMetaManipulator;
    }

    @Override
    public ServiceNodeManipulator getServiceNodeManipulator() {
        return this.serviceNodeManipulator;
    }

    @Override
    public ServiceMetaManipulator getServiceMetaManipulator() {
        return this.serviceMetaManipulator;
    }

    @Override
    public ClassifNodeManipulator getClassifNodeManipulator() {
        return this.classifNodeManipulator;
    }

    @Override
    public ClassifRulesManipulator getClassifRulesManipulator() {
        return this.classifRulesManipulator;
    }

    @Override
    public ScopeTreeManipulator getServiceTreeMapper() {
        return this.scopeTreeManipulator;
    }



}