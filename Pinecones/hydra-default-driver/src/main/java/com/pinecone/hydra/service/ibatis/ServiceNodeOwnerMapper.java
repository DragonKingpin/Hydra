package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.source.ServiceNodeOwnerManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeOwnerManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
@Mapper
public interface ServiceNodeOwnerMapper extends ScopeOwnerManipulator {
    @Insert("INSERT INTO `hydra_service_node_owner` (`subordinate_guid`, `owner_guid`) VALUES (#{subordinateGuid},#{ownerGuid})")
    void insert(@Param("subordinateGuid") GUID subordinateGuid, @Param("ownerGuid") GUID ownerGuid);

    @Delete("DELETE FROM `hydra_service_node_owner` WHERE `subordinate_guid`=#{subordinateGuid} AND `owner_guid`=#{ownerGuid}")
    void remove(@Param("subordinateGuid") GUID subordinateGuid,@Param("ownerGuid") GUID ownerGuid);

    @Delete("DELETE FROM `hydra_service_node_owner` WHERE `subordinate_guid`=#{subordinateGuid}")
    void removeBySubordinate(GUID subordinateGuid);

    @Select("SELECT `owner_guid` FROM `hydra_service_node_owner` WHERE `subordinate_guid`=#{guid}")
    GUID getOwner(GUID guid);
    @Update("UPDATE `hydra_service_node_owner` SET `owner_guid`=#{ownerGuid} WHERE `subordinate_guid`=#{subordinateGuid}")
    void update(@Param("subordinateGuid") GUID subordinateGuid,@Param("ownerGuid") GUID ownerGuid);
    @Select("SELECT `subordinate_guid` FROM `hydra_service_node_owner` WHERE `owner_guid`=#{guid}")
    List<GUID> getSubordinates(GUID guid);
}
