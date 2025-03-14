package com.pinecone.hydra.task.kom;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.system.ko.kom.ReparseKOMTree;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import java.util.List;

public interface TasksInstrument extends ReparseKOMTree {
    TaskConfig KernelTaskConfig = new KernelTaskConfig();

    Namespace affirmNamespace   (String path );

    TaskElement affirmService     (String path );

    ElementNode queryElement      (String path );




    boolean            containsChild     (GUID parentGuid, String childName );

    void               update            ( TreeNode treeNode );

    List<TaskElement> fetchAllService();
}
