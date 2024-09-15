package com.pinecone.hydra.config.distribute.operator;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.ConfNode;
import com.pinecone.hydra.config.distribute.entity.GenericConfNode;
import com.pinecone.hydra.config.distribute.entity.GenericConfNodeMeta;
import com.pinecone.hydra.config.distribute.entity.GenericNodeCommonData;
import com.pinecone.hydra.config.distribute.entity.GenericProperties;
import com.pinecone.hydra.config.distribute.entity.TextValue;
import com.pinecone.hydra.unit.udsn.entity.TreeNode;
import com.pinecone.hydra.config.distribute.source.ConfManipulatorSharer;
import com.pinecone.hydra.config.distribute.source.ConfNodeManipulator;
import com.pinecone.hydra.config.distribute.source.ConfNodeMetaManipulator;
import com.pinecone.hydra.config.distribute.source.NodeCommonDataManipulator;
import com.pinecone.hydra.config.distribute.source.PropertiesManipulator;
import com.pinecone.hydra.config.distribute.source.TextValueManipulator;
import com.pinecone.hydra.service.tree.UOIUtils;
import com.pinecone.hydra.unit.udsn.DistributedScopeTree;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.GenericDistributedScopeTree;
import com.pinecone.hydra.unit.udsn.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.udsn.source.TreeManipulatorSharer;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConfNodeOperator implements TreeNodeOperator {
    private DistributedScopeTree        distributedConfTree;
    private ConfManipulatorSharer       confManipulatorSharer;
    protected Map<GUID, ConfNode> cacheMap = new HashMap<>();

    private ConfNodeManipulator         confNodeManipulator;
    private PropertiesManipulator       propertiesManipulator;
    private TextValueManipulator        textValueManipulator;
    private ConfNodeMetaManipulator     confNodeMetaManipulator;
    private NodeCommonDataManipulator   nodeCommonDataManipulator;

    public ConfNodeOperator(ConfManipulatorSharer confManipulatorSharer, TreeManipulatorSharer treeManipulatorSharer){
        this.confManipulatorSharer      =     confManipulatorSharer;
        this.confNodeManipulator        =     this.confManipulatorSharer.getConfigurationManipulator();
        this.propertiesManipulator      =     this.confManipulatorSharer.getPropertiesManipulator();
        this.textValueManipulator       =     this.confManipulatorSharer.getTextValueManipulator();
        this.confNodeMetaManipulator    =     this.confManipulatorSharer.getConfNodeMetaManipulator();
        this.nodeCommonDataManipulator  =     this.confManipulatorSharer.getNodeCommonDataManipulator();
        this.distributedConfTree        =     new GenericDistributedScopeTree(treeManipulatorSharer);
    }

    @Override
    public GUID insert(TreeNode treeNode) {
        GenericConfNode confNode = (GenericConfNode) treeNode;
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID guid72 = uidGenerator.getGUID72();

        confNode.setGuid(guid72);
        confNode.setCreateTime(LocalDateTime.now());
        confNode.setUpdateTime(LocalDateTime.now());

        DistributedTreeNode distributeConfTreeNode = new GUIDDistributedScopeNode();
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

        this.nodeCommonDataManipulator.insert(nodeCommonData);
        this.confNodeMetaManipulator.insert(confNodeMeta);
        this.distributedConfTree.insert(distributeConfTreeNode);
        this.confNodeManipulator.insert(confNode);
        return guid72;
    }

    @Override
    public void remove(GUID guid) {
        //ConfNode为叶子节点只需要删除节点信息与引用继承关系
        GUIDDistributedScopeNode node = this.distributedConfTree.getNode(guid);
        this.distributedConfTree.remove(guid);
        this.confNodeManipulator.remove(guid);
        this.nodeCommonDataManipulator.remove(node.getBaseDataGUID());
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
        GUIDDistributedScopeNode node = this.distributedConfTree.getNode(guid);
        Debug.trace(node.toString());
        ConfNode configurationNode = this.confNodeManipulator.getConfigurationNode(guid);
        List<GenericProperties> properties = this.propertiesManipulator.getProperties(guid);
        TextValue textValue = this.textValueManipulator.getTextValue(guid);
        GenericConfNodeMeta confNodeMeta = (GenericConfNodeMeta) this.confNodeMetaManipulator.getConfNodeMeta(node.getNodeMetadataGUID());
        GenericNodeCommonData nodeCommonData = (GenericNodeCommonData) this.nodeCommonDataManipulator.getNodeCommonData(node.getBaseDataGUID());

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
