package com.pinecone.hydra.deploy.ibatis;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericVirtualMachineElement;
import com.pinecone.hydra.deploy.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.deploy.kom.source.VirtualMachineManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface VirtualMachineMapper extends VirtualMachineManipulator {

    void insert( VirtualMachineElement virtualMachineElement );

    void update( VirtualMachineElement serviceElement );

    void remove( @Param("guid") GUID guid );

    GenericVirtualMachineElement getDeployNode0( @Param("guid") GUID guid );

    @Override
    default VirtualMachineElement getDeployNode( GUID guid, DeployInstrument instrument ){
        GenericVirtualMachineElement element = this.getDeployNode0( guid );
        if( element == null ) {
            return null;
        }
        element.apply( instrument );
        return element;
    }

    @Override
    List<GUID> getGuidsByName( @Param("name") String name );

    @Override
    List<GUID> getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );


}
