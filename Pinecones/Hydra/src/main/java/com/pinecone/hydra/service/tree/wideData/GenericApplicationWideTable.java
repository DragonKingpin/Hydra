package com.pinecone.hydra.service.tree.wideData;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.meta.GenericApplicationNodeMeta;
import com.pinecone.hydra.service.tree.source.ApplicationMetaManipulator;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceFamilyTreeManipulator;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenericApplicationWideTable implements NodeWideTable {
    private DefaultMetaNodeManipulator      defaultMetaNodeManipulator;
    private Map<GUID, NodeWideData>         cacheMap = new HashMap<>();
    private ServiceFamilyTreeManipulator    serviceFamilyTreeManipulator;
    private CommonDataManipulator           commonDataManipulator;
    private ApplicationMetaManipulator      applicationMetaManipulator;
    private ScopeTreeManipulator            scopeTreeManipulator;

    public GenericApplicationWideTable(DefaultMetaNodeManipulator defaultMetaNodeManipulator){
        this.defaultMetaNodeManipulator     =  defaultMetaNodeManipulator;
        this.serviceFamilyTreeManipulator   =  this.defaultMetaNodeManipulator.getServiceFamilyTreeManipulator();
        this.commonDataManipulator          =  this.defaultMetaNodeManipulator.getCommonDataManipulator();
        this.applicationMetaManipulator     =  this.defaultMetaNodeManipulator.getApplicationMetaManipulator();
        this.scopeTreeManipulator           =  this.defaultMetaNodeManipulator.getScopeTreeManipulator();
    }

    @Override
    public NodeWideData get(GUID guid) {
        NodeWideData nodeWideData = this.cacheMap.get(guid);

        // 如果缓存中没有，则从数据库或其他来源获取
        if (nodeWideData == null) {
            nodeWideData = this.getWideData(guid);
            // 将获取到的数据放入缓存
            this.put(guid, nodeWideData);
        }

        while (nodeWideData.getParentGUID() != null) {
            GUID parentGUID = nodeWideData.getParentGUID();
            NodeWideData parentData = this.cacheMap.get(parentGUID);

            // 如果父级数据不在缓存中，则获取它
            if (parentData == null) {
                parentData = this.getWideData(parentGUID);
                this.put(parentGUID, parentData);
            }
            Debug.trace("开始继承");
            this.inherit(nodeWideData, parentData);
            break;
        }

        return nodeWideData;
    }

    @Override
    public void put(GUID guid, NodeWideData nodeWideData) {
        this.cacheMap.put(guid, nodeWideData);
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(guid);
        this.scopeTreeManipulator.removeNode(guid);
        this.scopeTreeManipulator.removePath(guid);
        this.applicationMetaManipulator.remove(node.getBaseDataGUID());
        this.commonDataManipulator.remove(node.getNodeMetadataGUID());
        this.serviceFamilyTreeManipulator.removeByChildGUID(guid);
        this.serviceFamilyTreeManipulator.removeByParentGUID(guid);
        this.cacheMap.remove(guid);
        List<GUIDDistributedScopeNode> childNodes = this.scopeTreeManipulator.getChildNode(guid);
        for (GUIDDistributedScopeNode scopeNode : childNodes){
            this.remove(scopeNode.getGuid());
        }
    }

    private void inherit(NodeWideData chileNodeWideData, NodeWideData patentNodeWideData){
        Class<? extends NodeWideData> clazz = chileNodeWideData.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field:fields){
            field.setAccessible(true);
            try {
                Object value1 = field.get(chileNodeWideData);
                Object value2 = field.get(patentNodeWideData);
                if (Objects.isNull(value1)){
                    field.set(chileNodeWideData,value2);
                }
            } catch (IllegalAccessException e) {
                throw new ProxyProvokeHandleException(e);
            }

        }
    }

    private NodeWideData getWideData(GUID guid){
        GenericApplicationWideDate genericApplicationWideDate = new GenericApplicationWideDate();
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(guid);
        GenericNodeCommonData nodeMetadata = this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID());
        GenericApplicationNodeMeta applicationMeta = this.applicationMetaManipulator.getApplicationMeta(node.getBaseDataGUID());
        GUID parentGUID = this.serviceFamilyTreeManipulator.getParentByChildGUID(guid);
        genericApplicationWideDate.setParentGUID(parentGUID);
        genericApplicationWideDate.setGuid(guid);
        genericApplicationWideDate.setScenario(nodeMetadata.getScenario());
        genericApplicationWideDate.setPrimaryImplLang(nodeMetadata.getPrimaryImplLang());
        genericApplicationWideDate.setExtraInformation(nodeMetadata.getExtraInformation());
        genericApplicationWideDate.setLevel(nodeMetadata.getLevel());
        genericApplicationWideDate.setDescription(nodeMetadata.getDescription());
        genericApplicationWideDate.setName(applicationMeta.getName());
        genericApplicationWideDate.setPath(applicationMeta.getPath());
        genericApplicationWideDate.setType(applicationMeta.getType());
        genericApplicationWideDate.setResourceType(applicationMeta.getResourceType());
        genericApplicationWideDate.setDeploymentMethod(applicationMeta.getDeploymentMethod());
        genericApplicationWideDate.setCreateTime(applicationMeta.getCreateTime());
        genericApplicationWideDate.setUpdateTime(applicationMeta.getUpdateTime());
        return genericApplicationWideDate;
    }
}
