package com.pinecone.hydra.service.tree.wideData;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.meta.GenericApplicationNodeMeta;
import com.pinecone.hydra.service.tree.source.ApplicationMetaManipulator;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceFamilyTreeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;

import java.lang.reflect.Field;
import java.util.HashMap;
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
