package com.pinecone.hydra.deploy.kom;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.entity.ContainerElement;
import com.pinecone.hydra.deploy.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.deploy.kom.entity.QuickElement;
import com.pinecone.hydra.deploy.kom.entity.ServerElement;
import com.pinecone.hydra.deploy.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.system.ko.kom.ReparseKOMTree;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;
import com.pinecone.hydra.deploy.kom.entity.ClusterElement;
import com.pinecone.hydra.deploy.kom.entity.Namespace;
import com.pinecone.hydra.deploy.kom.entity.DeployElement;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface DeployInstrument extends ReparseKOMTree {

    DeployConfig KERNEL_DEPLOY_CONFIG = new KernelDeployConfig();

    ClusterElement          affirmCluster(String path );

    Namespace               affirmNamespace( String path );

    ServerElement           affirmServer( String path );

    QuickElement            affirmQuick( String path );

    VirtualMachineElement   affirmVirtualMachine( String path );

    ContainerElement        affirmContainerElement(String path);

    PhysicalHostElement     affirmPhysicalHost(String path );

    ElementNode             queryElement( String path );

    boolean                 containsChild( GUID parentGuid, String childName );

    void                    update( TreeNode treeNode );


}
