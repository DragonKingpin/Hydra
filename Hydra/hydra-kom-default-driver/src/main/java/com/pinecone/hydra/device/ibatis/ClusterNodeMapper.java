package com.pinecone.hydra.device.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ClusterElement;
import com.pinecone.hydra.device.kom.entity.GenericClusterElement;
import com.pinecone.hydra.device.kom.source.ClusterNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface ClusterNodeMapper extends ClusterNodeManipulator {

    @Override
    void insert( ClusterElement clusterElement);

    @Override
    void remove( @Param("guid") GUID guid );

    GenericClusterElement getAppElement(@Param("guid") GUID guid );

    @Override
    default ClusterElement getClusterElement(GUID guid, DeviceInstrument instrument ) {
        GenericClusterElement element = this.getAppElement( guid );
        if( element == null ) {
            return null;
        }
        element.apply( instrument );

        return element;
    }

    @Override
    void update( ClusterElement clusterElement);

    List<GenericClusterElement> fetchJobNodeByName0( @Param("name") String name );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<ClusterElement> fetchJobNodeByName( String name ) {
        List<GenericClusterElement> list = this.fetchJobNodeByName0( name );
        return (List) list;
    }

    @Override
    List<GUID > getGuidsByName( @Param("name") String name );

    @Override
    List<GUID > getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );
}
