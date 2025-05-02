package com.walnut.odin.task.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskFamilyMeta;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.task.entity.GenericRavenTaskMeta;
import com.walnut.odin.task.entity.RavenTaskMeta;
import com.walnut.odin.task.source.TaskExMetaManipulator;

@Mapper
@IbatisDataAccessObject
public interface TastExMetaMapper extends TaskExMetaManipulator {

    @Override
    @Insert("INSERT INTO `odin_task_task_node` " +
            "(`guid`, `task_version`, `is_root`) " +
            "VALUES (#{guid}, #{taskName}, #{taskVersion}, #{rootTask})")
    void insert( RavenTaskMeta taskMeta );

    @Override
    @Delete("DELETE FROM `odin_task_task_node` WHERE `guid` = #{guid}")
    void remove( @Param("guid") GUID guid );

    @Select("SELECT `guid`, `task_version`, `is_root` AS `rootTask` " +
            "FROM `odin_task_task_node` WHERE `guid` = #{guid}")
    GenericRavenTaskMeta getTaskExMeta0(@Param("guid") GUID guid );

    @Override
    default RavenTaskMeta getTaskExMeta( GUID guid, TaskFamilyMeta kernelMeta ) {
        RavenTaskMeta meta = this.getTaskExMeta0( guid );
        if ( meta != null ) {
            meta.setKernelMeta( kernelMeta );
        }
        return meta;
    }

    @Override
    @Update("UPDATE `odin_task_task_node` SET " +
            "`task_version` = #{taskVersion}, " +
            "`is_root` = #{rootTask} " +
            "WHERE `guid` = #{guid}")
    void update( RavenTaskMeta taskMeta );

}
