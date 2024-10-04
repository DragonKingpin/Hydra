package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.DistributedRegistry;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.ConfigNodeMeta;
import com.pinecone.hydra.registry.entity.ArchConfigNode;
import com.pinecone.hydra.registry.entity.GenericConfigNodeMeta;
import com.pinecone.hydra.registry.entity.GenericNodeAttribute;
import com.pinecone.hydra.registry.entity.NodeAttribute;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.registry.source.RegistryCommonDataManipulator;
import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.registry.source.RegistryConfigNodeManipulator;
import com.pinecone.hydra.registry.source.RegistryNodeMetaManipulator;
import com.pinecone.hydra.service.tree.UOIUtils;
import com.pinecone.hydra.unit.udtt.DistributedTreeNode;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.GenericDistributedTrieTree;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.ulf.util.id.GuidAllocator;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class ArchConfigNodeOperator implements RegistryNodeOperator{
    protected DistributedTrieTree           distributedTrieTree;
    protected RegistryMasterManipulator     registryMasterManipulator;
    protected Map<GUID, ConfigNode>         cacheMap = new HashMap<>();

    protected RegistryConfigNodeManipulator registryConfigNodeManipulator;
    protected RegistryNodeMetaManipulator   configNodeMetaManipulator;
    protected RegistryCommonDataManipulator registryCommonDataManipulator;

    protected DistributedRegistry           registry;

    public ArchConfigNodeOperator(ConfigOperatorFactory factory ) {
        this( factory.getMasterManipulator(), (DistributedRegistry) factory.getRegistry() );
    }

    public ArchConfigNodeOperator(RegistryMasterManipulator masterManipulator, DistributedRegistry registry ){
        this.registryMasterManipulator     = masterManipulator;
        this.registryConfigNodeManipulator = this.registryMasterManipulator.getRegistryConfigNodeManipulator();
        this.configNodeMetaManipulator     = this.registryMasterManipulator.getRegistryNodeMetaManipulator();
        this.registryCommonDataManipulator = this.registryMasterManipulator.getRegistryCommonDataManipulator();
        this.distributedTrieTree           = new GenericDistributedTrieTree( (TreeMasterManipulator) masterManipulator.getSkeletonMasterManipulator() );

        this.registry                      = registry;
    }

    @Override
    public GUID insert( TreeNode treeNode ) {
        ArchConfigNode configNode = (ArchConfigNode) treeNode;
        GuidAllocator guidAllocator = this.registry.getGuidAllocator();
        GUID guid72 = guidAllocator.nextGUID72();

        configNode.setGuid(guid72);
        configNode.setCreateTime(LocalDateTime.now());
        configNode.setUpdateTime(LocalDateTime.now());

        DistributedTreeNode distributeConfTreeNode = new GUIDDistributedTrieNode();
        distributeConfTreeNode.setGuid(guid72);
        distributeConfTreeNode.setType(UOIUtils.createLocalJavaClass(configNode.getClass().getName()));


        GUID configNodeMetaGuid = guidAllocator.nextGUID72();
        GenericConfigNodeMeta configNodeMeta = configNode.getConfigNodeMeta();
        if ( configNodeMeta != null ){
            configNodeMeta.setGuid(configNodeMetaGuid);
            this.configNodeMetaManipulator.insert(configNodeMeta);
        }
        else {
            configNodeMetaGuid = null;
        }


        GUID commonDataGuid = guidAllocator.nextGUID72();
        GenericNodeAttribute nodeCommonData = configNode.getNodeCommonData();
        if (nodeCommonData != null){
            nodeCommonData.setGuid(commonDataGuid);
            this.registryCommonDataManipulator.insert(nodeCommonData);
        }
        else {
            commonDataGuid = null;
        }


        distributeConfTreeNode.setBaseDataGUID(commonDataGuid);
        distributeConfTreeNode.setNodeMetadataGUID(configNodeMetaGuid);
        this.distributedTrieTree.insert(distributeConfTreeNode);
        this.registryConfigNodeManipulator.insert(configNode);
        return guid72;
    }

    @Override
    public void purge( GUID guid ) {
        //ConfigNode为叶子节点只需要删除节点信息与引用继承关系
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        this.distributedTrieTree.purge( guid );
        this.registryConfigNodeManipulator.remove(guid);
        this.registryCommonDataManipulator.remove(node.getBaseDataGUID());
        this.configNodeMetaManipulator.remove(node.getNodeMetadataGUID());
        this.distributedTrieTree.removeCachePath(guid);
    }

    @Override
    public RegistryTreeNode get( GUID guid ) {
        ConfigNode rootConfig = this.cacheMap.get( guid );
        if ( rootConfig == null ) {
            rootConfig = this.getConfigNodeWideData( guid );
            ConfigNode thisConfig = rootConfig;
            while ( true ) {
                GUID affinityGuid = thisConfig.getDataAffinityGuid();
                if ( affinityGuid != null ){
                    ConfigNode parent = this.getConfigNodeWideData( affinityGuid );
                    this.inherit( thisConfig, parent );
                    thisConfig = parent;
                }
                else {
                    break;
                }
            }
            this.cacheMap.put( guid, rootConfig );
        }
        return rootConfig;
    }

    @Override
    public RegistryTreeNode getSelf( GUID guid ) {
        return this.getConfigNodeWideData( guid );
    }

    @Override
    public void update( TreeNode treeNode ) {
        ConfigNode configNode = (ConfigNode) treeNode;
        ConfigNodeMeta configNodeMeta = configNode.getConfigNodeMeta();
        NodeAttribute nodeAttribute = configNode.getNodeCommonData();
        configNode.setUpdateTime(LocalDateTime.now());
        if (configNodeMeta != null){
            this.configNodeMetaManipulator.update(configNodeMeta);
        }
        if (nodeAttribute != null){
            this.registryCommonDataManipulator.update(nodeAttribute);
        }
        this.registryConfigNodeManipulator.update(configNode);
    }

    @Override
    public void updateName( GUID guid, String name ) {
        this.registryConfigNodeManipulator.updateName( guid, name );
    }

    protected ConfigNode getConfigNodeWideData( GUID guid ){
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        ConfigNode cn = this.registryConfigNodeManipulator.getConfigNode( guid );
        if( cn instanceof ArchConfigNode ) {
            ((ArchConfigNode) cn).apply( this.registry );
        }

        GenericConfigNodeMeta configNodeMeta = (GenericConfigNodeMeta) this.configNodeMetaManipulator.getConfigNodeMeta(node.getNodeMetadataGUID());
        GenericNodeAttribute nodeCommonData = (GenericNodeAttribute) this.registryCommonDataManipulator.getNodeCommonData(node.getBaseDataGUID());

        cn.setNodeCommonData( nodeCommonData );
        cn.setConfigNodeMeta( configNodeMeta );
        return cn;
    }

    protected void inherit( ConfigNode self, ConfigNode prototype ){
        Class<? extends ConfigNode> clazz = self.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for ( Field field : fields ){
            field.setAccessible(true);
            try {
                Object value1 = field.get( self );
                Object value2 = field.get( prototype );
                if ( Objects.isNull(value1) || (value1 instanceof List && ((List<?>) value1).isEmpty()) ){
                    field.set(self,value2);
                }
            }
            catch ( IllegalAccessException e ) {
                throw new ProxyProvokeHandleException(e);
            }
        }
    }
}
