package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfNode;
import com.pinecone.hydra.registry.entity.GenericConfNode;
import com.pinecone.hydra.registry.entity.GenericConfNodeMeta;
import com.pinecone.hydra.registry.entity.GenericNodeCommonData;
import com.pinecone.hydra.registry.entity.GenericProperties;
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

public class ConfNodeOperator implements TreeNodeOperator {
    private DistributedTrieTree distributedConfTree;
    private RegistryMasterManipulator registryMasterManipulator;
    protected Map<GUID, ConfNode> cacheMap = new HashMap<>();

    private RegistryNodeManipulator confNodeManipulator;
    private RegistryPropertiesManipulator registryPropertiesManipulator;
    private RegistryTextValueManipulator registryTextValueManipulator;
    private RegistryNodeMetaManipulator confNodeMetaManipulator;
    private RegistryCommonDataManipulator registryCommonDataManipulator;

    public ConfNodeOperator(RegistryMasterManipulator registryMasterManipulator, TreeMasterManipulator treeManipulatorSharer){
        this.registryMasterManipulator = registryMasterManipulator;
        this.confNodeManipulator        =     this.registryMasterManipulator.getRegistryNodeManipulator();
        this.registryPropertiesManipulator =     this.registryMasterManipulator.getRegistryPropertiesManipulator();
        this.registryTextValueManipulator =     this.registryMasterManipulator.getRegistryTextValueManipulator();
        this.confNodeMetaManipulator    =     this.registryMasterManipulator.getConfNodeMetaManipulator();
        this.registryCommonDataManipulator =     this.registryMasterManipulator.getRegistryCommonDataManipulator();
        this.distributedConfTree        =     new GenericDistributedTrieTree(treeManipulatorSharer);
    }

    @Override
    public GUID insert(TreeNode treeNode) {
        GenericConfNode confNode = (GenericConfNode) treeNode;
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID guid72 = uidGenerator.getGUID72();

        confNode.setGuid(guid72);
        confNode.setCreateTime(LocalDateTime.now());
        confNode.setUpdateTime(LocalDateTime.now());

        DistributedTreeNode distributeConfTreeNode = new GUIDDistributedTrieNode();
        distributeConfTreeNode.setGuid(guid72);
        distributeConfTreeNode.setType(UOIUtils.createLocalJavaClass(confNode.getClass().getName()));


        GUID confNodeMetaGuid = uidGenerator.getGUID72();
        GenericConfNodeMeta confNodeMeta = confNode.getConfNodeMeta();
        confNodeMeta.setGuid(confNodeMetaGuid);

        GUID commonDataGuid = uidGenerator.getGUID72();
        GenericNodeCommonData nodeCommonData = confNode.getNodeCommonData();
        nodeCommonData.setGuid(commonDataGuid);

        distributeConfTreeNode.setBaseDataGUID(commonDataGuid);
        distributeConfTreeNode.setNodeMetadataGUID(confNodeMetaGuid);

        this.registryCommonDataManipulator.insert(nodeCommonData);
        this.confNodeMetaManipulator.insert(confNodeMeta);
        this.distributedConfTree.insert(distributeConfTreeNode);
        this.confNodeManipulator.insert(confNode);
        return guid72;
    }

    @Override
    public void remove(GUID guid) {
        //ConfNode为叶子节点只需要删除节点信息与引用继承关系
        GUIDDistributedTrieNode node = this.distributedConfTree.getNode(guid);
        this.distributedConfTree.remove(guid);
        this.confNodeManipulator.remove(guid);
        this.registryCommonDataManipulator.remove(node.getBaseDataGUID());
        this.confNodeMetaManipulator.remove(node.getNodeMetadataGUID());
        this.distributedConfTree.removePath(guid);
    }

    @Override
    public TreeNode get(GUID guid) {
        //检测缓存中是否存在信息
        ConfNode confNode = this.cacheMap.get(guid);
        if (confNode == null) {
            confNode = this.getConfNodeWideData(guid);
            GUID parentGuid = confNode.getParentGuid();
            if (parentGuid != null){
                this.inherit(confNode,(ConfNode) get(parentGuid));
            }
            this.cacheMap.put(guid,confNode);
        }
        return confNode;
    }

    @Override
    public TreeNode getWithoutInheritance(GUID guid) {
        return this.getConfNodeWideData(guid);
    }


    protected ConfNode getConfNodeWideData(GUID guid){
        GUIDDistributedTrieNode node = this.distributedConfTree.getNode(guid);
        Debug.trace(node.toString());
        ConfNode configurationNode = this.confNodeManipulator.getConfigurationNode(guid);
        List<GenericProperties> properties = this.registryPropertiesManipulator.getProperties(guid);
        TextValue textValue = this.registryTextValueManipulator.getTextValue(guid);
        GenericConfNodeMeta confNodeMeta = (GenericConfNodeMeta) this.confNodeMetaManipulator.getConfNodeMeta(node.getNodeMetadataGUID());
        GenericNodeCommonData nodeCommonData = (GenericNodeCommonData) this.registryCommonDataManipulator.getNodeCommonData(node.getBaseDataGUID());

        configurationNode.setNodeCommonData(nodeCommonData);
        configurationNode.setConfNodeMeta(confNodeMeta);
        configurationNode.setProperties(properties);
        configurationNode.setTextValue(textValue);
        return configurationNode;
    }

    protected void inherit(ConfNode chileConfNode, ConfNode patentConfNode ){
        Class<? extends ConfNode> clazz = chileConfNode.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields){
            field.setAccessible(true);
            try {
                Object value1 = field.get(chileConfNode);
                Object value2 = field.get(patentConfNode);
                if (Objects.isNull(value1) || (value1 instanceof List && ((List<?>) value1).isEmpty())){
                    field.set(chileConfNode,value2);
                }
            }
            catch (IllegalAccessException e) {
                throw new ProxyProvokeHandleException(e);
            }
        }
    }
}
