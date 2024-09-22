package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.GenericConfigNode;
import com.pinecone.hydra.registry.entity.GenericConfigNodeMeta;
import com.pinecone.hydra.registry.entity.GenericNodeCommonData;
import com.pinecone.hydra.registry.entity.GenericProperty;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.registry.source.RegistryNodeManipulator;
import com.pinecone.hydra.registry.source.RegistryNodeMetaManipulator;
import com.pinecone.hydra.registry.source.RegistryCommonDataManipulator;
import com.pinecone.hydra.registry.source.RegistryPropertiesManipulator;
import com.pinecone.hydra.registry.source.RegistryTextValueManipulator;
import com.pinecone.hydra.service.tree.UOIUtils;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.hydra.unit.udtt.DistributedTreeNode;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.GenericDistributedTrieTree;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConfigNodeOperator implements TreeNodeOperator {
    private DistributedTrieTree distributedConfTree;
    private RegistryMasterManipulator registryMasterManipulator;
    protected Map<GUID, ConfigNode> cacheMap = new HashMap<>();

    private RegistryNodeManipulator configNodeManipulator;
    private RegistryPropertiesManipulator registryPropertiesManipulator;
    private RegistryTextValueManipulator registryTextValueManipulator;
    private RegistryNodeMetaManipulator configNodeMetaManipulator;
    private RegistryCommonDataManipulator registryCommonDataManipulator;

    public ConfigNodeOperator(RegistryMasterManipulator registryMasterManipulator, TreeMasterManipulator treeManipulatorSharer){
        this.registryMasterManipulator = registryMasterManipulator;
        this.configNodeManipulator        =     this.registryMasterManipulator.getRegistryNodeManipulator();
        this.registryPropertiesManipulator =     this.registryMasterManipulator.getRegistryPropertiesManipulator();
        this.registryTextValueManipulator =     this.registryMasterManipulator.getRegistryTextValueManipulator();
        this.configNodeMetaManipulator    =     this.registryMasterManipulator.getRegistryNodeMetaManipulator();
        this.registryCommonDataManipulator =     this.registryMasterManipulator.getRegistryCommonDataManipulator();
        this.distributedConfTree        =     new GenericDistributedTrieTree(treeManipulatorSharer);
    }

    @Override
    public GUID insert(TreeNode treeNode) {
        GenericConfigNode configNode = (GenericConfigNode) treeNode;
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID guid72 = uidGenerator.getGUID72();

        configNode.setGuid(guid72);
        configNode.setCreateTime(LocalDateTime.now());
        configNode.setUpdateTime(LocalDateTime.now());

        DistributedTreeNode distributeConfTreeNode = new GUIDDistributedTrieNode();
        distributeConfTreeNode.setGuid(guid72);
        distributeConfTreeNode.setType(UOIUtils.createLocalJavaClass(configNode.getClass().getName()));


        GUID configNodeMetaGuid = uidGenerator.getGUID72();
        GenericConfigNodeMeta configNodeMeta = configNode.getConfigNodeMeta();
        configNodeMeta.setGuid(configNodeMetaGuid);

        GUID commonDataGuid = uidGenerator.getGUID72();
        GenericNodeCommonData nodeCommonData = configNode.getNodeCommonData();
        nodeCommonData.setGuid(commonDataGuid);

        distributeConfTreeNode.setBaseDataGUID(commonDataGuid);
        distributeConfTreeNode.setNodeMetadataGUID(configNodeMetaGuid);

        this.registryCommonDataManipulator.insert(nodeCommonData);
        this.configNodeMetaManipulator.insert(configNodeMeta);
        this.distributedConfTree.insert(distributeConfTreeNode);
        this.configNodeManipulator.insert(configNode);
        return guid72;
    }

    @Override
    public void remove(GUID guid) {
        //ConfigNode为叶子节点只需要删除节点信息与引用继承关系
        GUIDDistributedTrieNode node = this.distributedConfTree.getNode(guid);
        this.distributedConfTree.remove(guid);
        this.configNodeManipulator.remove(guid);
        this.registryCommonDataManipulator.remove(node.getBaseDataGUID());
        this.configNodeMetaManipulator.remove(node.getNodeMetadataGUID());
        this.distributedConfTree.removePath(guid);
    }

    @Override
    public TreeNode get(GUID guid) {
        //检测缓存中是否存在信息
        ConfigNode configNode = this.cacheMap.get(guid);
        if (configNode == null) {
            configNode = this.getConfigNodeWideData(guid);
            GUID parentGuid = configNode.getParentGuid();
            if (parentGuid != null){
                this.inherit(configNode,(ConfigNode) get(parentGuid));
            }
            this.cacheMap.put(guid, configNode);
        }
        return configNode;
    }

    @Override
    public TreeNode getWithoutInheritance(GUID guid) {
        return this.getConfigNodeWideData(guid);
    }

    @Override
    public void update(TreeNode treeNode) {
        ConfigNode configNode = (ConfigNode) treeNode;
        GenericConfigNodeMeta configNodeMeta = configNode.getConfigNodeMeta();
        GenericNodeCommonData nodeCommonData = configNode.getNodeCommonData();
        configNode.setUpdateTime(LocalDateTime.now());
        if (configNodeMeta != null){
            this.configNodeMetaManipulator.update(configNodeMeta);
        }
        if (nodeCommonData != null){
            this.registryCommonDataManipulator.update(nodeCommonData);
        }
        this.configNodeManipulator.update(configNode);
    }


    protected ConfigNode getConfigNodeWideData(GUID guid){
        GUIDDistributedTrieNode node = this.distributedConfTree.getNode(guid);
        //Debug.trace(node.toString());
        ConfigNode configurationNode = this.configNodeManipulator.getConfigurationNode(guid);
        List<GenericProperty> properties = this.registryPropertiesManipulator.getProperties(guid);
        TextValue textValue = this.registryTextValueManipulator.getTextValue(guid);
        GenericConfigNodeMeta configNodeMeta = (GenericConfigNodeMeta) this.configNodeMetaManipulator.getConfigNodeMeta(node.getNodeMetadataGUID());
        GenericNodeCommonData nodeCommonData = (GenericNodeCommonData) this.registryCommonDataManipulator.getNodeCommonData(node.getBaseDataGUID());

        configurationNode.setNodeCommonData(nodeCommonData);
        configurationNode.setConfigNodeMeta(configNodeMeta);
        configurationNode.setProperties(properties);
        configurationNode.setTextValue(textValue);
        return configurationNode;
    }

    protected void inherit(ConfigNode chileConfigNode, ConfigNode patentConfigNode){
        Class<? extends ConfigNode> clazz = chileConfigNode.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields){
            field.setAccessible(true);
            try {
                Object value1 = field.get(chileConfigNode);
                Object value2 = field.get(patentConfigNode);
                if (Objects.isNull(value1) || (value1 instanceof List && ((List<?>) value1).isEmpty())){
                    field.set(chileConfigNode,value2);
                }
            }
            catch (IllegalAccessException e) {
                throw new ProxyProvokeHandleException(e);
            }
        }
    }
}
