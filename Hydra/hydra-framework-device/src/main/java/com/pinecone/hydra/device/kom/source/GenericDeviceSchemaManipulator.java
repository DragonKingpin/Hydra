package com.pinecone.hydra.device.kom.source;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.generic.GenericDeviceSchema;

public interface GenericDeviceSchemaManipulator extends Pinenut {

    void insert( GenericDeviceSchema schema );

    void update( GenericDeviceSchema schema );

    GenericDeviceSchema getGenericDeviceSchema( GUID guid );

    GenericDeviceSchema getGenericDeviceSchemaByCode( String code );

    Collection<GenericDeviceSchema> fetchGenericDeviceSchemas();
}
