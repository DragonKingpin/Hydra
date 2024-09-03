package com.pinecone.hydra.service.tree.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulators;
import com.pinecone.hydra.service.tree.source.ServiceFamilyTreeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeOwnerManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;

public abstract class ArchMetaNodeInstance implements MetaNodeInstance {
    protected DefaultMetaNodeManipulators defaultMetaNodeManipulators;
    protected Map<GUID, MetaNodeWideEntity>       cacheMap = new HashMap<>();
    protected ServiceFamilyTreeManipulator        serviceFamilyTreeManipulator;
    protected CommonDataManipulator               commonDataManipulator;
    protected ScopeTreeManipulator                scopeTreeManipulator;
    protected ServiceNodeOwnerManipulator         serviceNodeOwnerManipulator;


    public ArchMetaNodeInstance( DefaultMetaNodeManipulators defaultMetaNodeManipulators){
        this.defaultMetaNodeManipulators = defaultMetaNodeManipulators;
        this.serviceFamilyTreeManipulator   =  this.defaultMetaNodeManipulators.getServiceFamilyTreeManipulator();
        this.commonDataManipulator          =  this.defaultMetaNodeManipulators.getCommonDataManipulator();
        this.scopeTreeManipulator           =  this.defaultMetaNodeManipulators.getScopeTreeManipulator();
        this.serviceNodeOwnerManipulator    =  this.defaultMetaNodeManipulators.getServiceNodeOwnerManipulator();
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

        while ( metaNodeWideEntity.getParentGUIDs() != null ) {
            GUID parentGUID = metaNodeWideEntity.getParentGUIDs();
            MetaNodeWideEntity parentData = this.cacheMap.get(parentGUID);

            // 如果父级数据不在缓存中，则获取它
            if ( parentData == null ) {
                parentData = this.getWideData(parentGUID);
                this.put( parentGUID, parentData );
            }
            Debug.trace( "开始继承" );
            this.inherit( metaNodeWideEntity, parentData );
            break;
        }

        return metaNodeWideEntity;
    }

    @Override
    public void put(GUID guid, MetaNodeWideEntity metaNodeWideEntity) {
        this.cacheMap.put(guid, metaNodeWideEntity);
    }

    @Override
    public void remove( GUID guid ) {
        List<GUIDDistributedScopeNode > childNodes = this.scopeTreeManipulator.getChildNode(guid);
        List<GUID> subordinates = this.serviceNodeOwnerManipulator.getSubordinates(guid);
        if ( childNodes.isEmpty() ){
            this.removeDependence( guid );
        }
        else {
            if (!subordinates.isEmpty()){
                for (GUID subordinateGUID : subordinates){
                    this.remove(subordinateGUID);
                }
            }
            childNodes = this.scopeTreeManipulator.getChildNode(guid);
            this.removeDependence( guid );
            for( GUIDDistributedScopeNode node : childNodes ){
                List<GUID > parentNode = this.scopeTreeManipulator.getParentNodes( node.getGuid() );
                if ( parentNode.size() <= 1 ){
                    this.remove( node.getGuid() );
                }
                else {
                    this.scopeTreeManipulator.removeInheritance( node.getGuid(), guid );
                }
            }
        }
    }

    protected GUIDDistributedScopeNode removeDependence0( GUID guid ) {
        GUIDDistributedScopeNode target = this.scopeTreeManipulator.getNode( guid );
        this.scopeTreeManipulator.removeNode( guid );
        this.scopeTreeManipulator.removePath( guid );
        this.commonDataManipulator.remove( target.getNodeMetadataGUID() );
        this.serviceFamilyTreeManipulator.removeByChildGUID( guid );
        this.serviceFamilyTreeManipulator.removeByParentGUID( guid );
        this.cacheMap.remove( guid );
        this.serviceNodeOwnerManipulator.removeBySubordinate(guid);
        return target;
    }

    protected void removeDependence( GUID guid ) {
        this.removeDependence0( guid );
    }

    protected void inherit( MetaNodeWideEntity chileMetaNodeWideEntity, MetaNodeWideEntity patentMetaNodeWideEntity ){
        Class<? extends MetaNodeWideEntity> clazz = chileMetaNodeWideEntity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields){
            field.setAccessible(true);
            try {
                Object value1 = field.get(chileMetaNodeWideEntity);
                Object value2 = field.get(patentMetaNodeWideEntity);
                if (Objects.isNull(value1)){
                    field.set(chileMetaNodeWideEntity,value2);
                }
            }
            catch (IllegalAccessException e) {
                throw new ProxyProvokeHandleException(e);
            }
        }
    }

    protected abstract MetaNodeWideEntity getWideData(GUID guid);
}
