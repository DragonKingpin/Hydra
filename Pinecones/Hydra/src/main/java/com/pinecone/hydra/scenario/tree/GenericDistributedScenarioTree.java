package com.pinecone.hydra.scenario.tree;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.scenario.source.ManipulatorSharer;
import com.pinecone.hydra.scenario.source.ScenarioNodePathManipulator;
import com.pinecone.hydra.scenario.source.ScenarioTreeManipulator;
import com.pinecone.hydra.unit.udsn.DistributedScopeTree;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

import java.util.List;

public class GenericDistributedScenarioTree implements DistributedScopeTree {
    private ManipulatorSharer                       manipulatorSharer;
    private ScenarioTreeManipulator                scenarioTreeManipulator;
    private ScenarioNodePathManipulator            scenarioNodePathManipulator;

    public GenericDistributedScenarioTree(ManipulatorSharer manipulatorSharer){
        this.manipulatorSharer            =   manipulatorSharer;
        this.scenarioTreeManipulator      =   manipulatorSharer.getScenarioTreeManipulator();
        this.scenarioNodePathManipulator  =   manipulatorSharer.getScenarioNodePathManipulator();
    }
    @Override
    public DistributedTreeNode getParentNode(GUID guid) {
        return null;
    }

    @Override
    public void insert(DistributedTreeNode distributedConfTreeNode) {
        this.scenarioTreeManipulator.insert(distributedConfTreeNode);
    }

    @Override
    public String getPath(GUID guid) {
        return this.scenarioNodePathManipulator.getPath(guid);
    }

    @Override
    public void insertNodeToParent(GUID nodeGUID, GUID parentGUID) {

    }

    @Override
    public GUIDDistributedScopeNode getNode(GUID guid) {
        return this.scenarioTreeManipulator.getNode(guid);
    }

    @Override
    public void remove(GUID guid) {
        this.scenarioTreeManipulator.remove(guid);
    }

    @Override
    public void put(GUID guid, GUIDDistributedScopeNode distributedTreeNode) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public boolean containsKey(GUID key) {
        return false;
    }

    @Override
    public GUID parsePath(String path) {
        return null;
    }

    @Override
    public List<GUIDDistributedScopeNode> getChildNode(GUID guid) {
        return null;
    }

    @Override
    public List<GUID> getParentNodes(GUID guid) {
        return this.scenarioTreeManipulator.getParent(guid);
    }

    @Override
    public void removeInheritance(GUID childGuid, GUID parentGuid) {
        this.scenarioTreeManipulator.removeInheritance(childGuid,parentGuid);
    }

    @Override
    public void removePath(GUID guid) {
        this.scenarioNodePathManipulator.remove(guid);
    }

    @Override
    public GUID getOwner(GUID guid) {
        return null;
    }

    @Override
    public List<GUID> getSubordinates(GUID guid) {
        return null;
    }

    @Override
    public boolean hasOwnProperty(Object elm) {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }
}
