package com.pinecone.hydra.task.kom;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.AppElement;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.system.ko.kom.ReparseKOMTree;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface TaskInstrument extends ReparseKOMTree {

    TaskConfig KernelServiceConfig = new KernelTaskConfig();

    AppElement affirmJob(String path );

    Namespace          affirmNamespace( String path );

    TaskElement        affirmTask( String path ,TaskElement metaInfos );

    ElementNode        queryElement( String path );

    boolean            containsChild( GUID parentGuid, String childName );

    void               update( TreeNode treeNode );

    InstanceInstrument getInstanceInstrument();




}
