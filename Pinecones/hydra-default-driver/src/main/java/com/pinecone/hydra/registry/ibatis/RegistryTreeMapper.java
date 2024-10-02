package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.LinkedType;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@IbatisDataAccessObject
public interface RegistryTreeMapper extends TrieTreeManipulator {
    @Insert("INSERT INTO `hydra_registry_node_tree` (`guid`, `linked_type`) VALUES ( #{guid}, #{linkedType} )")
    void insertRootNode( @Param("guid")  GUID guid, @Param("linkedType") LinkedType linkedType );

    @Override
    default void insert ( TireOwnerManipulator ownerManipulator, GUIDDistributedTrieNode node ){
        this.insertTreeNode( node.getGuid(), node.getType(), node.getBaseDataGUID(), node.getNodeMetadataGUID() );
        ownerManipulator.insertRootNode( node.getGuid() );
    }

    @Insert("INSERT INTO hydra_registry_nodes (`guid`, `type`,`base_data_guid`,`node_meta_guid`) VALUES (#{guid},#{type},#{baseDataGuid},#{nodeMetaGuid})")
    void insertTreeNode( @Param("guid") GUID guid, @Param("type") UOI type, @Param("baseDataGuid") GUID baseDataGuid, @Param("nodeMetaGuid") GUID nodeMetaGuid );

    @Select("SELECT `id`, `guid`, `type`, base_data_guid AS baseDataGUID, node_meta_guid AS nodeMetadataGUID FROM hydra_registry_nodes WHERE guid=#{guid}")
    GUIDDistributedTrieNode getNodeExtendsFromMeta( GUID guid );

    @Override
    default GUIDDistributedTrieNode getNode( GUID guid ) {
        GUIDDistributedTrieNode node = this.getNodeExtendsFromMeta( guid );
        List<GUID > parent = this.getParentGuids( guid );
        node.setParentGUID( parent );
        return node;
    }

    @Select("SELECT id, guid, parent_guid, linked_type FROM hydra_registry_node_tree WHERE guid = #{guid} AND parent_guid = #{parentGuid}")
    GUIDDistributedTrieNode getTreeNodeOnly( @Param("guid") GUID guid, @Param("parentGuid") GUID parentGuid );

    @Select("SELECT count( * ) FROM hydra_registry_node_tree WHERE guid = #{guid} AND parent_guid = #{parentGuid}")
    long countNode( GUID guid, GUID parentGuid );



    @Override
    default void purge( GUID guid ) {
        this.removeNodeMeta( guid );
        this.removeTreeNode( guid );
        this.removeOwnedTreeNode( guid );
    }

    @Delete("DELETE FROM `hydra_registry_nodes` WHERE `guid`=#{guid}")
    void removeNodeMeta( @Param("guid") GUID guid );

    @Delete("DELETE FROM `hydra_registry_node_tree` WHERE `guid` = #{guid}")
    void removeTreeNode( @Param("guid") GUID guid );

    @Delete("DELETE FROM `hydra_registry_node_tree` WHERE `parent_guid` = #{parent_guid}")
    void removeTreeNodeByParentGuid( @Param("parent_guid") GUID parentGuid );

    @Delete("DELETE FROM `hydra_registry_node_tree` WHERE `guid` = #{guid} AND `parent_guid` = #{parent_guid}")
    void removeTreeNodeYoke( @Param("guid") GUID guid, @Param("parent_guid") GUID parentGuid );

    @Delete("DELETE FROM `hydra_registry_node_tree` WHERE `guid` = #{guid} AND `linked_type` = #{linkedType}")
    void removeTreeNodeWithLinkedType( @Param("guid") GUID guid, @Param("linkedType") LinkedType linkedType );




    @Delete("DELETE FROM `hydra_registry_node_tree` WHERE `guid`=#{chileGuid} AND `parent_guid`=#{parentGuid}")
    void removeInheritance( @Param("chileGuid") GUID childGuid,@Param("parentGUid") GUID parentGuid );

    @Select("SELECT `id`, `guid`, `parent_guid` AS parentGuid FROM `hydra_registry_node_tree` WHERE `parent_guid`=#{guid}")
    List<GUIDDistributedTrieNode > getChildren( GUID guid );

    @Select("SELECT `parent_guid` FROM `hydra_registry_node_tree` WHERE `guid`=#{guid}")
    List<GUID > getParentGuids( GUID guid );

    @Update("UPDATE `hydra_registry_nodes` SET `type` = #{type} WHERE guid=#{guid}")
    void updateType( UOI type , GUID guid );

    @Select( "SELECT guid FROM hydra_registry_node_tree WHERE parent_guid IS NULL " )
    List<GUID > listRoot();

}
