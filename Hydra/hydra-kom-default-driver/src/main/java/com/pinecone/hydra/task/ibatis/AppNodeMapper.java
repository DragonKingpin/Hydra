package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.AppElement;
import com.pinecone.hydra.task.kom.entity.GenericAppElement;
import com.pinecone.hydra.task.kom.source.AppNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface AppNodeMapper extends AppNodeManipulator {

    @Override
    void insert( AppElement appElement);

    @Override
    void remove(@Param("guid") GUID guid);

    GenericAppElement getAppElement(@Param("guid") GUID guid);

    @Override
    default AppElement getAppElement(GUID guid, TaskInstrument instrument ) {
        GenericAppElement element = this.getAppElement( guid );
        element.apply( instrument );

        return element;
    }

    @Override
    void update( AppElement appElement);


    @Override
    List<GUID > getGuidsByName( @Param("name") String name );

    @Override
    List<GUID > getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );
}
