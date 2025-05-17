package com.pinecone.hydra.system.ko.runtime;

import java.util.Collection;

import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface RuntimeInstrument extends KOMInstrument {

    Collection<TreeNode> fetchTreeNodes();

    TreeNode add( String mountPointPath, TreeNode that );

}
