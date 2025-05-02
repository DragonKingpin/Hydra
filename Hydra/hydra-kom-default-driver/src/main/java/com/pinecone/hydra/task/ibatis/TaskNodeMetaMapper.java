package com.pinecone.hydra.task.ibatis;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.task.kom.entity.GenericCommonMeta;
import com.pinecone.hydra.task.kom.source.NodeMetaManipulator;
import com.pinecone.hydra.task.kom.TaskFamilyNode;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;


@Mapper
@IbatisDataAccessObject
public interface TaskNodeMetaMapper extends NodeMetaManipulator {

    @Override
    @Insert("INSERT INTO `hydra_task_node_meta` (`guid`, `scenario`, marshalling_architecture, extra_information, `description`) " +
            "VALUES (#{guid}, #{scenario}, #{marshallingArchitecture}, #{extraInformation}, #{description})")
    void insert( TaskFamilyNode node );

    @Override
    @Delete("DELETE FROM `hydra_task_node_meta` WHERE `guid`=#{guid}")
    void remove( @Param("guid")GUID guid );

    @Override
    @Select("SELECT `id` AS `enumId`, `guid`, `scenario`, `marshalling_architecture` AS marshallingArchitecture, `extra_information` AS extraInformation, `description`" +
            " FROM `hydra_task_node_meta` WHERE `guid` = #{guid}")
    GenericCommonMeta getNodeCommonMeta( @Param("guid") GUID guid );

    @Override
    @Update("UPDATE `hydra_task_node_meta` SET " +
            "`scenario` = #{scenario}, `marshalling_architecture` = #{marshallingArchitecture}, " +
            "`extra_information` = #{extraInformation}, " +
            "`description` = #{description}")
    void update( TaskFamilyNode node );

    @Update("UPDATE `hydra_task_node_meta` SET `scenario` = #{scenario} WHERE `guid` = #{guid}")
    void updateScenario( @Param("scenario") String scenario, @Param("guid") GUID guid );

    @Update("UPDATE `hydra_task_node_meta` SET `marshalling_architecture` = #{marshallingArchitecture} WHERE `guid` = #{guid}")
    void updateMarshallingArchitecture( @Param("marshallingArchitecture") String marshallingArchitecture, @Param("guid") GUID guid );

    @Update("UPDATE `hydra_task_node_meta` SET `extra_information` = #{extraInformation} WHERE `guid` = #{guid}")
    void updateExtraInformation( @Param("extraInformation") String extraInformation, @Param("guid") GUID guid );

    @Update("UPDATE `hydra_task_node_meta` SET `priority` = #{priority} WHERE `guid` = #{guid}")
    void updatePriority( @Param("priority") String priority, @Param("guid") GUID guid );

    @Update("UPDATE `hydra_task_node_meta` SET `description` = #{description} WHERE `guid` = #{guid}")
    void updateDescription( @Param("description") String description, @Param("guid") GUID guid );

}
