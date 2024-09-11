package com.pinecone.hydra.scenario.tree;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.TreeNode;
import com.pinecone.hydra.scenario.source.ManipulatorSharer;
import com.pinecone.hydra.scenario.source.NamespaceNodeManipulator;
import com.pinecone.hydra.scenario.source.NamespaceNodeMetaManipulator;
import com.pinecone.hydra.scenario.source.ScenarioCommonDataManipulator;
import com.pinecone.hydra.unit.udsn.DistributedScopeTree;

public class GenericDistributedScenarioMetaTree implements DistributedScenarioMetaTree{
    private DistributedScopeTree            distributedScenarioTree;
    private ManipulatorSharer               manipulatorSharer;

    private NamespaceNodeMetaManipulator    namespaceNodeMetaManipulator;
    private NamespaceNodeManipulator        namespaceNodeManipulator;
    private ScenarioCommonDataManipulator   scenarioCommonDataManipulator;

    public GenericDistributedScenarioMetaTree(ManipulatorSharer manipulatorSharer){
        this.manipulatorSharer              =   manipulatorSharer;
        this.distributedScenarioTree        =   new GenericDistributedScenarioTree(this.manipulatorSharer);
        this.namespaceNodeManipulator       =   this.manipulatorSharer.getNamespaceNodeManipulator();
        this.namespaceNodeMetaManipulator   =   this.manipulatorSharer.getNamespaceNodeMetaManipulator();
        this.scenarioCommonDataManipulator  =   this.manipulatorSharer.getScenarioCommonDataManipulator();
    }

    @Override
    public String getPath(GUID guid) {
        return null;
    }

    @Override
    public GUID insert(TreeNode treeNode) {
        return null;
    }

    @Override
    public TreeNode get(GUID guid) {
        return null;
    }

    @Override
    public TreeNode parsePath(String path) {
        return null;
    }

    @Override
    public void remove(GUID guid) {

    }

    @Override
    public TreeNode getWithoutInheritance(GUID guid) {
        return null;
    }
}
