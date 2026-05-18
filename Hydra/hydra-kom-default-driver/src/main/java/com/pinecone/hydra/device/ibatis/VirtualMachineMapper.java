package com.pinecone.hydra.device.ibatis;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.GenericVirtualMachineElement;
import com.pinecone.hydra.device.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.device.kom.source.VirtualMachineManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface VirtualMachineMapper extends VirtualMachineManipulator {

    void insert( VirtualMachineElement virtualMachineElement );

    void update( VirtualMachineElement serviceElement );

    void remove( @Param("guid") GUID guid );

    GenericVirtualMachineElement getDeviceNode0( @Param("guid") GUID guid );

    @Override
    default VirtualMachineElement getDeviceNode( GUID guid, DeviceInstrument instrument ){
        GenericVirtualMachineElement element = this.getDeviceNode0( guid );
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
