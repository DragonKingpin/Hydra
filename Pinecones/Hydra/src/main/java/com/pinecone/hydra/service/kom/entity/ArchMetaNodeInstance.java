package com.pinecone.hydra.service.kom.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.source.CommonDataManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.service.kom.source.ServiceFamilyTreeManipulator;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.GenericDistributedTrieTree;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;

public abstract class ArchMetaNodeInstance implements MetaNodeInstance {
    protected ServiceMasterManipulator            serviceMasterManipulator;
    protected Map<GUID, MetaNodeWideEntity>       cacheMap = new HashMap<>();
    protected ServiceFamilyTreeManipulator        serviceFamilyTreeManipulator;
    protected CommonDataManipulator               commonDataManipulator;
    protected TireOwnerManipulator                tireOwnerManipulator;
    protected DistributedTrieTree                 distributedTrieTree;


    public ArchMetaNodeInstance( ServiceMasterManipulator serviceMasterManipulator, TreeMasterManipulator treeManipulatorSharer ){
        this.serviceMasterManipulator           =       serviceMasterManipulator;
        this.serviceFamilyTreeManipulator       =       this.serviceMasterManipulator.getServiceFamilyTreeManipulator();
        this.commonDataManipulator              =       this.serviceMasterManipulator.getCommonDataManipulator();
        this.tireOwnerManipulator               =       this.serviceMasterManipulator.getTireOwnerManipulator();
        this.distributedTrieTree                =       new GenericDistributedTrieTree(treeManipulatorSharer);
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
        List<GUIDDistributedTrieNode> childNodes = this.distributedTrieTree.getChildren(guid);
        List<GUID> subordinates = this.tireOwnerManipulator.getSubordinates(guid);
        if ( childNodes.isEmpty() ){
            this.removeDependence( guid );
        }
        else {
            if (!subordinates.isEmpty()){
                for (GUID subordinateGUID : subordinates){
                    this.remove(subordinateGUID);
                }
            }
            childNodes = this.distributedTrieTree.getChildren(guid);
            this.removeDependence( guid );
            for( GUIDDistributedTrieNode node : childNodes ){
                List<GUID > parentNode = this.distributedTrieTree.getParentGuids( node.getGuid() );
                if ( parentNode.size() <= 1 ){
                    this.remove( node.getGuid() );
                }
                else {
                    this.distributedTrieTree.removeInheritance( node.getGuid(), guid );
                }
            }
        }
    }

    protected GUIDDistributedTrieNode removeDependence0(GUID guid ) {
        GUIDDistributedTrieNode target = this.distributedTrieTree.getNode( guid );
        this.distributedTrieTree.purge( guid );
        this.distributedTrieTree.removeCachePath( guid );
        this.commonDataManipulator.remove( target.getNodeMetadataGUID() );
        this.serviceFamilyTreeManipulator.removeByChildGUID( guid );
        this.serviceFamilyTreeManipulator.removeByParentGUID( guid );
        this.cacheMap.remove( guid );
        this.tireOwnerManipulator.removeBySubordinate(guid);
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
