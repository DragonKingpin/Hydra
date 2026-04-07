package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.pinecone.slime.meta.TableIndex64Meta;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface TaskNodeMapper extends TaskNodeManipulator {

    @Override
    void insert( TaskElement taskElement );

    @Override
    void remove( @Param("guid") GUID guid );

    GenericTaskElement getTaskNode0( @Param("guid") GUID guid );

    @Override
    default TaskElement getTaskNode( GUID guid, TaskInstrument instrument ) {
        GenericTaskElement taskElement = this.getTaskNode0( guid );
        taskElement.apply( instrument );
        return taskElement;
    }

    @Override
    void update( TaskElement taskElement );

    List<GenericTaskElement> fetchTaskNodeByName0( @Param("name") String name );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<TaskElement> fetchTaskNodeByName( String name ) {
        List<GenericTaskElement> list = this.fetchTaskNodeByName0( name );
        return (List) list;
    }

    @Override
    @Select( "SELECT `guid` FROM `hydra_task_task_node` WHERE `name` = #{name}" )
    List<GUID> getGuidsByName( String name );

    @Override
    @Select( "SELECT `guid` FROM `hydra_task_task_node` WHERE `name` = #{name} AND `guid` = #{guid}" )
    List<GUID> getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );





    @Override
    TableIndex64Meta selectSchedulableIdRange(
            @Param( "cycles" ) Collection<TaskScheduleCycle> cycles,
            @Param( "targetTime" ) LocalDateTime targetTime
    );

    List<GenericTaskElement> fetchSchedulableTasksInRange0(
            @Param( "idMin"  ) long idMin,
            @Param( "idMax"  ) long idMax,
            @Param( "cycles" ) Collection<TaskScheduleCycle> cycles,
            @Param( "targetTime" ) LocalDateTime targetTime
    );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<TaskElement> fetchSchedulableTasksInRange( long idMin, long idMax, Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime ) {
        List<GenericTaskElement> list = this.fetchSchedulableTasksInRange0( idMin, idMax, cycles, targetTime );
        return (List) list;
    }



    @Override
    List<TaskElement> listPage(int offset, int pageSize);

}
