package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericProperty;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.source.RegistryPropertiesManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.pinecone.ulf.util.guid.GUIDs;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
@IbatisDataAccessObject
public interface RegistryPropertiesMapper extends RegistryPropertiesManipulator {
    void insert( Property property );

    void remove( @Param("guid") GUID guid, @Param("key") String key );

    List<Map > getProperties0( @Param("guid") GUID guid );

    @Override
    default List<Property > getProperties( GUID guid, Properties parent ) {
        List<Map >    raws = this.getProperties0( guid );
        List<Property > ps = new ArrayList<>( raws.size() );

        for( Map raw : raws ) {
            Property property = new GenericProperty( parent );
            property.setEnumId( ( (Number) raw.get( "enumId" ) ).longValue() );
            property.setGuid  ( GUIDs.GUID128( (String) raw.get( "guid" ) )  );
            property.setType  ( (String) raw.get( "type" )  );
            property.setKey   ( (String) raw.get( "key" )   );

            property.setCreateTime ( ( (Timestamp) raw.get("createTime") ).toLocalDateTime() );
            property.setUpdateTime ( ( (Timestamp) raw.get("updateTime") ).toLocalDateTime() );
            property.setRawValue   ( raw.get( "rawValue" )   );

            ps.add( property );
        }
        return ps;
    }

    @SuppressWarnings( "unchecked" )
    default List<Property > getProperties( GUID guid ) {
        return (List) this.getProperties0( guid );
    }

    void update( Property property );

    void removeAll( @Param("guid") GUID guid );

    void copyPropertiesTo( @Param("sourceGuid") GUID sourceGuid, @Param("destinationGuid") GUID destinationGuid );
}
