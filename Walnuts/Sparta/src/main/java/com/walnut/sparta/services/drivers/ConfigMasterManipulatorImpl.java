package com.walnut.sparta.services.drivers;

import com.pinecone.hydra.config.distribute.source.ConfigMasterManipulator;
import com.pinecone.hydra.config.distribute.source.ConfigNodeManipulator;
import com.pinecone.hydra.config.distribute.source.ConfigNodeMetaManipulator;
import com.pinecone.hydra.config.distribute.source.ConfigNSNodeManipulator;
import com.pinecone.hydra.config.distribute.source.ConfigNSNodeMetaManipulator;
import com.pinecone.hydra.config.distribute.source.NodeCommonDataManipulator;
import com.pinecone.hydra.config.distribute.source.PropertiesManipulator;
import com.pinecone.hydra.config.distribute.source.TextValueManipulator;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ConfigMasterManipulatorImpl implements ConfigMasterManipulator {
    @Resource
    ConfigNodeManipulator           confNodeManipulator;

    @Resource
    ConfigNSNodeManipulator         namespaceNodeManipulator;

    @Resource
    PropertiesManipulator           propertiesManipulator;

    @Resource
    TextValueManipulator            textValueManipulator;

    @Resource
    ConfigNodeMetaManipulator       confNodeMetaManipulator;

    @Resource
    ConfigNSNodeMetaManipulator     namespaceNodeMetaManipulator;

    @Resource
    NodeCommonDataManipulator       nodeCommonDataManipulator;

    @Override
    public ConfigNodeManipulator getConfigurationManipulator() {
        return this.confNodeManipulator;
    }

    @Override
    public ConfigNSNodeManipulator getNamespaceManipulator() {
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
    public ConfigNodeMetaManipulator getConfNodeMetaManipulator() {
        return this.confNodeMetaManipulator;
    }

    @Override
    public ConfigNSNodeMetaManipulator getNamespaceNodeMetaManipulator() {
        return this.namespaceNodeMetaManipulator;
    }

    @Override
    public NodeCommonDataManipulator getNodeCommonDataManipulator() {
        return this.nodeCommonDataManipulator;
    }
}
