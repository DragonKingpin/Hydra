package com.pinecone.hydra.service.tree.wideData;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceFamilyTreeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceMetaManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenericServiceWideTable implements NodeWideTable {
    private DefaultMetaNodeManipulator      defaultMetaNodeManipulator;
    private Map<GUID, NodeWideData>         cacheMap = new HashMap<>();
    private ServiceFamilyTreeManipulator    serviceFamilyTreeManipulator;
    private ServiceMetaManipulator          serviceMetaManipulator;
    private CommonDataManipulator           commonDataManipulator;
    private ScopeTreeManipulator            scopeTreeManipulator;

    public GenericServiceWideTable(DefaultMetaNodeManipulator defaultMetaNodeManipulator){
        this.defaultMetaNodeManipulator       =   defaultMetaNodeManipulator;
        this.serviceFamilyTreeManipulator     =   defaultMetaNodeManipulator.getServiceFamilyTreeManipulator();
        this.serviceMetaManipulator           =   defaultMetaNodeManipulator.getServiceMetaManipulator();
        this.commonDataManipulator            =   defaultMetaNodeManipulator.getCommonDataManipulator();
        this.scopeTreeManipulator             =   defaultMetaNodeManipulator.getScopeTreeManipulator();
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

            this.inherit(nodeWideData, parentData);
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
        this.serviceMetaManipulator.remove(node.getBaseDataGUID());
        this.commonDataManipulator.remove(node.getNodeMetadataGUID());
        this.scopeTreeManipulator.removePath(guid);
        this.scopeTreeManipulator.removeNode(guid);
        this.serviceFamilyTreeManipulator.removeByParentGUID(guid);
        this.serviceFamilyTreeManipulator.removeByChildGUID(guid);
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
                throw new RuntimeException(e);
            }

        }
    }

    private NodeWideData getWideData(GUID guid){
        GenericApplicationWideDate genericApplicationWideDate = new GenericApplicationWideDate();
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(guid);
        GUID parentGUID = this.serviceFamilyTreeManipulator.getParentByChildGUID(guid);
        GenericNodeCommonData commonData = this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID());
        GenericServiceNodeMeta serviceMeta = this.serviceMetaManipulator.getServiceMeta(node.getBaseDataGUID());
        Debug.trace(commonData);
        genericApplicationWideDate.setParentGUID(parentGUID);
        genericApplicationWideDate.setGuid(guid);
        genericApplicationWideDate.setScenario(commonData.getScenario());
        genericApplicationWideDate.setPrimaryImplLang(commonData.getPrimaryImplLang());
        genericApplicationWideDate.setExtraInformation(commonData.getExtraInformation());
        genericApplicationWideDate.setLevel(commonData.getLevel());
        genericApplicationWideDate.setDescription(commonData.getDescription());
        genericApplicationWideDate.setName(serviceMeta.getName());
        genericApplicationWideDate.setPath(serviceMeta.getPath());
        genericApplicationWideDate.setType(serviceMeta.getType());
        genericApplicationWideDate.setResourceType(serviceMeta.getResourceType());
        genericApplicationWideDate.setCreateTime(serviceMeta.getCreateTime());
        genericApplicationWideDate.setUpdateTime(serviceMeta.getUpdateTime());
        return genericApplicationWideDate;
    }
}
