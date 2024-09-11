package com.pinecone.hydra.config.distribute.tree;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.source.ConfNodeOwnerManipulator;
import com.pinecone.hydra.config.distribute.source.ConfNodePathManipulator;
import com.pinecone.hydra.config.distribute.source.ConfTreeManipulator;
import com.pinecone.hydra.config.distribute.source.ConfManipulatorSharer;
import com.pinecone.hydra.unit.udsn.DistributedScopeTree;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

import java.util.List;

public class GenericDistributedConfTree implements DistributedScopeTree{
    private ConfManipulatorSharer           confManipulatorSharer;

    private ConfTreeManipulator             confTreeManipulator;

    private ConfNodeOwnerManipulator        confNodeOwnerManipulator;

    private ConfNodePathManipulator         confNodePathManipulator;

    public GenericDistributedConfTree(ConfManipulatorSharer confManipulatorSharer){
        this.confManipulatorSharer    =  confManipulatorSharer;
        this.confTreeManipulator      =  confManipulatorSharer.getConfTreeManipulator();
        this.confNodeOwnerManipulator =  confManipulatorSharer.getConfNodeOwnerManipulator();
        this.confNodePathManipulator  =  confManipulatorSharer.getConfNodePathManipulator();
    }

    @Override

    public void insert(DistributedTreeNode distributedConfTreeNode) {
        this.confTreeManipulator.insert(distributedConfTreeNode);
    }

    @Override
    public String getPath(GUID guid) {
        return null;
    }

    @Override
    public void insertNodeToParent(GUID nodeGUID, GUID parentGUID) {}

    @Override
    public GUIDDistributedScopeNode getNode(GUID guid) {
        return this.confTreeManipulator.getNode(guid);
    }

    @Override
    public DistributedTreeNode getParentNode(GUID guid) {
        return null;
    }

    @Override
    public void remove(GUID guid) {
        this.confTreeManipulator.remove(guid);
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
        return this.confTreeManipulator.getChild(guid);
    }

    @Override
    public List<GUID> getParentNodes(GUID guid) {
        return this.confTreeManipulator.getParentNodes(guid);
    }

    @Override
    public void removeInheritance(GUID childGuid, GUID parentGuid) {
        this.confTreeManipulator.removeInheritance(childGuid,parentGuid);
    }

    @Override
    public void removePath(GUID guid) {
        this.confNodePathManipulator.remove(guid);
    }

    @Override
    public GUID getOwner(GUID guid) {
        return this.confNodeOwnerManipulator.getOwner(guid);
    }

    @Override
    public List<GUID> getSubordinates(GUID guid) {
        return this.confNodeOwnerManipulator.getSubordinates(guid);
    }

    @Override
    public boolean hasOwnProperty(Object elm) {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

//    @Override
//    public void insertOwner(GUID subordinateGuid, GUID ownerGuid) {
//        this.confNodeOwnerManipulator.insert(subordinateGuid,ownerGuid);
//    }
//
//    @Override
//    public void updateOwner(GUID subordinateGuid, GUID ownerGuid) {
//
//    }
//
//    @Override
//    public void removeOwner(GUID subordinateGuid, GUID ownerGuid) {
//        this.confNodeOwnerManipulator.remove(subordinateGuid,ownerGuid);
//    }
//
//    @Override
//    public void removeOwnerBySubordinateGuid(GUID subordinateGuid) {
//        this.confNodeOwnerManipulator.removeBySubordinate(subordinateGuid);
//    }
}
