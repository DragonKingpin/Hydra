package com.pinecone.hydra.device.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.LinkedType;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@IbatisDataAccessObject
public interface DeviceNodeOwnerMapper extends TireOwnerManipulator {
    @Override
    void insertRootNode( @Param("guid")  GUID guid, @Param("linkedType") LinkedType linkedType );

    @Override
    void insert( @Param("targetGuid") GUID targetGuid, @Param("parentGuid") GUID parentGUID, @Param("linkedType") LinkedType linkedType );

    @Override
    void update( @Param("targetGuid") GUID targetGuid, @Param("parentGuid") GUID parentGUID, @Param("linkedType") LinkedType linkedType );

    @Override
    void updateParentGuid( @Param("targetGuid") GUID targetGuid, @Param("parentGuid") GUID parentGUID );

    @Override
    void updateLinkedType( @Param("targetGuid") GUID targetGuid, @Param("linkedType") LinkedType linkedType );

    @Override
    void remove( @Param("subordinateGuid") GUID subordinateGuid, @Param("ownerGuid") GUID ownerGuid );

    @Override
    void removeBySubordinate( @Param("subordinateGuid") GUID subordinateGuid );

    @Override
    GUID getOwner( @Param("subordinateGuid") GUID subordinateGuid );

    @Override
    List<GUID > getSubordinates( @Param("guid") GUID guid );


    void setLinkedType( @Param("sourceGuid") GUID sourceGuid, @Param("targetGuid") GUID targetGuid, @Param("linkedType") LinkedType linkedType );

    LinkedType getLinkedType( @Param("childGuid") GUID childGuid,@Param("parentGuid") GUID parentGuid );
}
