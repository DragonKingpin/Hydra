package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@IbatisDataAccessObject
public interface RegistryNodeOwnerMapper extends TireOwnerManipulator {
    @Insert("INSERT INTO `hydra_registry_node_tree` (`guid`, `parent_guid`,`linked_type`) VALUES (#{subordinateGuid},#{ownerGuid},'Owned')")
    void insert(@Param("subordinateGuid") GUID subordinateGuid, @Param("ownerGuid") GUID ownerGuid);

    @Delete("DELETE FROM `hydra_registry_node_tree` WHERE `guid`=#{subordinateGuid}  AND `linked_type`='Owned'")
    void remove(@Param("subordinateGuid") GUID subordinateGuid, @Param("ownerGuid") GUID ownerGuid);

    @Delete("DELETE FROM `hydra_registry_node_tree` WHERE `guid`=#{subordinateGuid} AND `linked_type`='Owned'")
    void removeBySubordinate(GUID subordinateGuid);

//    @Delete("DELETE FROM `hydra_registry_node_owner` WHERE `owner_guid`=#{ownerGuid}")
//    void removeByOwner(GUID ownerGuid);

    @Select("SELECT `parent_guid` FROM `hydra_registry_node_tree` WHERE `guid`=#{subordinateGuid} AND linked_type='Owned'")
    GUID getOwner(GUID subordinateGuid);

    @Select("SELECT guid FROM hydra_registry_node_tree where parent_guid=#{guid} AND linked_type='Owned'")
    List<GUID> getSubordinates(GUID guid);
    @Select("SELECT parent_guid FROM hydra_registry_node_tree WHERE guid=#{guid} AND linked_type='Owned'")
    GUIDDistributedTrieNode checkOwned(GUID guid);
    @Update("UPDATE `hydra_registry_node_tree` SET `linked_type` ='Owned' WHERE `guid` = #{sourceGuid} AND `parent_guid` = #{targetGuid}")
    void setOwned(@Param("sourceGuid") GUID sourceGuid,@Param("targetGuid") GUID targetGuid);
    @Update("UPDATE `hydra_registry_node_tree` SET `linked_type` ='Reparse' WHERE `guid` = #{sourceGuid} AND `parent_guid` = #{targetGuid}")
    void setReparse(@Param("sourceGuid") GUID sourceGuid,@Param("targetGuid") GUID targetGuid);
    @Select("SELECT `linked_type` FROM `hydra_registry_node_tree` WHERE `guid` = #{childGuid} AND `parent_guid` =#{parentGuid}")
    String getLinkedType(@Param("childGuid") GUID childGuid,@Param("parentGuid") GUID parentGuid);
}
