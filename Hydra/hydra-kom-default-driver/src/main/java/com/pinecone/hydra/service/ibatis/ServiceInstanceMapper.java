package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.kom.source.ServiceInstanceManipulator;
import com.pinecone.hydra.service.kom.entity.GenericServiceInstanceEntity;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface ServiceInstanceMapper extends ServiceInstanceManipulator {
    @Override
    void initServiceInstance(ServiceInstanceEntry element);

    @Override
    GenericServiceInstanceEntity queryServiceInstance( @Param("instanceId") GUID instanceId );

    @Override
    void updateServiceInstance(ServiceInstanceEntry element);
}
