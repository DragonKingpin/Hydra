package com.pinecone.hydra.layer.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.LinkedType;
import com.pinecone.hydra.unit.imperium.entity.HardlinkEntry;
import com.pinecone.hydra.unit.imperium.entity.TreeReparseLinkNode;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerTreeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@IbatisDataAccessObject
public interface LayerTreeMapper extends LayerTreeManipulator {
    @Insert("INSERT INTO hydra_atlas_layer_tree_nodes (`guid`) VALUES ( #{guid} )")
    void insertRootNode(@Param("guid")  GUID guid);

    @Override
    default void insert ( TireOwnerManipulator ownerManipulator, GUIDImperialTrieNode node ){
        this.insertTreeNode( node.getGuid(), node.getType(), node.getAttributesGUID(), node.getNodeMetadataGUID() );
        ownerManipulator.insertRootNode( node.getGuid() );
    }

    @Insert("INSERT INTO `hydra_atlas_layer_tree_nodes` (`guid`, `type`,`base_data_guid`,`node_meta_guid`) VALUES (#{guid},#{type},#{baseDataGuid},#{nodeMetaGuid})")
    void insertTreeNode( @Param("guid") GUID guid, @Param("type") UOI type, @Param("baseDataGuid") GUID baseDataGuid, @Param("nodeMetaGuid") GUID nodeMetaGuid );

    @Select("SELECT `id` AS `enumId`, `guid`, `type`, base_data_guid AS baseDataGUID, node_meta_guid AS nodeMetadataGUID FROM hydra_atlas_layer_tree_nodes WHERE guid=#{guid}")
    GUIDImperialTrieNode getNodeExtendsFromMeta(GUID guid );

    @Select("SELECT COUNT( `id` ) FROM hydra_atlas_layer_tree_nodes WHERE guid=#{guid}")
    boolean contains( GUID key );

    @Override
    default GUIDImperialTrieNode getNode(GUID guid ) {
        GUIDImperialTrieNode node = this.getNodeExtendsFromMeta( guid );
        if( node == null ){
            return node;
        }
        List<GUID > parent = this.fetchParentGuids( guid );
        node.setParentGUID( parent );
        return node;
    }

    @Select("SELECT id, guid, parent_guid FROM hydra_atlas_layer_tree WHERE guid = #{guid} AND parent_guid = #{parentGuid}")
    GUIDImperialTrieNode getTreeNodeOnly(@Param("guid") GUID guid, @Param("parentGuid") GUID parentGuid );

    @Select("SELECT count( * ) FROM hydra_atlas_layer_tree WHERE guid = #{guid} AND parent_guid = #{parentGuid}")
    long countNode( GUID guid, GUID parentGuid );


    @Override
    default void purge( GUID guid ) {
        this.removeNodeMeta( guid );
        this.removeTreeNode( guid );
    }

    @Delete("DELETE FROM `hydra_atlas_layer_tree_nodes` WHERE `guid`=#{guid}")
    void removeNodeMeta( @Param("guid") GUID guid );

    @Delete("DELETE FROM `hydra_atlas_layer_tree` WHERE `guid` = #{guid}")
    void removeTreeNode( @Param("guid") GUID guid );

    @Delete("DELETE FROM `hydra_atlas_layer_tree` WHERE `parent_guid` = #{parent_guid}")
    void removeTreeNodeByParentGuid( @Param("parent_guid") GUID parentGuid );

    @Delete("DELETE FROM `hydra_atlas_layer_tree` WHERE `guid` = #{guid} AND `parent_guid` = #{parent_guid}")
    void removeTreeNodeYoke( @Param("guid") GUID guid, @Param("parent_guid") GUID parentGuid );


    @Delete("DELETE FROM `hydra_atlas_layer_tree` WHERE `guid`=#{childGuid} AND `parent_guid`=#{parentGuid}")
    void removeInheritance( @Param("childGuid") GUID childGuid, @Param("parentGuid") GUID parentGuid );

    @Select("SELECT `id` AS `enumId`, `guid`, `parent_guid` AS parentGuid FROM `hydra_atlas_layer_tree` WHERE `parent_guid`=#{guid}")
    List<GUIDImperialTrieNode> getChildren(GUID guid );

    @Select("SELECT `guid` FROM `hydra_atlas_layer_tree` WHERE `parent_guid` = #{parentGuid}")
    List<GUID > fetchChildrenGuids( @Param("parentGuid") GUID parentGuid );

    @Select("SELECT `parent_guid` FROM `hydra_atlas_layer_tree` WHERE `guid`=#{guid}")
    List<GUID > fetchParentGuids( GUID guid );

    @Update("UPDATE `hydra_atlas_layer_tree_nodes` SET `type` = #{type} WHERE guid=#{guid}")
    void updateType( UOI type , GUID guid );

    @Select( "SELECT guid FROM hydra_atlas_layer_tree WHERE parent_guid IS NULL " )
    List<GUID > fetchRoot(@Param("tableName") String tableName);

    @Override
    @Select( "SELECT COUNT( `guid` ) FROM hydra_atlas_layer_tree WHERE `parent_guid` IS NULL AND guid = #{guid}" )
    boolean isRoot( GUID guid );

    @Update("UPDATE hydra_atlas_layer_tree SET parent_guid = #{parentGuid} WHERE guid = #{childGuid}")
    void addChild( @Param("childGuid") GUID childGuid, @Param("parentGuid") GUID parentGuid );




    @Override
    @Select( "SELECT COUNT( `guid` ) FROM hydra_atlas_layer_tree WHERE `guid` = #{guid} AND `linked_type` = #{linkedType}" )
    long queryLinkedCount( @Param("guid") GUID guid, @Param("linkedType") LinkedType linkedType );

    @Override
    @Select( "SELECT COUNT( `guid` ) FROM hydra_atlas_layer_tree WHERE `guid` = #{guid}" )
    long queryAllLinkedCount( @Param("guid") GUID guid );


    @Override
    @Insert(
            "INSERT INTO `hydra_atlas_layer_tree` (`guid`, `linked_type`,`tag_name`,`tag_guid`,`parent_guid`) " +
                    "VALUES (#{originalGuid}, #{linkedType}, #{tagName}, #{tagGuid}, #{dirGuid})"
    )
    void newLinkTag(
            @Param("originalGuid") GUID originalGuid, @Param("dirGuid") GUID dirGuid,
            @Param("tagName") String tagName, @Param("tagGuid") GUID tagGuid, @Param("linkedType") LinkedType linkedType
    );

    @Override
    @Update( "UPDATE hydra_atlas_layer_tree SET tag_name = #{tagName} WHERE tag_guid =#{tagGuid}" )
    void updateLinkTagName( @Param("tagGuid") GUID tagGuid, @Param("tagName") String tagName );

    @Override
    @Select( "SELECT `guid` FROM hydra_atlas_layer_tree WHERE tag_name = #{tagName} AND parent_guid = #{dirGuid}" )
    GUID getOriginalGuid( @Param("tagName") String tagName, @Param("dirGuid") GUID dirGuid );

    @Override
    @Select( "SELECT `guid` FROM hydra_atlas_layer_tree WHERE tag_name = #{tagName} AND guid = #{nodeGuid}" )
    GUID getOriginalGuidByNodeGuid( @Param("tagName") String tagName, @Param("nodeGuid") GUID nodeGUID );

    @Override
    @Select( "SELECT `guid` AS targetNodeGuid, `parent_guid` AS parentNodeGuid, `linked_type` AS linkedType, `tag_name` AS tagName, `tag_guid` AS tagGuid FROM hydra_atlas_layer_tree WHERE tag_name = #{tagName} AND parent_guid = #{parentDirGuid}" )
    TreeReparseLinkNode getReparseLinkNode(@Param("tagName") String tagName, @Param("parentDirGuid") GUID parentDirGuid );

    @Override
    @Select( "SELECT `guid` AS targetNodeGuid, `parent_guid` AS parentNodeGuid, `linked_type` AS linkedType, `tag_name` AS tagName, `tag_guid` AS tagGuid FROM hydra_atlas_layer_tree WHERE tag_name = #{tagName} AND guid = #{nodeGuid}" )
    TreeReparseLinkNode getReparseLinkNodeByNodeGuid( @Param("tagName") String tagName, @Param("nodeGuid") GUID nodeGUID );

    @Override
    @Select( "SELECT `guid` FROM hydra_atlas_layer_tree WHERE `tag_name` = #{tagName}" )
    List<GUID > fetchOriginalGuid( String tagName );

    @Override
    @Select( "SELECT `guid` FROM hydra_atlas_layer_tree WHERE `tag_name` = #{tagName} AND `parent_guid` IS NULL" )
    List<GUID > fetchOriginalGuidRoot( String tagName );

    @Override
    @Select( "SELECT COUNT(*) FROM `hydra_atlas_layer_tree` WHERE `tag_guid` = #{guid}" )
    boolean isTagGuid(GUID guid);

    @Override
    @Delete( "DELETE FROM `hydra_atlas_layer_tree` WHERE `tag_guid` = #{guid}" )
    void removeReparseLink( GUID guid );

    @Override
    @Select( "SELECT `guid` FROM `hydra_atlas_layer_tree` WHERE `tag_guid` = #{tagGuid}" )
    GUID getOriginalGuidByTagGuid(GUID tagGuid);

    @Override
    @Select(
            "<script>" +
                    "SELECT `guid` AS targetNodeGuid, `parent_guid` AS parentNodeGuid, `linked_type` AS linkedType, " +
                    "`tag_name` AS tagName, `tag_guid` AS tagGuid, `create_time` AS createTime, `update_time` AS updateTime " +
                    "FROM `hydra_atlas_layer_tree` " +
                    "WHERE `linked_type` = 'Hard' " +
                    "<if test='keyword != null and keyword != \"\"'>" +
                    "AND ( `tag_name` LIKE CONCAT('%', #{keyword}, '%') " +
                    "OR CAST(`guid` AS CHAR) LIKE CONCAT('%', #{keyword}, '%') " +
                    "OR CAST(`parent_guid` AS CHAR) LIKE CONCAT('%', #{keyword}, '%') " +
                    "OR CAST(`tag_guid` AS CHAR) LIKE CONCAT('%', #{keyword}, '%') ) " +
                    "</if>" +
                    "ORDER BY `id` DESC LIMIT #{limit} OFFSET #{offset}" +
                    "</script>"
    )
    List<HardlinkEntry> listHardlinks(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Override
    @Select(
            "<script>" +
                    "SELECT COUNT(*) FROM `hydra_atlas_layer_tree` " +
                    "WHERE `linked_type` = 'Hard' " +
                    "<if test='keyword != null and keyword != \"\"'>" +
                    "AND ( `tag_name` LIKE CONCAT('%', #{keyword}, '%') " +
                    "OR CAST(`guid` AS CHAR) LIKE CONCAT('%', #{keyword}, '%') " +
                    "OR CAST(`parent_guid` AS CHAR) LIKE CONCAT('%', #{keyword}, '%') " +
                    "OR CAST(`tag_guid` AS CHAR) LIKE CONCAT('%', #{keyword}, '%') ) " +
                    "</if>" +
                    "</script>"
    )
    long countHardlinks( @Param("keyword") String keyword );
}
