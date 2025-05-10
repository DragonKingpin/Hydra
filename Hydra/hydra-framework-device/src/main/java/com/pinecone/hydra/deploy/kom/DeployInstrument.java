package com.pinecone.hydra.deploy.kom;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.kom.ReparseKOMTree;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;
import com.pinecone.hydra.deploy.kom.entity.JobElement;
import com.pinecone.hydra.deploy.kom.entity.Namespace;
import com.pinecone.hydra.deploy.kom.entity.DeployElement;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface DeployInstrument extends ReparseKOMTree {

    DeployConfig KERNEL_DEPLOY_CONFIG = new KernelDeployConfig();

    JobElement         affirmJob(String path);

    Namespace          affirmNamespace(String path);

    DeployElement affirmTask(String path);

    ElementNode        queryElement(String path);

    boolean            containsChild(GUID parentGuid, String childName);

    void               update(TreeNode treeNode);


}
