package com.pinecone.hydra.service.tree.entity;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.Debug;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenericApplicationInstance implements MetaNodeInstance {
    private DefaultMetaNodeManipulator          defaultMetaNodeManipulator;
    private Map<GUID, MetaNodeWideEntity>       cacheMap = new HashMap<>();
    private ServiceFamilyTreeManipulator        serviceFamilyTreeManipulator;
    private CommonDataManipulator               commonDataManipulator;
    private ApplicationMetaManipulator          applicationMetaManipulator;
    private ScopeTreeManipulator                scopeTreeManipulator;

    public GenericApplicationInstance(DefaultMetaNodeManipulator defaultMetaNodeManipulator){
        this.defaultMetaNodeManipulator     =  defaultMetaNodeManipulator;
        this.serviceFamilyTreeManipulator   =  this.defaultMetaNodeManipulator.getServiceFamilyTreeManipulator();
        this.commonDataManipulator          =  this.defaultMetaNodeManipulator.getCommonDataManipulator();
        this.applicationMetaManipulator     =  this.defaultMetaNodeManipulator.getApplicationMetaManipulator();
        this.scopeTreeManipulator           =  this.defaultMetaNodeManipulator.getScopeTreeManipulator();
    }

    @Override
    public MetaNodeWideEntity get(GUID guid) {
        MetaNodeWideEntity metaNodeWideEntity = this.cacheMap.get(guid);

        // 如果缓存中没有，则从数据库或其他来源获取
        if (metaNodeWideEntity == null) {
            metaNodeWideEntity = this.getWideData(guid);
            // 将获取到的数据放入缓存
            this.put(guid, metaNodeWideEntity);
        }

        while (metaNodeWideEntity.getParentGUID() != null) {
            GUID parentGUID = metaNodeWideEntity.getParentGUID();
            MetaNodeWideEntity parentData = this.cacheMap.get(parentGUID);

            // 如果父级数据不在缓存中，则获取它
            if (parentData == null) {
                parentData = this.getWideData(parentGUID);
                this.put(parentGUID, parentData);
            }
            Debug.trace("开始继承");
            this.inherit(metaNodeWideEntity, parentData);
            break;
        }

        return metaNodeWideEntity;
    }

    @Override
    public void put(GUID guid, MetaNodeWideEntity metaNodeWideEntity) {
        this.cacheMap.put(guid, metaNodeWideEntity);
    }

    @Override
    public void remove(GUID guid) {
        List<GUIDDistributedScopeNode> childNodes = this.scopeTreeManipulator.getChildNode(guid);
        if (childNodes.isEmpty()){
            clear(guid);
        }else {
            clear(guid);
            for(GUIDDistributedScopeNode node : childNodes){
                List<GUID> parentNode = this.scopeTreeManipulator.getParentNode(node.getGuid());
                if (parentNode.isEmpty()){
                    remove(node.getGuid());
                }else {
                    this.scopeTreeManipulator.removeInheritance(node.getGuid(),guid);
                }
            }
        }
    }

    private void clear(GUID guid) {
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(guid);
        this.scopeTreeManipulator.removeNode(guid);
        this.scopeTreeManipulator.removePath(guid);
        this.applicationMetaManipulator.remove(node.getBaseDataGUID());
        this.commonDataManipulator.remove(node.getNodeMetadataGUID());
        this.serviceFamilyTreeManipulator.removeByChildGUID(guid);
        this.serviceFamilyTreeManipulator.removeByParentGUID(guid);
        this.cacheMap.remove(guid);
    }

    private void inherit(MetaNodeWideEntity chileMetaNodeWideEntity, MetaNodeWideEntity patentMetaNodeWideEntity){
        Class<? extends MetaNodeWideEntity> clazz = chileMetaNodeWideEntity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field:fields){
            field.setAccessible(true);
            try {
                Object value1 = field.get(chileMetaNodeWideEntity);
                Object value2 = field.get(patentMetaNodeWideEntity);
                if (Objects.isNull(value1)){
                    field.set(chileMetaNodeWideEntity,value2);
                }
            } catch (IllegalAccessException e) {
                throw new ProxyProvokeHandleException(e);
            }

        }
    }

    private MetaNodeWideEntity getWideData(GUID guid){
        GenericApplicationWideEntityMeta genericApplicationWideEntity = new GenericApplicationWideEntityMeta();
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(guid);
        GenericNodeCommonData nodeMetadata = this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID());
        GenericApplicationNodeMeta applicationMeta = this.applicationMetaManipulator.getApplicationMeta(node.getBaseDataGUID());
        GUID parentGUID = this.serviceFamilyTreeManipulator.getParentByChildGUID(guid);
        genericApplicationWideEntity.setParentGUID(parentGUID);
        genericApplicationWideEntity.setGuid(guid);
        genericApplicationWideEntity.setScenario(nodeMetadata.getScenario());
        genericApplicationWideEntity.setPrimaryImplLang(nodeMetadata.getPrimaryImplLang());
        genericApplicationWideEntity.setExtraInformation(nodeMetadata.getExtraInformation());
        genericApplicationWideEntity.setLevel(nodeMetadata.getLevel());
        genericApplicationWideEntity.setDescription(nodeMetadata.getDescription());
        genericApplicationWideEntity.setName(applicationMeta.getName());
        genericApplicationWideEntity.setPath(applicationMeta.getPath());
        genericApplicationWideEntity.setType(applicationMeta.getType());
        genericApplicationWideEntity.setResourceType(applicationMeta.getResourceType());
        genericApplicationWideEntity.setDeploymentMethod(applicationMeta.getDeploymentMethod());
        genericApplicationWideEntity.setCreateTime(applicationMeta.getCreateTime());
        genericApplicationWideEntity.setUpdateTime(applicationMeta.getUpdateTime());
        return genericApplicationWideEntity;
    }
}
