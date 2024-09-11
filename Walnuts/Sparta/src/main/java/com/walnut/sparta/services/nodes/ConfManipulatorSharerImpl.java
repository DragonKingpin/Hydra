package com.walnut.sparta.services.nodes;

import com.pinecone.hydra.config.distribute.source.ConfManipulatorSharer;
import com.pinecone.hydra.config.distribute.source.ConfNodeManipulator;
import com.pinecone.hydra.config.distribute.source.ConfNodeMetaManipulator;
import com.pinecone.hydra.config.distribute.source.ConfNodeOwnerManipulator;
import com.pinecone.hydra.config.distribute.source.ConfNodePathManipulator;
import com.pinecone.hydra.config.distribute.source.ConfTreeManipulator;
import com.pinecone.hydra.config.distribute.source.NamespaceNodeManipulator;
import com.pinecone.hydra.config.distribute.source.NamespaceNodeMetaManipulator;
import com.pinecone.hydra.config.distribute.source.NodeCommonDataManipulator;
import com.pinecone.hydra.config.distribute.source.PropertiesManipulator;
import com.pinecone.hydra.config.distribute.source.TextValueManipulator;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
@Component
public class ConfManipulatorSharerImpl implements ConfManipulatorSharer {
    @Resource
    ConfNodeManipulator             confNodeManipulator;

    @Resource
    NamespaceNodeManipulator        namespaceNodeManipulator;

    @Resource
    PropertiesManipulator           propertiesManipulator;

    @Resource
    TextValueManipulator            textValueManipulator;

    @Resource
    ConfTreeManipulator             confTreeManipulator;

    @Resource
    ConfNodeOwnerManipulator        confNodeOwnerManipulator;

    @Resource
    ConfNodePathManipulator         confNodePathManipulator;

    @Resource
    ConfNodeMetaManipulator         confNodeMetaManipulator;

    @Resource
    NamespaceNodeMetaManipulator    namespaceNodeMetaManipulator;

    @Resource
    NodeCommonDataManipulator       nodeCommonDataManipulator;
    @Override
    public ConfNodeManipulator getConfigurationManipulator() {
        return this.confNodeManipulator;
    }

    @Override
    public NamespaceNodeManipulator getNamespaceManipulator() {
        return this.namespaceNodeManipulator;
    }

    @Override
    public PropertiesManipulator getPropertiesManipulator() {
        return this.propertiesManipulator;
    }

    @Override
    public TextValueManipulator getTextValueManipulator() {
        return this.textValueManipulator;
    }

    @Override
    public ConfTreeManipulator getConfTreeManipulator() {
        return this.confTreeManipulator;
    }

    @Override
    public ConfNodeOwnerManipulator getConfNodeOwnerManipulator() {
        return this.confNodeOwnerManipulator;
    }

    @Override
    public ConfNodePathManipulator getConfNodePathManipulator() {
        return this.confNodePathManipulator;
    }

    @Override
    public ConfNodeMetaManipulator getConfNodeMetaManipulator() {
        return this.confNodeMetaManipulator;
    }

    @Override
    public NamespaceNodeMetaManipulator getNamespaceNodeMetaManipulator() {
        return this.namespaceNodeMetaManipulator;
    }

    @Override
    public NodeCommonDataManipulator getNodeCommonDataManipulator() {
        return this.nodeCommonDataManipulator;
    }
}
