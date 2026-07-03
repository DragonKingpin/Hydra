package com.pinecone.hydra.device.kom.source;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.generic.GenericDeviceType;

public interface GenericDeviceTypeManipulator extends Pinenut {

    void insert( GenericDeviceType type );

    void update( GenericDeviceType type );

    GenericDeviceType getGenericDeviceTypeByCode( String code );

    Collection<GenericDeviceType> fetchGenericDeviceTypes();
}
