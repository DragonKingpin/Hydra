package com.pinecone.hydra.task.kom;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.digest.TaskElementDigest;
import com.pinecone.hydra.task.kom.digest.TaskTreeElementDigest;
import com.pinecone.hydra.task.kom.entity.AppElement;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.system.ko.kom.ReparseKOMTree;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import java.util.Collection;
import java.util.List;

public interface TaskInstrument extends ReparseKOMTree {

    TaskConfig KernelServiceConfig = new KernelTaskConfig();

    AppElement         affirmApp( String path );

    Namespace          affirmNamespace( String path );

    TaskElement        affirmTask( String path ,TaskElement metaInfos );

    ElementNode        queryElement( String path );

    boolean            containsChild( GUID parentGuid, String childName );

    void               move( String sourcePath, String destinationPath );

    void               move( GUID sourceGuid, GUID destinationGuid );

    void               update( TreeNode treeNode );

    InstanceInstrument getInstanceInstrument();

    TaskTreeElementDigest queryTaskTreeDigestByPath( String path );

    TaskTreeElementDigest queryTaskTreeDigestByGuid( GUID guid );

    List<TaskTreeElementDigest> fetchTaskTreeChildDigests( GUID parentGuid );

    List<TaskElementDigest> listTaskElementDigests( int offset, int pageSize );

    List<TaskElementDigest> fetchTaskElementDigestsByGuids( Collection<GUID> guids );




}
