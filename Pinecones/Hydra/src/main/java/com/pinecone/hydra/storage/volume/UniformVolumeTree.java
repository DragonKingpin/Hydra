package com.pinecone.hydra.storage.volume;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchKOMTree;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.hydra.unit.udtt.entity.EntityNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.ulf.util.id.GuidAllocator;

import java.util.List;

public class UniformVolumeTree extends ArchKOMTree implements VolumeTree{


    public UniformVolumeTree(Hydrarum hydrarum, KOIMasterManipulator masterManipulator) {
        super(hydrarum, masterManipulator, VolumeTree.KernelVolumeConfig);
    }

    @Override
    public GuidAllocator getGuidAllocator() {
        return null;
    }

    @Override
    public DistributedTrieTree getMasterTrieTree() {
        return null;
    }

    @Override
    public KernelObjectConfig getConfig() {
        return null;
    }

    @Override
    public String getPath(GUID guid) {
        return null;
    }

    @Override
    public String getFullName(GUID guid) {
        return null;
    }

    @Override
    public GUID queryGUIDByPath(String path) {
        return null;
    }

    @Override
    public GUID queryGUIDByFN(String fullName) {
        return null;
    }

    @Override
    public GUID put(TreeNode treeNode) {
        return null;
    }

    @Override
    public TreeNode get(GUID guid) {
        return null;
    }

    @Override
    public GUID queryGUIDByNS(String path, String szBadSep, String szTargetSep) {
        return null;
    }

    @Override
    public TreeNode get(GUID guid, int depth) {
        return null;
    }

    @Override
    public TreeNode getSelf(GUID guid) {
        return null;
    }

    @Override
    public void remove(GUID guid) {

    }

    @Override
    public void remove(String path) {

    }

    @Override
    public List<TreeNode> getChildren(GUID guid) {
        return null;
    }

    @Override
    public Object queryEntityHandleByNS(String path, String szBadSep, String szTargetSep) {
        return null;
    }

    @Override
    public EntityNode queryNode(String path) {
        return null;
    }

    @Override
    public List<? extends TreeNode> listRoot() {
        return null;
    }

    @Override
    public void rename(GUID guid, String name) {

    }
}
