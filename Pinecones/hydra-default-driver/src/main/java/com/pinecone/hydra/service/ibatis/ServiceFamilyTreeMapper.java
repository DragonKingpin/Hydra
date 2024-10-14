package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.source.ServiceFamilyTreeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface ServiceFamilyTreeMapper extends ServiceFamilyTreeManipulator {
    @Insert("INSERT INTO hydra_service_family_tree (`child_guid`, `parent_guid`) VALUES (#{childGUID},#{parentGUID})")
    void insert(@Param("childGUID") GUID childGUID, @Param("parentGUID") GUID parentGUID);

    @Delete("DELETE FROM hydra_service_family_tree WHERE `child_guid`=#{childGUID}")
    void removeByChildGUID(@Param("childGUID") GUID childGUID);

    @Delete("DELETE FROM hydra_service_family_tree WHERE `parent_guid`=#{parentGUID}")
    void removeByParentGUID(@Param("parentGUID") GUID parentGUID);

    @Delete("DELETE FROM hydra_service_family_tree WHERE `parent_guid`=#{parentGUID} AND `child_guid`=#{childGUID}")
    void remove(@Param("childGUID") GUID childGUID,@Param("parentGUID") GUID parentGUID);

    @Select("SELECT `parent_guid` FROM hydra_service_family_tree WHERE `child_guid`=#{childGUID}")
    GUID getParentByChildGUID(@Param("childGUID") GUID childGUID);

    @Select("SELECT `child_guid` FROM hydra_service_family_tree WHERE `parent_guid`=#{parentGUID}")
    GUID getChildByParentGUID(GUID parentGUID);
}
