package com.pinecone.hydra.service.kom;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.ApplicationElement;
import com.pinecone.hydra.service.kom.entity.ElementNode;
import com.pinecone.hydra.service.kom.entity.Namespace;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ReparseKOMTree;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import java.util.List;

public interface ServiceInstrument extends ReparseKOMTree {

    ServiceConfig KernelServiceConfig = new KernelServiceConfig();

    ServiceMasterManipulator getServiceMasterManipulator();

    ApplicationElement affirmApplication ( String path );

    Namespace          affirmNamespace   ( String path );

    ServiceElement     affirmService     ( String path );

    ElementNode        queryElement      ( String path );

    boolean            containsChild     ( GUID parentGuid, String childName );

    void               update            ( TreeNode treeNode );

    List<ServiceElement> fetchAllService();

    List<ServiceElement> fetchServices( ServiceElementQuery query );

    List<ServiceElement> fetchServicesByGuids( List<GUID> guids );

    long countServices( ServiceElementQuery query );

    ServiceElementPage fetchServicePage( ServiceElementQuery query );

    void createServiceInstance( ServiceInstanceEntry serviceInstanceEntry);

    ServiceInstanceEntry queryServiceInstance(GUID serviceId );

    List<ServiceInstanceEntry> fetchServiceInstances( ServiceInstanceQuery query );

    long countServiceInstances( ServiceInstanceQuery query );

    List<ServiceInstanceEntry> fetchServiceInstancesByServiceGuid( GUID serviceGuid );

    ServiceInstancePage fetchServiceInstancePage( ServiceInstanceQuery query );

    void updateServiceInstance( ServiceInstanceEntry element );

}
