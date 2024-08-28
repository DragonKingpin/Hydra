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
        NodeWideData nodeWideData = cacheMap.get(guid);
        if (nodeWideData==null){
            NodeWideData wideData = this.getWideData(guid);
            this.put(guid,wideData);
            return get(guid);
        }else {
            GUID parentGUID = nodeWideData.getParentGUID();
            if (parentGUID==null){
                return nodeWideData;
            }else {
                NodeWideData parentCache = this.get(parentGUID);
                if (!Objects.isNull(parentCache)){
                    this.inherit(nodeWideData,parentCache);
                    return nodeWideData;
                }
            }

        }
        return nodeWideData;
    }

    @Override
    public void put(GUID guid, NodeWideData nodeWideData) {
        this.cacheMap.put(guid, nodeWideData);
    }

    @Override
    public void remove(GUID guid) {
        this.cacheMap.remove(guid);
        for (NodeWideData nodeWideData : cacheMap.values()){
            if (nodeWideData.getParentGUID().equals(guid)){
                remove(nodeWideData.getGuid());
            }
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
