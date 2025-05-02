package com.pinecone.hydra.task.kom;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.JobElement;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.system.ko.kom.ReparseKOMTree;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface TaskInstrument extends ReparseKOMTree {

    ServiceConfig KernelServiceConfig = new KernelServiceConfig();

    JobElement         affirmJob( String path );

    Namespace          affirmNamespace( String path );

    TaskElement        affirmTask( String path );

    ElementNode        queryElement( String path );

    boolean            containsChild( GUID parentGuid, String childName );

    void               update( TreeNode treeNode );

}
