package com.walnut.odin.task.mapper;

import com.pinecone.hydra.task.kom.instance.source.InstanceNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.InstanceAtlasNode;

@IbatisDataAccessObject
public interface InstanceAtlasNodeMapper extends InstanceNodeManipulator {

    void insertInstanceAtlasNode( InstanceAtlasNode instanceAtlasNode);

}
