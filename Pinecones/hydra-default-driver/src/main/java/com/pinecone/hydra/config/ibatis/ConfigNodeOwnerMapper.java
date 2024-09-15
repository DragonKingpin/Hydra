package com.pinecone.hydra.config.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.source.ScopeOwnerManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ConfigNodeOwnerMapper extends ScopeOwnerManipulator {
    @Insert("INSERT INTO `hydra_conf_node_owner` (`subordinate_guid`, `owner_guid`) VALUES (#{subordinateGuid},#{ownerGuid})")
    void insert(@Param("subordinateGuid") GUID subordinateGuid, @Param("ownerGuid") GUID ownerGuid);

    @Delete("DELETE FROM `hydra_conf_node_owner` WHERE `subordinate_guid`=#{subordinateGuid} AND `owner_guid`=#{ownerGuid}")
    void remove(@Param("subordinateGuid") GUID subordinateGuid, @Param("ownerGuid") GUID ownerGuid);

    @Delete("DELETE FROM `hydra_conf_node_owner` WHERE `subordinate_guid`=#{subordinateGuid}")
    void removeBySubordinate(GUID subordinateGuid);

    @Delete("DELETE FROM `hydra_conf_node_owner` WHERE `owner_guid`=#{ownerGuid}")
    void removeByOwner(GUID ownerGuid);

    @Select("SELECT `id`, `subordinate_guid` AS subordinateGuid, `owner_guid` AS ownerGuid FROM `hydra_conf_node_owner` WHERE `subordinate_guid`=#{subordinateGuid}")
    GUID getOwner(GUID subordinateGuid);

    @Select("SELECT subordinate_guid FROM hydra_conf_node_owner where owner_guid=#{guid}")
    List<GUID> getSubordinates(GUID guid);
}
