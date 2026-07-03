package com.pinecone.hydra.account.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.GenericGroup;
import com.pinecone.hydra.account.entity.Group;
import com.pinecone.hydra.account.source.GroupNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@IbatisDataAccessObject
public interface GroupNodeMapper extends GroupNodeManipulator {
    void insert(Group group);

    void remove(@Param("groupGuid") GUID groupGuid);

    GenericGroup queryGroup(@Param("groupGuid") GUID groupGuid );

    List<GUID > getGuidsByName(@Param("name") String name );

    List<GUID > getGuidsByNameID(@Param("name") String name, @Param("guid") GUID guid );

    void update(Group group);
}
