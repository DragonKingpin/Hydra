package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.LinkedType;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
@IbatisDataAccessObject
public interface FileOwnerMapper extends TireOwnerManipulator {
    void insertRootNode(@Param("guid")  GUID guid, @Param("linkedType") LinkedType linkedType );

    void insert( @Param("targetGuid") GUID targetGuid, @Param("parentGuid") GUID parentGUID, @Param("linkedType") LinkedType linkedType );



    void update( @Param("targetGuid") GUID targetGuid, @Param("parentGuid") GUID parentGUID, @Param("linkedType") LinkedType linkedType );

    void updateParentGuid( @Param("targetGuid") GUID targetGuid, @Param("parentGuid") GUID parentGUID );

    void updateLinkedType( @Param("targetGuid") GUID targetGuid, @Param("linkedType") LinkedType linkedType );



    void remove( @Param("subordinateGuid") GUID subordinateGuid, @Param("ownerGuid") GUID ownerGuid );

    void removeBySubordinate( GUID subordinateGuid );

//    void removeByOwner(GUID ownerGuid);

    GUID getOwner( GUID subordinateGuid );

    List<GUID > getSubordinates( GUID guid );


    void setLinkedType( @Param("sourceGuid") GUID sourceGuid, @Param("targetGuid") GUID targetGuid, @Param("linkedType") LinkedType linkedType );

    LinkedType getLinkedType( @Param("childGuid") GUID childGuid,@Param("parentGuid") GUID parentGuid );
}
