package com.pinecone.hydra.deploy.kom;

import com.pinecone.framework.util.name.path.PathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.kom.StandardPathSelector;
import com.pinecone.hydra.unit.imperium.ImperialTree;

public class DeployPathSelector extends StandardPathSelector {
    public DeployPathSelector(PathResolver pathResolver, ImperialTree trieTree, GUIDNameManipulator dirMan, GUIDNameManipulator[] fileMans ) {
        super( pathResolver, trieTree, dirMan, fileMans );
    }
}
