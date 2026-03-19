package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    List<GUID> getGuidsByName( String name );

    @Override
    List<GUID> getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );



}
